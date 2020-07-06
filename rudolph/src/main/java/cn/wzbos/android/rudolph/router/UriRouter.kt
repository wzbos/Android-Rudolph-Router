package cn.wzbos.android.rudolph.router

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import cn.wzbos.android.rudolph.RouteType

class UriRouter private constructor(builder: Builder<*>) : Router<Any?>(builder) {
    private var router: Router<Any?>? = null

    init {
        if (null != routeType) {
            when (routeType) {
                RouteType.ACTIVITY -> router = ActivityRouter(builder)
                RouteType.FRAGMENT, RouteType.FRAGMENT_V4, RouteType.FRAGMENT_ANDROID_X -> router = FragmentRouter(builder)
                RouteType.SERVICE -> router = ServiceRouter(builder)
                RouteType.METHOD -> router = MethodRouter(builder)
            }
        } else {
            val data: Uri? = uriData
            if (data !== Uri.EMPTY) {
                router = ActivityRouter(builder)
            }
        }
    }

    /**
     * open route,support route type: fragment/fragmentV4、provider,method
     *
     * @return return route result.
     */
    override fun open(): Any? {
        if (null == router) {
            return null
        }
        return router?.open()
    }

    /**
     * open route,support all route type
     *
     * @return return route result.
     * Activity return null
     * Fragment return a new instance
     * Provide return a IRouteService instance
     * Method return result
     */
    fun open(context: Context?): Any? {
        if (null == router) return null
        if (router is ActivityRouter) {
            (router as ActivityRouter).start(context)
            return null
        } else if (router is MethodRouter) {
            return (router as MethodRouter).open(context)
        } else {
            return router?.open()
        }
    }

    /**
     * open route
     *
     * @param fragment fragment
     */
    fun open(fragment: Fragment): Any? {
        return open(fragment.activity)
    }

    /**
     * open route
     *
     * @param fragmentV4 fragmentV4
     */
    fun open(fragmentV4: android.support.v4.app.Fragment): Any? {
        return open(fragmentV4.context)
    }

    /**
     * start Activity for fragment
     *
     * @param fragment    fragment
     * @param requestCode requestCode
     */
    fun open(fragment: Fragment, requestCode: Int) {
        if (null == router) return
        if (router is ActivityRouter) {
            (router as ActivityRouter).startForResult(fragment, requestCode)
        } else {
            open(fragment.activity)
        }
    }

    /**
     * start Activity for fragmentV4
     *
     * @param fragmentV4  fragmentV4
     * @param requestCode requestCode
     */
    fun open(fragmentV4: android.support.v4.app.Fragment, requestCode: Int) {
        if (null == router) return
        if (router is ActivityRouter) {
            (router as ActivityRouter).startForResult(fragmentV4, requestCode)
        } else {
            open(fragmentV4.activity)
        }
    }

    /**
     * start Activity for activity
     *
     * @param activity    activity
     * @param requestCode requestCode
     */
    fun open(activity: Activity, requestCode: Int) {
        if (null == router) return
        if (router is ActivityRouter) {
            (router as ActivityRouter).startForResult(activity, requestCode)
        } else {
            open(activity)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Builder<T : Builder<T>> constructor(url: String) : RouteBuilder<Builder<T>, UriRouter>(url) {
        var options: Bundle? = null
        var flags: Int = -1
        var enterAnim: Int = 0
        var exitAnim: Int = 0
        fun flags(flag: Int): T {
            flags = flag
            return this as T
        }

        fun options(options: Bundle?): T {
            this.options = options
            return this as T
        }

        fun transition(enterAnim: Int, exitAnim: Int): T {
            this.enterAnim = enterAnim
            this.exitAnim = exitAnim
            return this as T
        }

        override fun build(): UriRouter {
            return UriRouter(this)
        }
    }

}