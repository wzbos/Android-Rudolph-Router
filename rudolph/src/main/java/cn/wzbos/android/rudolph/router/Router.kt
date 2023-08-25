package cn.wzbos.android.rudolph.router

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.wzbos.android.rudolph.*
import cn.wzbos.android.rudolph.logger.RLog

abstract class Router<T> internal constructor(builder: RouteBuilder<*, *>) :
    IRouter<Router<T>, T, Bundle> {
    //调用地址
    override var rawUrl: String = builder.rawUrl
    override var extras: Bundle? = builder.extras
    override var target: Class<*>? = builder.target
    override var callback: IRouter.Callback? = builder.callback
    override var routeSchema: MutableList<String>? = builder.scheme
    override var routeHost: MutableList<String>? = builder.host
    override var routePath: MutableList<String>? = builder.path
    override var routeType: RouteType? = builder.routeType
    override var routeTag: String? = builder.routeTag
    override var extraTypes: MutableMap<String, ExtraType>? = builder.extraTypes
    override var interceptors: MutableList<Class<out RouteInterceptor>>? = builder.interceptors
    override var caller: Any? = null
    override fun transform(router: Router<T>) {
        this.rawUrl = router.rawUrl
        this.extras = router.extras
        this.target = router.target
        this.routeSchema = router.routeSchema
        this.routeHost = router.routeHost
        this.routePath = router.routePath
        this.routeType = router.routeType
        this.routeTag = router.routeTag
        this.extraTypes = router.extraTypes
    }

    val context: Context?
        get() {
            return try {
                when (caller) {
                    is Fragment -> (caller as? Fragment)?.activity
                    is Activity -> (caller as? Activity)
                    is Application -> (caller as? Context)
                    else -> Rudolph.context
                } ?: Rudolph.context
            } catch (e: Exception) {
                RLog.e("Rudolph", "getContext error!", e)
                Rudolph.context
            }
        }

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

    fun intercept(caller: Any?): Boolean {
        this.caller = caller

        interceptors?.forEach {
            if (it.newInstance().intercept(this)) {
                return true
            }
        }

        Rudolph.globalInterceptors?.forEach {
            if (it.intercept(this)) {
                return true
            }
        }
        return false
    }

    @Deprecated(message = "use execute", replaceWith = ReplaceWith("execute()"))
    fun open(): T {
        return execute()
    }

    override fun toString(): String {
        return "Router(rawUrl=$rawUrl, extras=$extras, target=$target, callback=$callback, routeType=$routeType, routeTag=$routeTag, extraTypes=$extraTypes)"
    }


}