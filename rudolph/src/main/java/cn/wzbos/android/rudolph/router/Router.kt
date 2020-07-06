package cn.wzbos.android.rudolph.router

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
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
            if (rawUrl!!.contains("://")) {
                return Uri.parse(rawUrl)
            }
            if (!TextUtils.isEmpty(Rudolph.scheme)) {
                return Uri.parse(Rudolph.scheme + "://" + rawUrl)
            }
            return Uri.EMPTY
        }

    fun intercept(context: Context?): Boolean {
        val interceptors: List<Interceptor>? = interceptors
        if (null != interceptors && interceptors.isNotEmpty()) {
            for (interceptor: Interceptor in interceptors) {
                if (interceptor.intercept(context, this)) {
                    return true
                }
            }
        }
        return false
    }

    abstract fun open(): T

    override fun toString(): String {
        return "Router(rawUrl=$rawUrl, extras=$extras, target=$target, callback=$callback, routePath=$routePath, routeType=$routeType, routeTag=$routeTag, extraTypes=$extraTypes)"
    }


}