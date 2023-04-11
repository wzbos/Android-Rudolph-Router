package cn.wzbos.android.rudolph

import cn.wzbos.android.rudolph.AndroidModuleUtil.createApiModule
import cn.wzbos.android.rudolph.RudolphBuildInfo.Companion.load
import cn.wzbos.android.rudolph.annotations.*
import com.squareup.javapoet.*
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.IOException
import java.io.Writer
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.MirroredTypesException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.StandardLocation


/**
 * Router Processor
 * Created by wuzongbo on 2017/5/30.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(Constant.ANNOTATION_TYPE_ROUTE, Constant.ANNOTATION_TYPE_COMPONENT)
class RudolphProcessor : AbstractProcessor() {
    private var mFiler: Filer? = null
    private lateinit var logger: Logger
    private var types: Types? = null
    private var elements: Elements? = null
    private var activityIntentBuilderTm: TypeElement? = null
    private var fragmentBuilderTm: TypeElement? = null
    private var providerRouterBuilderTm: TypeElement? = null
    private var type_IBind: TypeElement? = null
    private var fragmentTm: TypeMirror? = null
    private var fragmentTmV4: TypeElement? = null
    private var fragmentAndroidX: TypeElement? = null
    private var activityTm: TypeMirror? = null
    private var serviceTm: TypeMirror? = null
    private var parcelableTM: TypeMirror? = null
    private var serializableTM: TypeMirror? = null
    private var buildInfo: RudolphBuildInfo? = null

    companion object {
        private val rudolph = ClassName.get(Constant.PACKAGE, "Rudolph")
        private val Gson = ClassName.get("com.google.gson", "Gson")
        private val Base64 = ClassName.get("android.util", "Base64")
        private val TypeToken = ClassName.get("com.google.gson.reflect", "TypeToken")
        private val clsContext = ClassName.get("android.content", "Context")
        private val Bundle = ClassName.get("android.os", "Bundle")
    }

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        mFiler = processingEnv.filer
        types = processingEnv.typeUtils
        logger = Logger(processingEnv.messager)

        processingEnv.elementUtils?.let {
            elements = it
            activityIntentBuilderTm = it.getTypeElement(Constant.ACTIVITY_INTENT_BUILDER)
            fragmentBuilderTm = it.getTypeElement(Constant.FRAGMENT_INTENT_BUILDER)
            providerRouterBuilderTm = it.getTypeElement(Constant.PROVIDER_ROUTER_BUILDER)
            type_IBind = it.getTypeElement(Constant.ROUTER_BINDER)
            parcelableTM = it.getTypeElement("android.os.Parcelable").asType()
            serializableTM = it.getTypeElement("java.io.Serializable").asType()
            activityTm = it.getTypeElement("android.app.Activity").asType()
            fragmentTm = it.getTypeElement("android.app.Fragment").asType()
            fragmentTmV4 = it.getTypeElement("android.support.v4.app.Fragment")
            fragmentAndroidX = it.getTypeElement("androidx.fragment.app.Fragment")
            serviceTm = it.getTypeElement(Constant.ROUTE_SERVICE).asType()
        }

        buildInfo = load(processingEnv)
        buildInfo?.let {
            logger.info("exportProtocolName=${it.exportProtocolName}")
            logger.info("exportProtocolPackage=${it.exportProtocolPackage}")

            if (it.exportApi()) {
                createApiModule(it)
            }
        }
    }

    private val routeClsName: String
        get() {
            val name = buildInfo?.moduleName?.replace("[^0-9a-zA-Z_]+".toRegex(), "")
            return name?.substring(0, 1)?.uppercase() + name?.substring(1) + "Routes"
        }

    override fun process(
        annotations: Set<TypeElement>,
        roundEnvironment: RoundEnvironment,
    ): Boolean {
        if (CollectionUtils.isNotEmpty(annotations)) {
            try {

                val clsName = routeClsName
                val superInterfaceType = elements!!.getTypeElement(Constant.ROUTE_TABLE)

                val clsBuilder = TypeSpec.classBuilder(clsName).addJavadoc(Constant.WARNING_TIPS)
                    .addSuperinterface(ClassName.get(superInterfaceType))
                    .addModifiers(Modifier.PUBLIC)

                //构件路由表类的初始化方法 init(Context context)
                val initMethodBuilder =
                    MethodSpec.methodBuilder("init").addAnnotation(Override::class.java)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(clsContext, "context").build())
                val components = roundEnvironment.getElementsAnnotatedWith(Component::class.java)
                if (CollectionUtils.isNotEmpty(components)) {
                    for (element in components) {
                        initMethodBuilder.addStatement("new \$T().init(context)", element)
                    }
                }

                //构件路由表类的初始化方法 register()
                val registerMethodBuilder =
                    MethodSpec.methodBuilder("register").addAnnotation(Override::class.java)
                        .addModifiers(Modifier.PUBLIC)
                val routers = roundEnvironment.getElementsAnnotatedWith(Route::class.java)
                if (CollectionUtils.isNotEmpty(routers)) {
                    var routetype: RouteType
                    for (element in routers) {
                        val route = element?.getAnnotation(Route::class.java)
                        val kind = element.kind
                        var target: ClassName
                        if (kind == ElementKind.CLASS) {
                            val tm = element.asType()
                            val typeElement = element as TypeElement
                            target = ClassName.get(typeElement)
                            routetype = when {
                                types?.isSubtype(tm, activityTm) == true -> {
                                    // Activity
                                    RouteType.ACTIVITY
                                }
                                types?.isSubtype(tm, fragmentTm) == true -> {
                                    RouteType.FRAGMENT
                                }
                                types?.isSubtype(tm, fragmentTmV4?.asType()) == true -> {
                                    RouteType.FRAGMENT_V4
                                }
                                types?.isSubtype(tm, fragmentAndroidX?.asType()) == true -> {
                                    RouteType.FRAGMENT_ANDROID_X
                                }
                                types?.isSubtype(tm, serviceTm) == true -> {
                                    RouteType.SERVICE
                                }
                                else -> {
                                    RouteType.UNKNOWN
                                }
                            }

                            generateRouterCls(typeElement, routetype, route!!)
                            generateRouteBinderCls(typeElement, target)
                        } else if (kind == ElementKind.METHOD) {
                            val executableElement = element as ExecutableElement
                            routetype = RouteType.METHOD
                            target =
                                ClassName.get(executableElement.enclosingElement as TypeElement)
                        } else {
                            logger.error("UnKnown route type:$kind")
                            continue
                        }
                        generateRouteTable(
                            registerMethodBuilder,
                            element,
                            target,
                            route!!,
                            routetype
                        )
                    }
                }
                JavaFile.builder(
                    Constant.PACKAGE_NAME,
                    clsBuilder.addMethod(registerMethodBuilder.build())
                        .addMethod(initMethodBuilder.build()).build()
                ).build().writeTo(mFiler)
                writeClsNameToAssets(clsName)
                logger.info(">>> Generated $clsName <<<")
            } catch (e: Exception) {
                e.printStackTrace()
                logger.error(e)
            }
            return true
        }
        return false
    }

    /**
     * 以文件名方式记录路由表名（assets/rudolph/xxxRoutes）
     *
     *
     * 生成路径：build/tmp/kapt3/classes/debug/assets/rudolph
     *
     *
     *  * 将路由表的类名以文件名方式创建一个空文件存于assets目录用于路由表初始化加载；
     *  * 此方式可以避免使用代码插装方式初始化路由表,相比更简单，更有效。
     *
     */
    private fun writeClsNameToAssets(filename: String) {
        var writer: Writer? = null
        try {
            val path = "rudolph/$filename"
            val fileObject = mFiler!!.createResource(StandardLocation.CLASS_OUTPUT, "assets", path)
            writer = fileObject.openWriter()
        } catch (e: Exception) {
            e.printStackTrace()
            logger.info(e.message!!)
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 获取协议层工程代码目录
     *
     * @return File
     */
    private val outputDirectory: File?
        get() {
            buildInfo?.let {
                if (!it.exportProtocolName.isNullOrEmpty()) {
                    return File(it.projectPath + "/" + it.exportProtocolName + "/src/main/java")
                }
            }
            return null
        }


    private fun Array<String>.split(): String {
        var splitStr = ""
        this.forEachIndexed { index, item ->
            if (index > 0) splitStr += ","
            splitStr += "\"$item\""
        }
        return splitStr
    }

    private fun getRouteUrl(element: Element?, route: Route): String {
        val urls = if (route.value.isNotBlank()) {
            arrayOf(*route.urls).plus(route.value)
        } else {
            route.urls
        }

        return if (urls.isEmpty()) {
            "\"/" + element.toString().lowercase(Locale.getDefault()) + "\""
        } else {
            urls.split()
        }
    }

    /**
     * 生成路由表类
     * <pre>
     * public class SampleARoutes implements IRouteTable {
     * @Override
     * public void init(Application application) {
     * new TestComponent().init(application);
     *
     * Rudolph.addRoute(new RouteInfo.Builder().routeType(RouteType.ACTIVITY)
     * .target(KotlinActivity.class)
     * .path("/kotlin/test")
     * .tag("")
     * .putParam("d3V6b25nYm8",String.class)
     * .putParam("userId",int.class)
     * .putParam("userName",String.class).build());
     * }
     * }
    </pre> *
     */
    private fun generateRouteTable(
        builder: MethodSpec.Builder,
        element: Element,
        target: ClassName,
        route: Route,
        routeType: RouteType,
    ) {

        val arrays: MutableList<Element> = ArrayList()

        builder.addCode(
            "\n\n\$T.addRoute(new \$T()"
                    + "\n\t.routeType(\$T.$routeType)"
                    + "\n\t.targetClass(\$T.class)",
            rudolph,
            ClassName.get(RouteInfo.Builder::class.java),  //RouteInfo.Builder
            ClassName.get(RouteType::class.java),  //RouteType
            target
        )


        builder.addCode("\n\t.url(${getRouteUrl(element, route)})")

        if (route.tag.isNotEmpty()) {
            builder.addCode("\n\t.tag(\$S)", route.tag)
        }

        try {
            route.interceptors
        } catch (e: MirroredTypesException) {
            if (!e.typeMirrors.isNullOrEmpty()) {
                builder.addCode("\n\t.interceptors(")
                e.typeMirrors.forEachIndexed { index, interceptor ->
                    if (index > 0) builder.addCode(", ")
                    builder.addCode("\$T.class", interceptor)
                }
                builder.addCode(")")
            }
        }

        if (element.kind == ElementKind.CLASS) {
            arrays.addAll(element.enclosedElements)
        } else {
            val method = element as ExecutableElement?
            arrays.addAll(method!!.parameters)
        }

        for (field in arrays) {
            val param = field.getAnnotation(Extra::class.java) ?: continue
            val argName = getArgName(field, param)
            val clsName: String = ClassName.get(field.asType()).toString()
            if (clsName.contains("<") && clsName.contains(">")) {
                builder.addCode(
                    "\n\t.extra(\$S, new \$T<\$T>(){}.getType(), ${param.base64}, ${param.json})",
                    argName,
                    TypeToken,
                    ClassName.get(field.asType())
                )
            } else {
                builder.addCode(
                    "\n\t.extra(\$S, \$T.class, ${param.base64}, ${param.json})",
                    argName,
                    ClassName.get(field.asType())
                )
            }
        }
        builder.addCode(".build());")
    }

    /**
     * 生成Router类 xxxRouter.java
     * <pre>
     * public class MainActivityRouter {
     * public static MainActivityRouter.Builder builder() {
     * return new MainActivityRouter.Builder();
     * }
     *
     * public static class Builder extends ActivityRouter.Builder<MainActivityRouter.Builder> {
     * Builder() {
     * super("/cn.wzbos.samplea.mainactivity");
     * }
     *
     * public MainActivityRouter.Builder index(int val) {
     * super.putExtra("index",val);
     * return this;
     * }
     *
     * public MainActivityRouter.Builder name(String val) {
     * super.putExtra("name",val);
     * return this;
     * }
     * }
     * }
    </MainActivityRouter.Builder></pre> *
     */
    @Throws(IllegalAccessException::class)
    private fun generateRouterCls(element: TypeElement?, routeType: RouteType, route: Route) {
        logger.info("generateRouterCls:" + element!!.simpleName + "Router")
        val export = element.getAnnotation(Export::class.java)
        var interfaceClsName: TypeName? = null
        val mirror = getInterface(route)
        if (mirror != null && ClassName.get(mirror) !== TypeName.get(Any::class.java)) {
            interfaceClsName = ClassName.get(mirror)
        }

        val clsName = if (export == null || "" == export.value) {
            element.simpleName.toString() + "Router"
        } else {
            export.value
        }
        val clsRouterBuilder = TypeSpec.classBuilder(clsName) //增加注释
            .addJavadoc(Constant.WARNING_TIPS).addModifiers(Modifier.PUBLIC)

        //处理路由服务类型
        if (routeType == RouteType.SERVICE) {
            if (interfaceClsName == null) {
                logger.error("@Route clazz:" + route.clazz.qualifiedName + " 不存在！")
                return
            }
            if (route.singleton) {
                clsRouterBuilder.addField(
                    interfaceClsName,
                    "instance",
                    Modifier.PRIVATE,
                    Modifier.VOLATILE,
                    Modifier.STATIC
                )
                clsRouterBuilder.addMethod(
                    MethodSpec.methodBuilder("get").returns(interfaceClsName)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addCode(
                            CodeBlock.builder().beginControlFlow("if (instance == null)")
                                .beginControlFlow("synchronized ($clsName.class)")
                                .beginControlFlow("if (instance == null)")
                                .addStatement(
                                    "Object result = \$T.builder(\$S).execute()",
                                    rudolph,
                                    getRoutePath(element, route)
                                )
                                .beginControlFlow("if(result instanceof \$T)", interfaceClsName)
                                .addStatement("instance = (\$T)result", interfaceClsName)
                                .endControlFlow()
                                .endControlFlow().addStatement("return instance").endControlFlow()
                                .endControlFlow().addStatement("return instance").build()
                        ).build()
                )
            } else {
                clsRouterBuilder.addMethod(
                    MethodSpec.methodBuilder("newInstance")
                        .addJavadoc("create new instance\n").returns(interfaceClsName)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addStatement("return (\$T) builder().build().execute()", interfaceClsName).build()
                )
            }
        }


        if (routeType != RouteType.SERVICE || !route.singleton) {
            val routerBuilderClsName = ClassName.get(clsName, "Builder")
            val builderTypeSpec =
                generate(interfaceClsName, routerBuilderClsName, element, routeType)
            clsRouterBuilder.addType(builderTypeSpec)
            //构件构造方法(Context context, Class<?> clazz)
            clsRouterBuilder.addMethod(
                MethodSpec.methodBuilder("builder")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(routerBuilderClsName)
                    .addStatement("return new \$T()", routerBuilderClsName).build()
            )
            //        logger.error("getExportApiPackageName:"+getExportApiPackageName(element));
        }

        try {
            val file = JavaFile.builder(
                getExportApiPackageName(element, export != null),
                clsRouterBuilder.build()
            ).build()
            val outDirectory: File? = outputDirectory
            if (export != null && outputDirectory != null) {
                file.writeTo(outDirectory)
            } else {
                file.writeTo(mFiler)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error(e)
        }
    }

    private fun getInterface(route: Route): TypeMirror? {
        try {
            route.clazz
        } catch (mte: MirroredTypesException) {
            if (mte.typeMirrors.size > 0) return mte.typeMirrors[0]
        }
        return null
    }


//    fun getAnnotationMirror(element: Element, annotationClass: KClass<*>): Optional<out AnnotationMirror?>? {
//        val annotationClassName = annotationClass.qualifiedName
//        return element.annotationMirrors.stream()
//                .filter { m -> m.annotationType.toString() == annotationClassName }
//                .findFirst()
//    }


    private fun getExportApiPackageName(element: TypeElement?, export: Boolean): String? {
        return if (export && buildInfo != null && StringUtils.isNotEmpty(buildInfo?.exportProtocolPackage)) {
            buildInfo?.exportProtocolPackage
        } else {
            val qualifiedName = element!!.qualifiedName.toString()
            qualifiedName.substring(0, qualifiedName.lastIndexOf("."))
        }
    }

    private fun getRoutePath(element: Element?, route: Route): String {
        if (route.urls.isNotEmpty()) {
            return route.urls.first().lowercase(Locale.getDefault())
        }
        if (route.value.isNotBlank()) {
            return route.value.lowercase(Locale.getDefault())
        }
        return "/" + element.toString().lowercase(Locale.getDefault())
    }


    private fun getArgName(element: Element, param: Extra): String {
        return if (StringUtils.isEmpty(param.value)) element.simpleName.toString() else param.value
    }

    /**
     * 生成路由类
     */
    @Throws(IllegalAccessException::class)
    private fun generate(
        interfaceClsName: TypeName?,
        builderType: ClassName,
        element: TypeElement?,
        routeType: RouteType,
    ): TypeSpec {
        val route = element!!.getAnnotation(Route::class.java)
        val builder = TypeSpec.classBuilder(builderType.simpleName())
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)

        //构件构造方法(Context context, Class<?> clazz)
        builder.addMethod(
            MethodSpec.constructorBuilder()
                .addStatement("super(\$S)", getRoutePath(element, route))
                .build()
        )

        //构件输入参数
        when (routeType) {
            RouteType.ACTIVITY -> {
                builder.superclass(
                    ParameterizedTypeName.get(
                        ClassName.get(activityIntentBuilderTm),
                        builderType
                    )
                )
            }
            RouteType.FRAGMENT -> {
                builder.superclass(
                    ParameterizedTypeName.get(
                        ClassName.get(fragmentBuilderTm),
                        builderType,
                        TypeName.get(fragmentTm)
                    )
                )
            }
            RouteType.FRAGMENT_V4 -> {
                builder.superclass(
                    ParameterizedTypeName.get(
                        ClassName.get(fragmentBuilderTm),
                        builderType,
                        TypeName.get(fragmentTmV4!!.asType())
                    )
                )
            }
            RouteType.FRAGMENT_ANDROID_X -> {
                builder.superclass(
                    ParameterizedTypeName.get(
                        ClassName.get(fragmentBuilderTm),
                        builderType,
                        TypeName.get(fragmentAndroidX!!.asType())
                    )
                )
            }
            RouteType.SERVICE -> {
                builder.superclass(
                    ParameterizedTypeName.get(
                        ClassName.get(providerRouterBuilderTm),
                        builderType,
                        interfaceClsName
                    )
                )
            }
            else -> {
                throw IllegalAccessException("Unsupported class type: $element")
            }
        }

        //生成参数方法
        for (field in element.enclosedElements) {
            val param = field.getAnnotation(Extra::class.java)
            if (param == null || !param.export) continue
            //            logger.error("field:"+field.getSimpleName().toString());
            val fieldName = field.simpleName.toString()
            val argName = getArgName(field, param)

            //排除内部参数
            if (argName == Consts.RAW_URI) continue
            val typeMirror = field.asType()
            val typeName = ClassName.get(typeMirror)

//            TypeKind typeKind = typeMirror.getKind();
//            logger.error(fieldName + " [Class:" + ClassName.get(typeMirror).toString() + ",Kind:" + typeKind + ",Primitive:" + typeKind.isPrimitive() + "]");
            var methodName = "putExtra"
            if (typeName is ParameterizedTypeName) {
                //                logger.error("rawType:" + parameterizedTypeName.rawType);
                if ("java.util.ArrayList" == typeName.rawType.toString()) {
                    val strTypeArguments = typeName.typeArguments.toString()
                    //                    logger.error("typeArguments:" + parameterizedTypeName.typeArguments.toString());
                    methodName = when (strTypeArguments) {
                        "[java.lang.CharSequence]" -> {
                            "putCharSequenceArrayListExtra"
                        }
                        "[java.lang.String]" -> {
                            "putStringArrayListExtra"
                        }
                        "[java.lang.Integer]" -> {
                            "putIntegerArrayListExtra"
                        }
                        else -> {
                            "putParcelableArrayListExtra"
                        }
                    }
                }
            }
            val extraName = getArgName(field, param)
            val msBuilder = MethodSpec.methodBuilder(fieldName).addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(typeName, "val").build()).returns(builderType)
            var argValName = "val"
            if (param.json) {
                msBuilder.addStatement("String json = new \$T().toJson(val)", Gson)
                argValName = "json"
            }
            if (param.base64) {
                msBuilder.addStatement(
                    "String base64 = new String(\$T.encode($argValName.getBytes(),Base64.NO_PADDING|Base64.URL_SAFE))",
                    Base64
                )
                argValName = "base64"
            }
            msBuilder.addStatement("super.$methodName(\$S,$argValName)", extraName)
            msBuilder.addStatement("return this")
            builder.addMethod(msBuilder.build()).build()
        }
        return builder.build()
    }

    private fun getPackageName(element: TypeElement?): String {
        val qualifiedName = element!!.qualifiedName.toString()
        return qualifiedName.substring(0, qualifiedName.lastIndexOf("."))
    }


    /**
     * 生成路由参数注入类 xxxRouteBinder.java
     * <pre>
     * public class KotlinActivityBinder implements IRouteBinder {
     *      private static final String userId = "userId";
     *
     *      @Override
     *      public void bind(Object target, Bundle args) {
     *          KotlinActivity kotlinActivity = (KotlinActivity)target;
     *          if (args != null) {
     *              if (args.containsKey(cn.wzbos.android.rudolph.Consts.RAW_URI)) {
     *              kotlinActivity.setRouteUri(args.getString(cn.wzbos.android.rudolph.Consts.RAW_URI));
     *          }
     *          if (args.containsKey(userId)) {
     *              kotlinActivity.setUserId(args.getInt(userId));
     *          }
     *          ...
     *      }
     * }
     * </pre>
     *
     */
    @Throws(IOException::class)
    private fun generateRouteBinderCls(element: TypeElement?, target: ClassName) {
        val clsName = target.simpleName() + "Binder"
        logger.info("generate <<< $clsName >>>")
        val clsRouterBuilder = TypeSpec.classBuilder(clsName).addJavadoc(Constant.WARNING_TIPS)
            .addSuperinterface(ClassName.get(type_IBind)).addModifiers(Modifier.PUBLIC)

        //generate bind method
        val bindBuilder = MethodSpec.methodBuilder("bind").addAnnotation(Override::class.java)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(ParameterSpec.builder(TypeName.OBJECT, "target").build())
            .addParameter(ParameterSpec.builder(Bundle, "args").build())

        //generate inject code
        val clsElements = element!!.enclosedElements
        if (clsElements != null && clsElements.size > 0) {
            var targetName: String = target.simpleName()
            targetName =
                targetName.substring(0, 1).lowercase(Locale.getDefault()) + targetName.substring(1)
            bindBuilder.addStatement("\$T $targetName = (\$T)target", target, target)
            val codeBlockBuilder = CodeBlock.builder()
            codeBlockBuilder.beginControlFlow("if (args != null)")
            for (ele in clsElements) {
                if (ele.kind != ElementKind.FIELD) continue
                val field = ele.getAnnotation(Extra::class.java) ?: continue
                val setCodeBlockBuilder = CodeBlock.builder()
                val fieldName = ele.simpleName.toString()
                val argName = if (StringUtils.isEmpty(field.value)) fieldName else field.value

//                logger.warning("fieldName:" + fieldName + ",argName:" + argName);
                val typeMirror = ele.asType()
                val cls = typeMirror.toString()
                var extraName: String
                if (argName != Consts.RAW_URI) {
                    extraName = getArgName(ele, field)
                    //将参数名称生成为静态常量
                    clsRouterBuilder.addField(
                        FieldSpec.builder(
                            String::class.java,
                            extraName,
                            Modifier.PRIVATE,
                            Modifier.FINAL,
                            Modifier.STATIC
                        ).initializer("\$S", argName).build()
                    )
                } else {
                    extraName = Constant.TYPE_RAW_URI
                }
                codeBlockBuilder.beginControlFlow("if (args.containsKey($extraName))")
                var varCode: String
                var args = arrayOf<Any?>()
                if ("java.lang.String" == cls || "java.lang.CharSequence" == cls) {
                    if (field.base64) {
                        varCode =
                            "new String(\$T.decode(args.getString($extraName).getBytes(),Base64.NO_PADDING|Base64.URL_SAFE))"
                        args = arrayOf(Base64)
                    } else {
                        varCode = "args.getString($extraName)"
                    }
                } else if ("java.lang.Boolean" == cls || "boolean" == cls) {
                    varCode = "args.getBoolean($extraName)"
                } else if ("java.lang.Byte" == cls || "byte" == cls) {
                    varCode = "args.getByte($extraName)"
                } else if ("java.lang.Short" == cls || "short" == cls) {
                    varCode = "args.getShort($extraName)"
                } else if ("java.lang.Integer" == cls || "int" == cls) {
                    varCode = "args.getInt($extraName)"
                } else if ("java.lang.Long" == cls || "long" == cls) {
                    varCode = "args.getLong($extraName)"
                } else if ("java.lang.Character" == cls) {
                    varCode = "(Character)args.getSerializable($extraName)"
                } else if ("char" == cls) {
                    varCode = "args.getChar($extraName)"
                } else if ("java.lang.Float" == cls || "float" == cls) {
                    varCode = "args.getFloat($extraName)"
                } else if ("java.lang.Double" == cls || "double" == cls) {
                    varCode = "args.getDouble($extraName)"
                } else if ("android.os.Bundle" == cls) {
                    varCode = "args.getBundle($extraName)"
                } else {
                    if (types?.isSubtype(typeMirror, parcelableTM) == true) {
                        varCode = "args.getParcelable($extraName)"
                    } else {
                        if (field.json) {
                            codeBlockBuilder.addStatement("String val = args.getString($extraName)")
                            if (field.base64) {
                                codeBlockBuilder.addStatement(
                                    "val = new String(\$T.decode(val.getBytes(),Base64.NO_PADDING|Base64.URL_SAFE))",
                                    Base64
                                )
                            }
                            if (cls.contains("<") && cls.contains(">")) {
                                varCode = "new \$T().fromJson(val, new \$T<\$T>(){}.getType())"
                                args = arrayOf(Gson, TypeToken, ele.asType())
                            } else {
                                varCode = "new \$T().fromJson(val,\$T.class)"
                                args = arrayOf(Gson, ele.asType())
                            }
                        } else {
                            if (types?.isSubtype(typeMirror, serializableTM) == true) {
                                varCode = "(\$T)args.getSerializable($extraName)"
                                args = arrayOf(ele.asType())
                            } else {
                                logger.error("arg:${element.simpleName}.$argName no support!")
                                continue
                            }
                        }
                    }
                }
                if (isKotlin(element)) {
                    setCodeBlockBuilder.addStatement(
                        "\t$targetName.set${
                            fieldName.substring(0, 1).toUpperCase(Locale.getDefault())
                        }${fieldName.substring(1)}($varCode)", *args
                    )
                } else {
                    setCodeBlockBuilder.addStatement("\t$targetName.$fieldName = $varCode", *args)
                }
                setCodeBlockBuilder.endControlFlow()
                codeBlockBuilder.add(setCodeBlockBuilder.build())
            }
            codeBlockBuilder.endControlFlow()
            bindBuilder.addCode(codeBlockBuilder.build())
        }
        clsRouterBuilder.addMethod(bindBuilder.build())
        JavaFile.builder(getPackageName(element), clsRouterBuilder.build()).build().writeTo(mFiler)
    }


    /**
     * 判断当前工程是否为kotlin工程
     *
     * @param element TypeElement
     * @return true：kotlin 工程，false: Java工程
     */
    private fun isKotlin(element: TypeElement?): Boolean {
        var ret = false
        try {
            val cls = Class.forName("kotlin.Metadata")
            val annotation: Any? = element!!.getAnnotation(cls as Class<Annotation?>)
            if (null != annotation) {
                ret = true
            }
        } catch (ignored: ClassNotFoundException) {
        }
        return ret
    }
}