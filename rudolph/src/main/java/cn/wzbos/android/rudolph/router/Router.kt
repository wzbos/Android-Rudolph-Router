package cn.wzbos.android.rudolph.router

import android.content.Context
import android.net.Uri
import android.os.Bundle
import cn.wzbos.android.rudolph.*

abstract class Router<T> internal constructor(builder: RouteBuilder<*, *>) {
    //调用地址
    var rawUrl: String = builder.rawUrl
    var extras: Bundle? = builder.extras
    var target: Class<*>? = builder.target
    var callback: OnRouteListener? = builder.callback
    var routeSchema: MutableList<String>? = builder.scheme
    var routeHost: MutableList<String>? = builder.host
    var routePath: MutableList<String>? = builder.path
    var routeType: RouteType? = builder.routeType
    var routeTag: String? = builder.routeTag
    var extraTypes: MutableMap<String, ExtraInfo>? = builder.extraTypes
    var interceptors: MutableList<Class<out RouteInterceptor>>? = builder.interceptors

    val uriData: Uri
        get() {
            if (rawUrl.contains("://")) {
                return Uri.parse(rawUrl)
            }
            if (!Rudolph.scheme.isNullOrBlank()) {
                return Uri.parse(Rudolph.scheme + "://" + rawUrl)
            }
            return Uri.EMPTY
        }

    fun intercept(context: Context?): Boolean {
        interceptors?.forEach {
            if (it.newInstance().intercept()) {
                return true
            }
        }

        Rudolph.globalInterceptors?.forEach {
            if (it.intercept(context, this)) {
                return true
            }
        }
        return false
    }

    @Deprecated(message = "use execute", replaceWith = ReplaceWith("execute()"))
    fun open(): T {
        return execute()
    }

    /**
     * 执行路由
     */
    abstract fun execute(): T

    override fun toString(): String {
        return "Router(rawUrl=$rawUrl, extras=$extras, target=$target, callback=$callback, routeType=$routeType, routeTag=$routeTag, extraTypes=$extraTypes)"
    }


}