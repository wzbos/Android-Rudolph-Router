package cn.wzbos.android.rudolph

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import cn.wzbos.android.rudolph.router.UriRouter
import java.util.*


/**
 * Rudolph
 * Created by wuzongbo on 17/4/10.
 */
object Rudolph {
    private const val TAG = "Rudolph"
    private var mInterceptors: MutableList<Interceptor>? = null
    private val routes: MutableList<RouteInfo> = ArrayList()

    @JvmStatic
    var logger: ILogger? = null

    var application: Application? = null
        private set

    /**
     * scheme
     */
    @JvmStatic
    var scheme: String? = null

    /**
     * 获取是否已初始化路由表
     */
    var isInitialized = false
        private set

    /**
     * Rudolph 初始化,建议方法application中
     *
     * @param application Application
     */
    @JvmStatic
    fun init(application: Application) {
        RLog.v(TAG, "init")
        if (this.isInitialized) return
        this.application = application
        val assetManager = application.resources.assets
        try {
            val list = assetManager.list("rudolph")
            if (null != list && list.isNotEmpty()) {
                val tables: MutableList<IRouteTable> = ArrayList()
                for (className in list) {
                    try {
                        val clazz = Class.forName("cn.wzbos.android.rudolph.routes.$className")
                        if (IRouteTable::class.java.isAssignableFrom(clazz)) {
                            val iRouteTable = clazz.newInstance() as IRouteTable
                            iRouteTable.register()
                            tables.add(iRouteTable)
                        }
                    } catch (e: ClassNotFoundException) {
                        RLog.e(TAG, "初始化\"$className\"组件失败，请检查包名是否正确！", e)
                    }
                }

                //待所有组件注册完成后再进行初始化，防止组件之间相互访问
                for (iRouteTable in tables) {
                    iRouteTable.init(application)
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
     * 获取所有拦截器
     *
     * @return Interceptor集合
     */
    @JvmStatic
    val interceptors: List<Interceptor>?
        get() = mInterceptors

    /**
     * 添加拦截器
     *
     * @param interceptor 拦截器
     */
    @JvmStatic
    fun addInterceptor(interceptor: Interceptor) {
        if (null == mInterceptors) {
            mInterceptors = ArrayList()
        }
        mInterceptors!!.add(interceptor)
    }

    /**
     * 移除拦截器
     *
     * @param interceptor 通过 addInterceptor 添加的拦截器
     */
    @JvmStatic
    fun removeInterceptor(interceptor: Interceptor) {
        if (null != mInterceptors) {
            mInterceptors!!.remove(interceptor)
        }
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
    fun getRouter(path: String?): RouteInfo? {
        routes.forEach { info ->
            run {
                if (info.equalPath(path)) {
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
     * @see Extra
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
     * @see Extra
     */
    @JvmStatic
    fun onNewIntent(activity: Activity, intent: Intent) {
        RouteBinder.instance?.bind(activity, intent.extras)
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param fragment Fragment
     * @see Extra
     */
    @JvmStatic
    fun bind(fragment: Fragment) {
        RouteBinder.instance?.bind(fragment, fragment.arguments)
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param fragment Fragment
     * @see Extra
     */
    @JvmStatic
    fun bind(fragment: android.support.v4.app.Fragment) {
        RouteBinder.instance?.bind(fragment, fragment.arguments)
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param service IRouteService
     * @param bundle  Bundle
     * @see Extra
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





























