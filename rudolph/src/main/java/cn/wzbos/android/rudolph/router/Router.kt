package cn.wzbos.android.rudolph.router

import android.content.Context
import android.net.Uri
import android.os.Bundle
import cn.wzbos.android.rudolph.Interceptor
import cn.wzbos.android.rudolph.OnRouteListener
import cn.wzbos.android.rudolph.RouteType
import cn.wzbos.android.rudolph.Rudolph
import cn.wzbos.android.rudolph.Rudolph.interceptors

abstract class Router<T> internal constructor(builder: RouteBuilder<*, *>) {
    var rawUrl: String? = builder.rawUrl
    var extras: Bundle? = builder.extras
    var target: Class<*>? = builder.target
    var callback: OnRouteListener? = builder.callback
    var routePath: String? = builder.routePath
    var routeType: RouteType? = builder.routeType
    var routeTag: String? = builder.routeTag
    var extraTypes: MutableMap<String, String>? = builder.extraTypes

    val uriData: Uri
        get() {
            if (rawUrl?.contains("://") == true) {
                return Uri.parse(rawUrl)
            }
            if (!Rudolph.scheme.isNullOrBlank()) {
                return Uri.parse(Rudolph.scheme + "://" + rawUrl)
            }
            return Uri.EMPTY
        }

    fun intercept(context: Context?): Boolean {
        val interceptors: List<Interceptor>? = interceptors

        interceptors?.forEach {
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
        return "Router(rawUrl=$rawUrl, extras=$extras, target=$target, callback=$callback, routePath=$routePath, routeType=$routeType, routeTag=$routeTag, extraTypes=$extraTypes)"
    }


}