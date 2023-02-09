package cn.wzbos.android.rudolph

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.wzbos.android.rudolph.logger.RLog
import cn.wzbos.android.rudolph.router.UriRouter
import cn.wzbos.android.rudolph.utils.match
import java.util.*


/**
 * Rudolph
 * Created by wuzongbo on 17/4/10.
 */
object Rudolph {
    private const val TAG = "Rudolph"

    var globalInterceptors: MutableList<Interceptor>? = null
        private set

    private val routes: MutableList<RouteInfo> = ArrayList()

    @JvmStatic
    var logger: ILogger? = null

    var context: Application? = null
        private set

    /**
     * scheme
     */
    @JvmStatic
    var scheme: String? = null

    /**
     * host
     */
    @JvmStatic
    var host: String? = null

    /**
     * 获取是否已初始化路由表
     */
    var isInitialized = false
        private set

    /**
     * Rudolph 初始化,建议方法application中
     *
     * @param context Context
     */
    @JvmStatic
    fun init(context: Application) {
        init(context, null, null)
    }

    /**
     * Rudolph 初始化,建议方法application中
     *
     * @param context Context
     * @param scheme scheme
     * @param host host
     */
    @JvmStatic
    @JvmOverloads
    fun init(
        context: Context,
        scheme: String? = null,
        host: String? = null,
    ) {
        RLog.v(TAG, "init")
        if (this.isInitialized) return
        this.scheme = scheme
        this.host = host
        this.context = context.applicationContext as Application
        val assetManager = context.resources.assets
        try {
            val list = assetManager.list("rudolph")
            if (null != list && list.isNotEmpty()) {
                val tables: MutableList<IRouteTable> = ArrayList()
                for (className in list) {
                    try {
                        RLog.i(TAG, "init Router: $className")
                        val clazz = Class.forName("cn.wzbos.android.rudolph.routes.$className")
                        if (IRouteTable::class.java.isAssignableFrom(clazz)) {
                            val iRouteTable = clazz.newInstance() as IRouteTable
                            iRouteTable.register()
                            tables.add(iRouteTable)
                        }
                    } catch (e: Exception) {
                        RLog.e(TAG, "初始化\"$className\"组件失败，请检查包名是否正确！", e)
                    }
                }

                //待所有组件注册完成后再进行初始化，防止组件之间相互访问
                for (iRouteTable in tables) {
                    iRouteTable.init(context)
                }
            }
            this.isInitialized = true
        } catch (e: Exception) {
            RLog.e(TAG, "路由初始化异常！", e)
        }
    }

    @JvmStatic
    fun addRoute(routeInfo: RouteInfo) {
        routes.add(routeInfo)
    }


    /**
     * 获取全局拦截器
     *
     * @return Interceptor集合
     */
    @JvmStatic
    @Deprecated(
        "已过期请使用 globalInterceptors",
        replaceWith = ReplaceWith("globalInterceptors")
    )
    val interceptors: MutableList<Interceptor>? = globalInterceptors

    /**
     * 添加全局拦截器
     *
     * @param interceptor 拦截器
     */
    @JvmStatic
    @Deprecated(
        "此方法已过期，请使用 registerGlobalInterceptor",
        replaceWith = ReplaceWith("registerGlobalInterceptor(interceptor)")
    )
    fun addInterceptor(interceptor: Interceptor) {
        registerGlobalInterceptor(interceptor)
    }


    /**
     * 添加多个全局拦截器
     *
     * @param interceptors 拦截器集合
     */
    @JvmStatic
    @Deprecated(
        "此方法已过期，请使用 registerGlobalInterceptor",
        replaceWith = ReplaceWith("registerGlobalInterceptor(interceptors)")
    )
    fun addInterceptor(interceptors: MutableList<Interceptor>) {
        interceptors.forEach {
            registerGlobalInterceptor(it)
        }
    }

    /**
     * 移除拦截器
     *
     * @param interceptor 通过 addInterceptor 添加的拦截器
     */
    @JvmStatic
    @Deprecated(
        "此方法已过期，请使用 unregisterGlobalInterceptor",
        replaceWith = ReplaceWith("unregisterGlobalInterceptor(interceptor)")
    )
    fun removeInterceptor(interceptor: Interceptor) {
        unregisterGlobalInterceptor(interceptor)
    }


    /**
     * 注册全局拦截器
     *
     * @param interceptor 拦截器
     */
    @JvmStatic
    fun registerGlobalInterceptor(interceptor: Interceptor) {
        if (null == globalInterceptors) {
            globalInterceptors = mutableListOf()
        }
        globalInterceptors?.add(interceptor)
    }

    /**
     * 取消注册全局拦截器
     *
     * @param interceptor 通过 addInterceptor 添加的拦截器
     */
    @JvmStatic
    fun unregisterGlobalInterceptor(interceptor: Interceptor) {
        globalInterceptors?.remove(interceptor)
    }


    /**
     * 获取已加载所有路由信息
     *
     * @return RouteInfo 集合
     */
    @JvmStatic
    val routers: List<RouteInfo>
        get() = routes

    /**
     * 根据路由地址获取一个路由信息
     *
     * @param path 路由地址（如：/user/info 或 demo://my.app.com/user/info）
     * @return RouteInfo
     */
    @JvmStatic
    fun getRouter(url: String?): RouteInfo? {
        routes.forEach { info ->
            run {
                if (info.match(url)) {
                    return info
                }
            }
        }
        return null
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param activity Activity
     */
    @JvmStatic
    fun bind(activity: Activity) {
        RouteBinder.instance?.bind(activity, activity.intent.extras)
    }

    /**
     * 将 @Extra 注解的字段重新赋值（对应Activity的onNewIntent生命周期）
     *
     * @param activity Activity
     * @param intent   Intent
     */
    @JvmStatic
    fun onNewIntent(activity: Activity, intent: Intent) {
        RouteBinder.instance?.bind(activity, intent.extras)
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param fragment Fragment
     */
    @JvmStatic
    fun bind(fragment: Fragment) {
        RouteBinder.instance?.bind(fragment, fragment.arguments)
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param service IRouteService
     * @param bundle  Bundle
     */
    @JvmStatic
    fun bind(service: IRouteService, bundle: Bundle?) {
        RouteBinder.instance?.bind(service, bundle)
    }

    /**
     * 通过路由地址获取一个路由构造器
     *
     * @param url 路由地址
     * @return UriRouter.Builder
     */
    @JvmStatic
    fun builder(url: Uri): UriRouter.Builder<*> {
        return builder(url.toString())
    }

    /**
     * 通过路由地址获取一个路由构造器
     *
     * @param url 路由地址
     * @return UriRouter.Builder
     */
    @JvmStatic
    fun builder(url: String?): UriRouter.Builder<*> {
        return UriRouter.Builder<Nothing>(url!!)
    }

}





























