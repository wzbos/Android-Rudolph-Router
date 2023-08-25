package cn.wzbos.android.rudolph.router

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import cn.wzbos.android.rudolph.RouteType
import cn.wzbos.android.rudolph.logger.RLog

/**
 * Uri 路由
 */
@Suppress("UNUSED")
class UriRouter private constructor(builder: Builder<*>) : Router<Any?>(builder) {
    private var router: Router<Any?>? = null

    init {
        if (null != routeType) {
            when (routeType) {
                RouteType.ACTIVITY -> router = ActivityRouter(builder)
                RouteType.FRAGMENT,
                RouteType.FRAGMENT_V4,
                RouteType.FRAGMENT_ANDROID_X -> router = FragmentRouter(builder)
                RouteType.SERVICE -> router = ServiceRouter(builder)
                RouteType.METHOD -> router = MethodRouter(builder)
                else
                -> RLog.e("UriRouter", "unknown routeType=$routeType")
            }
        } else {
            val data: Uri = uriData
            if (data !== Uri.EMPTY) {
                router = ActivityRouter(builder)
            }
        }
    }

    @Deprecated(
        message = "use execute",
        replaceWith = ReplaceWith("execute(context)")
    )
    fun open(context: Context?): Any? {
        return execute(context)
    }

    @Deprecated(
        message = "use execute",
        replaceWith = ReplaceWith("execute(fragment)")
    )
    fun open(fragment: Fragment?): Any? {
        return execute(fragment)
    }

    @Deprecated(
        message = "use execute",
        replaceWith = ReplaceWith("execute(fragment, requestCode)")
    )
    fun open(fragment: Fragment?, requestCode: Int) {
        return execute(fragment, requestCode)
    }

    @Deprecated(
        message = "use execute",
        replaceWith = ReplaceWith("execute(activity, requestCode)")
    )
    fun open(activity: Activity, requestCode: Int) {
        return execute(activity, requestCode)
    }

    /**
     * 执行路由，适用于全部类型路由
     *
     * @return 根据路由类型返回不同的结果
     * ActivityRouter   返回：Null
     * FragmentRouter   返回：Fragment 实例
     * ServiceRouter    返回：Service 实例
     * MethodRouter     返回：结果值
     */
    override fun execute(): Any? {
        if (null == router) {
            return null
        }
        return router?.execute()
    }

    /**
     * 执行路由，适用于全部类型路由
     * @param context 上下文
     * @return 根据路由类型返回不同的结果
     * ActivityRouter   返回：Null
     * FragmentRouter   返回：Fragment 实例
     * ServiceRouter    返回：Service 实例
     * MethodRouter     返回：结果值
     */
    fun execute(context: Context?): Any? {
        return when (router) {
            is ActivityRouter -> {
                (router as ActivityRouter).start(context)
                null
            }
            is MethodRouter -> {
                (router as MethodRouter).execute(context)
            }
            else -> {
                router?.execute()
            }
        }
    }

    /**
     * 打开路由，适用于启动Activity页面
     *
     * @param activity    Activity
     * @param requestCode requestCode
     */
    fun execute(activity: Activity, requestCode: Int) {
        if (router is ActivityRouter) {
            (router as ActivityRouter).startForResult(activity, requestCode)
        } else {
            execute(activity)
        }
    }


    /**
     * 打开路由，适用于全部类型路由
     *
     * @param fragment fragment
     */
    fun execute(fragment: Fragment?): Any? {
        return when (router) {
            is ActivityRouter -> {
                (router as ActivityRouter).start(fragment)
                null
            }
            is MethodRouter -> {
                (router as MethodRouter).execute(fragment)
            }
            else -> {
                router?.execute()
            }
        }
    }


    /**
     * 打开路由，适用于启动Activity页面
     *
     * @param fragment    fragment
     * @param requestCode requestCode
     */
    fun execute(fragment: Fragment?, requestCode: Int) {
        if (null == router) return
        if (router is ActivityRouter) {
            (router as ActivityRouter).startForResult(fragment, requestCode)
        } else {
            execute(fragment?.context)
        }
    }


    /**
     * 转换成Activity路由
     */
    fun asActivityRouter(): ActivityRouter? {
        return if (router is ActivityRouter) {
            router as ActivityRouter
        } else null
    }

    /**
     * 转换成Fragment路由
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> asFragment(): FragmentRouter<T>? {
        return if (router is FragmentRouter) {
            router as FragmentRouter<T>
        } else null
    }

    /**
     * 转换成方法路由
     */
    fun asMethodRouter(): MethodRouter? {
        return if (router is MethodRouter) {
            router as MethodRouter
        } else null
    }


    @Suppress("UNCHECKED_CAST")
    class Builder<T : Builder<T>> constructor(url: String) :
        RouteBuilder<Builder<T>, UriRouter>(url) {
        var options: Bundle? = null
            private set
        var flags: Int = -1
            private set
        var enterAnim: Int = 0
            private set
        var exitAnim: Int = 0
            private set
        var delayFinish: Long = -1
            private set

        /**
         * 设置Activity的启动标识
         * @param flag 启动标识
         */
        fun flags(flag: Int): T {
            flags = flag
            return this as T
        }

        /**
         * 设置Activity的启动参数
         * @param options 启动参数
         */
        fun options(options: Bundle?): T {
            this.options = options
            return this as T
        }

        /**
         * 设置Activity的启动和关闭动画，仅对Activity路由生效
         * @param enterAnim 启动动画资源ID
         * @param exitAnim 关闭动画资源ID
         */
        fun transition(@AnimRes enterAnim: Int, @AnimRes exitAnim: Int): T {
            this.enterAnim = enterAnim
            this.exitAnim = exitAnim
            return this as T
        }

        /**
         * 延迟关闭,仅对Activity生效（默认一秒）
         */
        fun delayFinish(): T {
            return delayFinish(1000)
        }

        /**
         * 延迟关闭,仅对Activity生效
         * @param delay 延迟时间（单位毫秒）
         */
        fun delayFinish(delay: Long): T {
            this.delayFinish = delay
            return this as T
        }

        /**
         * 构建路由
         * @return UriRouter
         */
        override fun build(): UriRouter {
            return UriRouter(this)
        }

        /**
         * 构建并执行路由，适用于全部类型路由
         *
         * @return 根据路由类型返回不同的结果
         * ActivityRouter   返回：Null
         * FragmentRouter   返回：Fragment 实例
         * ServiceRouter    返回：Service 实例
         * MethodRouter     返回：结果值
         */
        fun execute(): Any? {
            return build().execute()
        }

        /**
         * 构建并执行路由，适用于全部类型路由
         *
         * @return 根据路由类型返回不同的结果
         * ActivityRouter   返回：Null
         * FragmentRouter   返回：Fragment 实例
         * ServiceRouter    返回：Service 实例
         * MethodRouter     返回：结果值
         */
        fun execute(context: Context?): Any? {
            return build().execute(context)
        }

        /**
         * 构建并执行路由，适用于全部类型路由
         *
         * @param fragment Fragment
         * @return 根据路由类型返回不同的结果
         * 参考：
         * ActivityRouter   返回：Null
         * FragmentRouter   返回：Fragment 实例
         * ServiceRouter    返回：Service 实例
         * MethodRouter     返回：结果值
         */
        fun execute(fragment: Fragment?): Any? {
            return build().execute(fragment)
        }

        /**
         * 构建并执行路由，适用于启动Activity页面
         *
         * @param fragment Fragment
         * @return requestCode RequestCode
         */
        fun execute(fragment: Fragment, requestCode: Int) {
            return build().execute(fragment, requestCode)
        }


        /**
         * 构建并执行路由，适用于启动Activity页面
         *
         * @param activity    Activity
         * @param requestCode requestCode
         */
        fun execute(activity: Activity, requestCode: Int) {
            return build().execute(activity, requestCode)
        }

    }

}