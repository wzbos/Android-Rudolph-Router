package cn.wzbos.android.rudolph.router

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import cn.wzbos.android.rudolph.Rudolph.application
import cn.wzbos.android.rudolph.exception.ErrorMessage
import cn.wzbos.android.rudolph.exception.RudolphException

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


    internal constructor(builder: RouteBuilder<*, *>) : super(builder)
    private constructor(builder: Builder<*>) : super(builder) {
        options = builder.options
        flags = builder.flags
        enterAnim = builder.enterAnim
        exitAnim = builder.exitAnim
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
        if (context === context!!.applicationContext) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        extras?.let {
            intent.putExtras(it)
        }
        return intent
    }

    override fun open(): Any? {
        start(application)
        return null
    }

    fun start(context: Context?) {
        if (super.intercept(context)) return
        val intent = getIntent(context) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context!!.startActivity(intent, options)
        } else {
            context!!.startActivity(intent)
        }
        startOver(context)
    }

    fun start(fragment: Fragment) {
        start(fragment.activity)
    }

    fun start(fragmentV4: android.support.v4.app.Fragment) {
        start(fragmentV4.context)
    }

    /**
     * startForResult for android.app.Fragment
     */
    fun startForResult(fragment: Fragment, requestCode: Int) {
        this.requestCode = requestCode
        if (super.intercept(fragment.activity)) return
        val intent = getIntent(fragment.activity) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            fragment.startActivityForResult(intent, requestCode, options)
        } else {
            fragment.startActivityForResult(intent, requestCode)
        }
        startOver(fragment.activity)
    }

    /**
     * startForResult for android.support.v4.app.Fragment
     */
    fun startForResult(fragmentV4: android.support.v4.app.Fragment, requestCode: Int) {
        this.requestCode = requestCode
        if (super.intercept(fragmentV4.context)) return
        val intent = getIntent(fragmentV4.context) ?: return
        fragmentV4.startActivityForResult(intent, requestCode)
        startOver(fragmentV4.activity)
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
    }

    private fun startOver(context: Context?) {
        if (-1 != enterAnim && -1 != exitAnim && context is Activity) {
            context.overridePendingTransition(enterAnim, exitAnim)
        }
        callback?.onSuccess(this)
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


        constructor(cls: Class<*>?) : super(cls)
        constructor(url: String) : super(url)

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

        override fun build(): ActivityRouter {
            return ActivityRouter(this)
        }
    }
}