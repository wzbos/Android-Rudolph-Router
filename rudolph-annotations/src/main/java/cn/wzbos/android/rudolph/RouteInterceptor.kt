package cn.wzbos.android.rudolph

interface RouteInterceptor {
    fun intercept(router: IRouter<*, *, *>): Boolean
}