
package cn.wzbos.android.rudolph;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import cn.wzbos.android.rudolph.annotations.Arg;
import cn.wzbos.android.rudolph.annotations.Component;
import cn.wzbos.android.rudolph.annotations.Exclude;
import cn.wzbos.android.rudolph.annotations.Export;
import cn.wzbos.android.rudolph.annotations.Route;

import static cn.wzbos.android.rudolph.Consts.RAW_URI;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.element.Modifier.VOLATILE;

/**
 * Router Processor
 * Created by wuzongbo on 2017/5/30.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({Constant.ANNOTATION_TYPE_ROUTE, Constant.ANNOTATION_TYPE_COMPONENT})
public class RudolphProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Logger logger;
    private Types types;
    private Elements elements;
    private static final ClassName rudolph = ClassName.get(Constant.PACKAGE, "Rudolph");
    private static final ClassName Gson = ClassName.get("com.google.gson", "Gson");
    private static final ClassName Base64 = ClassName.get("android.util", "Base64");
    private static final ClassName TypeToken = ClassName.get("com.google.gson.reflect", "TypeToken");
    private static final ClassName clsApplication = ClassName.get("android.app", "Application");
    private static final ClassName Bundle = ClassName.get("android.os", "Bundle");


    private TypeElement activityIntentBuilderTm;
    private TypeElement fragmentBuilderTm;
    private TypeElement providerRouterBuilderTm;
    private TypeElement type_IBind;

    private TypeMirror fragmentTm;
    private TypeElement fragmentTmV4 = null;
    private TypeElement fragmentAndroidX = null;
    private TypeMirror activityTm;
    private TypeMirror serviceTm;
    private TypeMirror parcelableTM;
    private TypeMirror serializableTM;
    private RudolphBuildInfo buildInfo;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();


        types = processingEnv.getTypeUtils();
        logger = new Logger(processingEnv.getMessager());
        elements = processingEnv.getElementUtils();

        activityIntentBuilderTm = elements.getTypeElement(Constant.ACTIVITY_INTENT_BUILDER);
        fragmentBuilderTm = elements.getTypeElement(Constant.FRAGMENT_INTENT_BUILDER);
        providerRouterBuilderTm = elements.getTypeElement(Constant.PROVIDER_ROUTER_BUILDER);
        type_IBind = elements.getTypeElement(Constant.ROUTER_BINDER);


        parcelableTM = this.elements.getTypeElement("android.os.Parcelable").asType();
        serializableTM = this.elements.getTypeElement("java.io.Serializable").asType();
        activityTm = elements.getTypeElement("android.app.Activity").asType();
        fragmentTm = elements.getTypeElement("android.app.Fragment").asType();
        fragmentTmV4 = elements.getTypeElement("android.support.v4.app.Fragment");
        fragmentAndroidX = elements.getTypeElement(" androidx.fragment.app.Fragment");
        serviceTm = elements.getTypeElement(Constant.ROUTE_SERVICE).asType();


        buildInfo = RudolphBuildInfo.load(processingEnv);

        if (buildInfo.exportApi()) {
            AndroidModuleUtil.createApiModule(buildInfo);
        }
    }


    private String getRouteClsName() {
        String name = buildInfo.moduleName.replaceAll("[^0-9a-zA-Z_]+", "");
        return name.substring(0, 1).toUpperCase() + name.substring(1) + "Routes";
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (CollectionUtils.isNotEmpty(annotations)) {
            try {
                //构件路由表类的初始化方法 init(Application application)
                MethodSpec.Builder builder = MethodSpec.methodBuilder("init")
                        .addAnnotation(Override.class)
                        .addModifiers(PUBLIC)
                        .addParameter(ParameterSpec.builder(clsApplication, "application").build());

                Set<? extends Element> components = roundEnvironment.getElementsAnnotatedWith(Component.class);
                if (CollectionUtils.isNotEmpty(components)) {
                    for (Element element : components) {
                        builder.addStatement("new $T().init(application)", element);
                    }
                }

                Set<? extends Element> routers = roundEnvironment.getElementsAnnotatedWith(Route.class);

                if (CollectionUtils.isNotEmpty(routers)) {
                    RouteType routetype;
                    for (Element element : routers) {
                        Route route = element.getAnnotation(Route.class);

                        ElementKind kind = element.getKind();
                        ClassName target;
                        if (kind == ElementKind.CLASS) {
                            TypeMirror tm = element.asType();
                            TypeElement typeElement = (TypeElement) element;
                            target = ClassName.get(typeElement);
                            if (types.isSubtype(tm, activityTm)) {
                                // Activity
                                routetype = RouteType.ACTIVITY;
                            } else if (types.isSubtype(tm, fragmentTm)) {
                                // Fragment
                                routetype = RouteType.FRAGMENT;
                            } else if (fragmentTmV4 != null && types.isSubtype(tm, fragmentTmV4.asType())) {
                                // Fragment V4
                                routetype = RouteType.FRAGMENT_V4;
                            } else if (fragmentAndroidX != null && types.isSubtype(tm, fragmentAndroidX.asType())) {
                                // Fragment AndroidX
                                routetype = RouteType.FRAGMENT_AndroidX;
                            } else if (types.isSubtype(tm, serviceTm)) {
                                routetype = RouteType.SERVICE;
                            } else {
                                routetype = RouteType.UNKNOWN;
                            }
                            generateRouterCls(typeElement, routetype, route);
                            generateRouteBinderCls(typeElement, target);
                        } else if (kind == ElementKind.METHOD) {
                            ExecutableElement executableElement = (ExecutableElement) element;
                            routetype = RouteType.METHOD;
                            target = ClassName.get(((TypeElement) executableElement.getEnclosingElement()));
                        } else {
                            logger.error("UnKnown route type:" + kind);
                            continue;
                        }

                        generateRouteTable(builder, element, target, route, routetype);

                    }
                }


                TypeElement superInterfaceType = this.elements.getTypeElement(Constant.ROUTE_TABLE);
                String clsName = getRouteClsName();
                JavaFile.builder(Constant.PACKAGE_NAME,
                        TypeSpec.classBuilder(clsName)
                                .addJavadoc(Constant.WARNING_TIPS)
                                .addSuperinterface(ClassName.get(superInterfaceType))
                                .addModifiers(PUBLIC)
                                .addMethod(builder.build())
                                .build()
                ).build().writeTo(mFiler);

                writeClsNameToAssets(clsName);

                logger.info(">>> Generated " + clsName + " <<<");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e);
            }
            return true;
        }

        return false;
    }


    /**
     * 以文件名方式记录路由表名（assets/rudolph/xxxRoutes）
     * <p>
     * 生成路径：build/tmp/kapt3/classes/debug/assets/rudolph
     * </p>
     * <ul>
     * <li>将路由表的类名以文件名方式创建一个空文件存于assets目录用于路由表初始化加载；</li>
     * <li>此方式可以避免使用代码插装方式初始化路由表,相比更简单，更有效。</li>
     * </ul>
     */
    private void writeClsNameToAssets(String filename) {
        Writer writer = null;
        try {
            String path = "rudolph/" + filename;
            FileObject fileObject = mFiler.createResource(StandardLocation.CLASS_OUTPUT, "assets", path);

            writer = fileObject.openWriter();

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取协议层工程代码目录
     *
     * @return File
     */
    private File getOutputDirectory() {
        if (buildInfo != null && StringUtils.isNotEmpty(buildInfo.export_api_name)) {
            return new File(buildInfo.projectPath + "/" + buildInfo.export_api_name + "/src/main/java");
        } else {
            return null;
        }
    }


    /**
     * 生成路由表类
     * <pre>
     * public class SampleARoutes implements IRouteTable {
     *   {@literal @}Override
     *   public void init(Application application) {
     *     new TestComponent().init(application);
     *
     *     Rudolph.addRoute(new RouteInfo.Builder().routeType(RouteType.ACTIVITY)
     *     .destination(KotlinActivity.class)
     *     .path("/kotlin/test")
     *     .tag("")
     *     .putParam("d3V6b25nYm8",String.class)
     *     .putParam("userId",int.class)
     *     .putParam("userName",String.class).build());
     *   }
     * }
     * </pre>
     */
    private void generateRouteTable(MethodSpec.Builder builder, Element element, ClassName target, Route route, RouteType routetype) {

//        logger.error(">>>>>>> " + element.getSimpleName() + " <<<<<<<<<<<");
        List<Element> arrays = new ArrayList<>();
        if (element.getKind() == ElementKind.CLASS) {
            builder.addCode(
                    "\n\n$T.addRoute(new $T().routeType($T." + routetype + ")\n.destination($T.class)\n.path($S)\n.tag($S)",
                    rudolph,
                    ClassName.get(RouteInfo.Builder.class), //RouteInfo.Builder
                    ClassName.get(RouteType.class),        //RouteType
                    target,
                    getRoutePath(element, route), route.tag());
            arrays.addAll(element.getEnclosedElements());
        } else {
            ExecutableElement method = (ExecutableElement) element;
            builder.addCode(
                    "\n\n$T.addRoute(new $T().routeType($T." + routetype + ")\n.destination($T.class)\n.path($S)\n.tag($S)",
                    rudolph,
                    ClassName.get(RouteInfo.Builder.class),//RouteInfo.Builder
                    ClassName.get(RouteType.class),//RouteType
                    target,
                    getRoutePath(element, route), route.tag());

            arrays.addAll(method.getParameters());
        }

        for (Element field : arrays) {
            Arg param = field.getAnnotation(Arg.class);
            if (param == null)
                continue;

            String argName = getArgName(field, param);
            String clsName = ClassName.get(field.asType()).toString();
            if (clsName.contains("<") && clsName.contains(">")) {
                builder.addCode("\n.putParam($S,new $T<$T>(){}.getType())",
                        argName,
                        TypeToken,
                        ClassName.get(field.asType()));
            } else {
                builder.addCode("\n.putParam($S,$T.class)",
                        argName,
                        ClassName.get(field.asType()));
            }
        }
        builder.addCode(".build());");
    }

    /**
     * 生成Router类 xxxRouter.java
     * <pre>
     * public class MainActivityRouter {
     *   public static MainActivityRouter.Builder builder() {
     *     return new MainActivityRouter.Builder();
     *   }
     *
     *   public static class Builder extends ActivityRouter.Builder<MainActivityRouter.Builder> {
     *     Builder() {
     *       super("/cn.wzbos.samplea.mainactivity");
     *     }
     *
     *     public MainActivityRouter.Builder index(int val) {
     *       super.arg("index",val);
     *       return this;
     *     }
     *
     *     public MainActivityRouter.Builder name(String val) {
     *       super.arg("name",val);
     *       return this;
     *     }
     *   }
     * }
     * </pre>
     */
    private void generateRouterCls(TypeElement element, RouteType routeType, Route route) throws IllegalAccessException {
        logger.warning("generateRouterCls:" + element.getSimpleName() + "Router");
        Export export = element.getAnnotation(Export.class);

        TypeName interfaceClsName = null;

        TypeMirror mirror = APUtils.getTypeMirrorFromAnnotationValue(route::clazz);
        if (mirror != null && ClassName.get(mirror) != TypeName.get(Object.class)) {
            interfaceClsName = ClassName.get(mirror);
        }

        String clsName;
        if (export == null || "".equals(export.value())) {
            clsName = element.getSimpleName() + "Router";
        } else {
            clsName = export.value();
        }

        TypeSpec.Builder clsRouterBuilder = TypeSpec.classBuilder(clsName)
                //增加注释
                .addJavadoc(Constant.WARNING_TIPS)
                .addModifiers(PUBLIC);

        //生成单例
        if (routeType == RouteType.SERVICE) {
            if (interfaceClsName == null) {
                logger.error("@Route clazz:" + route.clazz().getName() + " 不存在！");
                return;
            }
            if (route.singleton()) {
                clsRouterBuilder.addField(interfaceClsName, "instance", PRIVATE, VOLATILE, STATIC);
                clsRouterBuilder.addMethod(MethodSpec.methodBuilder("get")
                        .returns(interfaceClsName)
                        .addModifiers(PUBLIC, STATIC)
                        .addCode(CodeBlock.builder()
                                .beginControlFlow("if (instance == null)")
                                .beginControlFlow("synchronized (" + clsName + ".class)")
                                .beginControlFlow("if (instance == null)")
                                .addStatement("instance = builder().build().open()")
                                .endControlFlow()
                                .addStatement("return instance")
                                .endControlFlow()
                                .endControlFlow()
                                .addStatement("return instance")
                                .build())
                        .build());
            }
        }

        ClassName routerBuilderClsName = ClassName.get(clsName, "Builder");
        TypeSpec builderTypeSpec = generate(interfaceClsName, routerBuilderClsName, element, routeType);
        clsRouterBuilder.addType(builderTypeSpec);
        //构件构造方法(Context context, Class<?> clazz)
        clsRouterBuilder.addMethod(MethodSpec.methodBuilder("builder")
                .addModifiers(PUBLIC, STATIC)
                .returns(routerBuilderClsName)
                .addStatement("return new $T()", routerBuilderClsName)
                .build());
//        logger.error("getExportApiPackageName:"+getExportApiPackageName(element));

        try {
            JavaFile file = JavaFile.builder(getExportApiPackageName(element, export != null), clsRouterBuilder.build())
                    .build();
            File out_directory;
            if (export != null && (out_directory = getOutputDirectory()) != null) {
//                logger.info(out_directory.getAbsolutePath() + "/" + clsName+".java");
                file.writeTo(out_directory);
            } else {
                file.writeTo(mFiler);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }

    private String getExportApiPackageName(TypeElement element, boolean export) {
        if (export && buildInfo != null && StringUtils.isNotEmpty(buildInfo.export_api_package)) {
            return buildInfo.export_api_package;
        } else {
            String qualifiedName = element.getQualifiedName().toString();
            return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
        }
    }

    private String getRoutePath(Element element, Route route) {
        return StringUtils.isEmpty(route.value()) ? "/" + element.toString().toLowerCase() : route.value().toLowerCase();
    }

    private String getArgName(Element element, Arg param) {
        return StringUtils.isEmpty(param.value()) ? element.getSimpleName().toString() : param.value();
    }

    /**
     * 生成路由类
     */
    private TypeSpec generate(TypeName interfaceClsName, ClassName builderType, TypeElement element, RouteType routeType) throws IllegalAccessException {

        Route route = element.getAnnotation(Route.class);

        TypeSpec.Builder builder = TypeSpec.classBuilder((builderType).simpleName())
                .addModifiers(PUBLIC, STATIC);

        //构件构造方法(Context context, Class<?> clazz)
        builder.addMethod(MethodSpec.constructorBuilder()
                .addStatement("super($S)", getRoutePath(element, route))
                .build());

        //构件输入参数
        if (routeType == RouteType.ACTIVITY) {
            // Activity
            builder.superclass(ParameterizedTypeName.get(
                    ClassName.get(activityIntentBuilderTm),
                    builderType));
        } else if (routeType == RouteType.FRAGMENT) {
            // Fragment
            builder.superclass(ParameterizedTypeName.get(
                    ClassName.get(fragmentBuilderTm),
                    builderType,
                    TypeName.get(fragmentTm)));
        } else if (routeType == RouteType.FRAGMENT_V4) {
            // Fragment V4
            builder.superclass(ParameterizedTypeName.get(
                    ClassName.get(fragmentBuilderTm),
                    builderType,
                    TypeName.get(fragmentTmV4.asType())));
        } else if (routeType == RouteType.FRAGMENT_AndroidX) {
            // Fragment AndroidX
            builder.superclass(ParameterizedTypeName.get(
                    ClassName.get(fragmentBuilderTm),
                    builderType,
                    TypeName.get(fragmentAndroidX.asType())));
        } else if (routeType == RouteType.SERVICE) {
            // Provider
            builder.superclass(ParameterizedTypeName.get(
                    ClassName.get(providerRouterBuilderTm),
                    builderType,
                    interfaceClsName));
        } else {
            throw new IllegalAccessException("Unsupported class type: " + element);
        }

        //生成参数方法
        for (Element field : element.getEnclosedElements()) {

            Arg param = field.getAnnotation(Arg.class);
            if (param == null || !param.export())
                continue;
//            logger.error("field:"+field.getSimpleName().toString());


            String fieldName = field.getSimpleName().toString();
            String argName = getArgName(field, param);

            //排除内部参数
            if (argName.equals(RAW_URI))
                continue;

            TypeMirror typeMirror = field.asType();
            TypeName typeName = ClassName.get(typeMirror);

//            TypeKind typeKind = typeMirror.getKind();
//            logger.error(fieldName + " [Class:" + ClassName.get(typeMirror).toString() + ",Kind:" + typeKind + ",Primitive:" + typeKind.isPrimitive() + "]");

            String methodName = "arg";
            if (typeName instanceof ParameterizedTypeName) {
                ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
//                logger.error("rawType:" + parameterizedTypeName.rawType);
                if ("java.util.ArrayList".equals(parameterizedTypeName.rawType.toString())) {
                    String strTypeArguments = parameterizedTypeName.typeArguments.toString();
//                    logger.error("typeArguments:" + parameterizedTypeName.typeArguments.toString());

                    if ("[java.lang.CharSequence]".equals(strTypeArguments)) {
                        methodName = "charSequenceArrayListArg";
                    } else if ("[java.lang.String]".equals(strTypeArguments)) {
                        methodName = "stringArrayListArg";
                    } else if ("[java.lang.Integer]".equals(strTypeArguments)) {
                        methodName = "integerArrayListArg";
                    } else {
                        methodName = "parcelableArrayListArg";
                    }
                }
            }

            String extraName = getArgName(field, param);

            MethodSpec.Builder msBuilder = MethodSpec.methodBuilder(fieldName)
                    .addModifiers(PUBLIC)
                    .addParameter(ParameterSpec.builder(typeName, "val").build())
                    .returns(builderType);

            String argValName = "val";

            if (param.json()) {
                msBuilder.addStatement("String json = new $T().toJson(val)", Gson);
                argValName = "json";
            }

            if (param.base64()) {
                msBuilder.addStatement("String base64 = new String($T.encode(" + argValName + ".getBytes(),Base64.NO_PADDING|Base64.URL_SAFE))", Base64);
                argValName = "base64";
            }

            msBuilder.addStatement("super." + methodName + "($S," + argValName + ")", extraName);
            msBuilder.addStatement("return this");

            builder.addMethod(msBuilder.build()).build();
        }

        return builder.build();
    }

    String getPackageName(TypeElement element) {
        String qualifiedName = element.getQualifiedName().toString();
        return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
    }

    /**
     * 生成路由服务类的接口
     */
    private ClassName generateServiceInterface(TypeElement element, boolean export) {

        String interfaceName = "I" + element.getSimpleName();


        TypeSpec.Builder clsRouterBuilder = TypeSpec.interfaceBuilder(interfaceName)
                //增加注释
                .addJavadoc(Constant.WARNING_TIPS)
                .addSuperinterface(ClassName.get(serviceTm))
                .addModifiers(PUBLIC);

        List<? extends Element> elements = element.getEnclosedElements();

        for (Element e : elements) {
            if (e.getModifiers().contains(Modifier.PUBLIC)) {
                if (e.getAnnotation(Exclude.class) == null) {
                    if (e instanceof ExecutableElement) {
                        ExecutableElement executableElement = (ExecutableElement) e;
                        if (executableElement.getKind() != ElementKind.CONSTRUCTOR) {
                            MethodSpec.Builder builder = MethodSpec.methodBuilder(executableElement.getSimpleName().toString());
                            builder.addModifiers(PUBLIC, ABSTRACT);
                            if (executableElement.getReturnType().getKind() != TypeKind.VOID) {
                                builder.returns(ClassName.get(executableElement.getReturnType()));
                            }
                            for (VariableElement variableElement : executableElement.getParameters()) {
                                builder.addParameter(ClassName.get(variableElement.asType()), variableElement.getSimpleName().toString());
                            }
                            clsRouterBuilder.addMethod(builder.build());
                        }
                    }
                }
            }
        }

        try {

            ClassName className = ClassName.get(getExportApiPackageName(element, export), interfaceName);
            JavaFile file = JavaFile.builder(getExportApiPackageName(element, export), clsRouterBuilder.build())
                    .build();
            File out_directory;
            if (export && (out_directory = getOutputDirectory()) != null) {
                logger.warning("output:" + out_directory.getAbsolutePath());
                file.writeTo(out_directory);
            } else {
                file.writeTo(mFiler);
            }
            return className;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return null;
    }

//    /**
//     * generate extend class from service
//     */
//    private ClassName generateServiceEx(TypeElement element, ClassName interfaceTypeName) throws IOException {
//        String clsName = element.getSimpleName() + "Provider";
//        TypeSpec.Builder clsRouterBuilder = TypeSpec.classBuilder(clsName)
//                //增加注释
//                .addJavadoc(Constant.WARNING_TIPS)
//                .superclass(ClassName.get(element))
//                .addSuperinterface(interfaceTypeName)
//                .addModifiers(PUBLIC);
//
//        String pkgName = getPackageName(element);
//        JavaFile.builder(pkgName, clsRouterBuilder.build())
//                .build().writeTo(mFiler);
//
//        return ClassName.get(pkgName, clsName);
//    }


    /**
     * 生成路由参数注入类 xxxRouteBinder.java
     * <pre>
     *   public class KotlinActivityBinder implements IRouteBinder {
     *   private static final String userId = "userId";
     *
     *   {@literal @}Override
     *   public void bind(Object target, Bundle args) {
     *     KotlinActivity kotlinActivity = (KotlinActivity)target;
     *     if (args != null) {
     *       if (args.containsKey(cn.wzbos.android.rudolph.Consts.RAW_URI)) {
     *         	kotlinActivity.setRouteUri(args.getString(cn.wzbos.android.rudolph.Consts.RAW_URI));
     *       }
     *       if (args.containsKey(userId)) {
     *         	kotlinActivity.setUserId(args.getInt(userId));
     *       }
     *       ...
     *     }
     *   }
     * }
     * </pre>
     */
    private void generateRouteBinderCls(TypeElement element, ClassName target) throws IOException {

        String clsName = target.simpleName() + "Binder";
        logger.info("generate <<< " + clsName + " >>>");
        TypeSpec.Builder clsRouterBuilder = TypeSpec.classBuilder(clsName)
                .addJavadoc(Constant.WARNING_TIPS)
                .addSuperinterface(ClassName.get(type_IBind))
                .addModifiers(PUBLIC);

        //generate bind method
        MethodSpec.Builder bindBuilder = MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC)
                .addParameter(ParameterSpec.builder(TypeName.OBJECT, "target").build())
                .addParameter(ParameterSpec.builder(Bundle, "args").build());

        //generate inject code
        List<? extends Element> clsElements = element.getEnclosedElements();
        if (clsElements != null && clsElements.size() > 0) {

            String var = target.simpleName();
            var = var.substring(0, 1).toLowerCase() + var.substring(1);

            bindBuilder.addStatement("$T " + var + " = ($T)target", target, target);
            CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
            codeBlockBuilder.beginControlFlow("if (args != null)");

            for (Element ele : clsElements) {
                if (ele.getKind() != ElementKind.FIELD)
                    continue;

                Arg field = ele.getAnnotation(Arg.class);
                if (field == null)
                    continue;

                CodeBlock.Builder setCodeBlockBuilder = CodeBlock.builder();
                String fieldName = ele.getSimpleName().toString();
                String argName = StringUtils.isEmpty(field.value()) ? fieldName : field.value();

//                logger.warning("fieldName:" + fieldName + ",argName:" + argName);

                TypeMirror typeMirror = ele.asType();
                String cls = typeMirror.toString();

                String extraName;
                if (!argName.equals(RAW_URI)) {
                    extraName = getArgName(ele, field);
                    //将参数名称生成为静态常量
                    clsRouterBuilder.addField(FieldSpec.builder(String.class, extraName, PRIVATE, FINAL, STATIC).initializer("$S", argName).build());
                } else {
                    extraName = Constant.TYPE_RAW_URI;
                }

                codeBlockBuilder.beginControlFlow("if (args.containsKey(" + extraName + "))");

                String varCode;
                Object[] args = new Object[]{};

                if ("java.lang.String".equals(cls) || "java.lang.CharSequence".equals(cls)) {
                    if (field.base64()) {
                        varCode = "new String($T.decode(args.getString(" + extraName + ").getBytes(),Base64.NO_PADDING|Base64.URL_SAFE))";
                        args = new Object[]{Base64};
                    } else {
                        varCode = "args.getString(" + extraName + ")";
                    }
                } else if ("java.lang.Boolean".equals(cls) || "boolean".equals(cls)) {
                    varCode = "args.getBoolean(" + extraName + ")";
                } else if ("java.lang.Byte".equals(cls) || "byte".equals(cls)) {
                    varCode = "args.getByte(" + extraName + ")";
                } else if ("java.lang.Short".equals(cls) || "short".equals(cls)) {
                    varCode = "args.getShort(" + extraName + ")";
                } else if ("java.lang.Integer".equals(cls) || "int".equals(cls)) {
                    varCode = "args.getInt(" + extraName + ")";
                } else if ("java.lang.Long".equals(cls) || "long".equals(cls)) {
                    varCode = "args.getLong(" + extraName + ")";
                } else if ("java.lang.Character".equals(cls)) {
                    varCode = "(Character)args.getSerializable(" + extraName + ")";
                } else if ("char".equals(cls)) {
                    varCode = "args.getChar(" + extraName + ")";
                } else if ("java.lang.Float".equals(cls) || "float".equals(cls)) {
                    varCode = "args.getFloat(" + extraName + ")";
                } else if ("java.lang.Double".equals(cls) || "double".equals(cls)) {
                    varCode = "args.getDouble(" + extraName + ")";
                } else {
                    if (types.isSubtype(typeMirror, parcelableTM)) {
                        varCode = "args.getParcelable(" + extraName + ")";
                    } else {

                        if (field.json()) {
                            codeBlockBuilder.addStatement("String val = args.getString(" + extraName + ")");

                            if (field.base64()) {
                                codeBlockBuilder.addStatement("val = new String($T.decode(val.getBytes(),Base64.NO_PADDING|Base64.URL_SAFE))", Base64);
                            }

                            if (cls.contains("<") && cls.contains(">")) {
                                varCode = "new $T().fromJson(val, new $T<$T>(){}.getType())";
                                args = new Object[]{Gson, TypeToken, ele.asType()};
                            } else {
                                varCode = "new $T().fromJson(val,$T.class)";
                                args = new Object[]{Gson, ele.asType()};
                            }
                        } else {
                            if (types.isSubtype(typeMirror, serializableTM)) {
                                varCode = "($T)args.getSerializable(" + extraName + ")";
                                args = new Object[]{ele.asType()};
                            } else {
                                logger.error("arg:" + element.getSimpleName() + "." + argName + " no support!");
                                continue;
                            }
                        }
                    }
                }


                if (isKotlin(element)) {
                    setCodeBlockBuilder.addStatement("\t" + var + ".set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) + "(" + varCode + ")", args);
                } else {
                    setCodeBlockBuilder.addStatement("\t" + var + "." + fieldName + " = " + varCode, args);
                }

                setCodeBlockBuilder.endControlFlow();
                codeBlockBuilder.add(setCodeBlockBuilder.build());
            }

            codeBlockBuilder.endControlFlow();
            bindBuilder.addCode(codeBlockBuilder.build());
        }

        clsRouterBuilder.addMethod(bindBuilder.build());

        JavaFile.builder(getPackageName(element), clsRouterBuilder.build())
                .build()
                .writeTo(mFiler);
    }


    /**
     * 判断当前工程是否为kotlin工程
     *
     * @param element TypeElement
     * @return true：kotlin 工程，false: Java工程
     */
    @SuppressWarnings("unchecked")
    private boolean isKotlin(TypeElement element) {
        boolean ret = false;
        try {
            Class<?> cls = Class.forName("kotlin.Metadata");
            if (null != cls) {
                Object annotation = element.getAnnotation((Class<Annotation>) cls);
                if (null != annotation) {
                    ret = true;
                }
            }
        } catch (ClassNotFoundException ignored) {
        }

        return ret;
    }

}
