package cn.wzbos.android.rudolph

interface IRouter<R : IRouter<R, T, E>, T, E> {
    //调用地址
    var rawUrl: String
    var extras: E?
    var target: Class<*>?
    var routeSchema: MutableList<String>?
    var routeHost: MutableList<String>?
    var routePath: MutableList<String>?
    var routeType: RouteType?
    var routeTag: String?
    var extraTypes: MutableMap<String, ExtraType>?
    var interceptors: MutableList<Class<out RouteInterceptor>>?
    var caller: Any?
    var callback: Callback?

    /**
     * 将当前路由转换为另一个路由
     */
    fun transform(router: R)

    /**
     * 执行路由
     */
    fun execute(): T

    /**
     * Created by wuzongbo on 2017/4/12.
     */
    interface Callback {
        fun onSuccess(router: IRouter<*, *, *>?)
        fun onError(router: IRouter<*, *, *>?, throwable: Throwable?)
    }
}