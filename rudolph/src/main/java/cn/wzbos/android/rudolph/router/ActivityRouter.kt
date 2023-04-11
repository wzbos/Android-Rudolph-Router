package cn.wzbos.android.rudolph.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cn.wzbos.android.rudolph.Rudolph
import cn.wzbos.android.rudolph.exception.ErrorMessage
import cn.wzbos.android.rudolph.exception.RudolphException

@Suppress("UNUSED")
class ActivityRouter : Router<Any?> {
    var options: Bundle? = null
        private set
    var flags = -1
        private set
    var enterAnim = -1
        private set
    var exitAnim = -1
        private set
    var requestCode = -1
        private set
    var delayFinish: Long = -1
        private set

    fun buildUpon(): RouteBuilder<*, *> {
        return Builder(this)
    }

    private constructor(builder: Builder<*>) : super(builder) {
        options = builder.options
        flags = builder.flags
        enterAnim = builder.enterAnim
        exitAnim = builder.exitAnim
        delayFinish = builder.delayFinish
    }

    constructor(builder: UriRouter.Builder<*>) : super(builder) {
        options = builder.options
        flags = builder.flags
        enterAnim = builder.enterAnim
        exitAnim = builder.exitAnim
        delayFinish = builder.delayFinish
    }


    private fun getIntent(context: Context?): Intent? {
        val intent: Intent
        if (target == null) {
            val data = uriData
            if (data === Uri.EMPTY) {
                callback?.onError(this, RudolphException(ErrorMessage.NOT_FOUND_ERROR))
                return null
            } else {
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = data
            }
        } else {
            intent = Intent(context, target)
        }
        if (-1 != flags) intent.flags = flags
        if (context === context?.applicationContext) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        extras?.let {
            intent.putExtras(it)
        }
        return intent
    }

    override fun execute(): Any? {
        start(Rudolph.context)
        return null
    }


    fun start(fragment: Fragment?) {
        if (fragment == null) {
            callback?.onError(this, RudolphException("fragment is null！"))
            return
        }
        start(fragment.activity)
    }

    fun start(context: Context?) {
        if (super.intercept(context))
            return

        if (context == null) {
            callback?.onError(this, RudolphException("context is null！"))
            return
        }

        val intent = getIntent(context)
        if (intent != null) {
            context.startActivity(intent, options)
            startOver(context)
            if (context is Activity)
                finish(context)
        }
    }

    /**
     * startForResult for android.support.v4.app.Fragment
     */
    fun startForResult(fragment: Fragment?, requestCode: Int) {
        this.requestCode = requestCode
        if (super.intercept(fragment?.context))
            return

        if (fragment == null) {
            callback?.onError(this, RudolphException("fragment is null！"))
            return
        }

        val intent = getIntent(fragment.context)
        fragment.startActivityForResult(intent, requestCode)
        startOver(fragment.activity)
        finish(fragment.activity)
    }

    /**
     * startForResult for Activity
     */
    fun startForResult(activity: Activity, requestCode: Int) {
        this.requestCode = requestCode
        if (super.intercept(activity)) return
        val intent = getIntent(activity) ?: return

        activity.startActivityForResult(intent, requestCode, options)
        startOver(activity)
        finish(activity)
    }


    /**
     * startForResult for Activity
     */
    fun startForResult(
        activity: FragmentActivity,
        resultCallback: ActivityResultCallback
    ) {
        if (super.intercept(activity)) return
        val intent = getIntent(activity) ?: return
        this.requestCode = ActivityResultRegister.register(resultCallback)
        activity.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (Lifecycle.Event.ON_DESTROY == event) {
                    ActivityResultRegister.remove(requestCode)
                }
            }
        })
        activity.startActivityForResult(intent, requestCode, options)
        startOver(activity)
        finish(activity)
    }


    private fun startOver(context: Context?) {
        if (-1 != enterAnim && -1 != exitAnim && context is Activity) {
            context.overridePendingTransition(enterAnim, exitAnim)
        }
        callback?.onSuccess(this)
    }

    private fun finish(activity: Activity?) {
        if (delayFinish > -1) {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    activity?.finish()
                },
                delayFinish
            )
        }
    }

    /**
     * ActivityRouter Builder
     */
    @Suppress("UNCHECKED_CAST")
    open class Builder<T : Builder<T>?> : RouteBuilder<Builder<T>?, ActivityRouter?> {
        var options: Bundle? = null
            private set
        var flags = -1
            private set
        var enterAnim = 0
            private set
        var exitAnim = 0
            private set
        var delayFinish: Long = -1
            private set

        constructor(url: String) : super(url)

        //        constructor(target: Class<*>) : super(target: Class<*>)
        constructor(router: ActivityRouter) : super(router.rawUrl) {
            this.options = router.options
            this.flags = router.flags
            this.enterAnim = router.enterAnim
            this.exitAnim = router.exitAnim
            this.delayFinish = router.delayFinish
        }

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
         * 设置Activity的启动和关闭动画
         * @param enterAnim 启动动画资源ID
         * @param exitAnim 关闭动画资源ID
         */
        fun transition(@AnimRes enterAnim: Int, @AnimRes exitAnim: Int): T {
            this.enterAnim = enterAnim
            this.exitAnim = exitAnim
            return this as T
        }

        /**
         * 延迟关闭（默认一秒）
         */
        fun delayFinish(): T {
            return delayFinish(1000)
        }

        /**
         * 延迟关闭
         * @param delay 延迟时间（单位毫秒）
         */
        fun delayFinish(delay: Long): T {
            this.delayFinish = delay
            return this as T
        }

        /**
         * 构建路由
         */
        override fun build(): ActivityRouter {
            return ActivityRouter(this)
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
        fun buildStart(context: Context?) {
            return build().start(context)
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
        fun buildStart(fragment: Fragment?) {
            return build().start(fragment)
        }

        /**
         * 构建并执行路由，适用于启动Activity页面
         *
         * @param fragment Fragment
         * @return requestCode RequestCode
         */
        fun buildStartForResult(fragment: Fragment?, requestCode: Int) {
            return build().startForResult(fragment, requestCode)
        }

        /**
         * 构建并执行路由，适用于启动Activity页面
         *
         * @param activity    Activity
         * @param requestCode requestCode
         */
        fun buildStartForResult(activity: Activity, requestCode: Int) {
            return build().startForResult(activity, requestCode)
        }

        fun startForResult(
            activity: FragmentActivity,
            resultCallback: ActivityResultCallback
        ) {
            return build().startForResult(activity, resultCallback)
        }
    }
}