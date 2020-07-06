package cn.wzbos.android.rudolph.router

import android.app.Fragment
import android.util.Log
import cn.wzbos.android.rudolph.RLog
import cn.wzbos.android.rudolph.exception.ErrorCode
import cn.wzbos.android.rudolph.exception.ErrorMessage
import cn.wzbos.android.rudolph.exception.RudolphException

@Suppress("UNCHECKED_CAST")
class FragmentRouter<R> : Router<R?> {
    internal constructor(builder: RouteBuilder<*, *>) : super(builder)
    private constructor(builder: Builder<*, *>) : super(builder)


    override fun open(): R? {
        if (super.intercept(null)) return null
        if (target == null) {
            callback?.onError(this, RudolphException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND_ERROR))
            return null
        }

        try {
            val instance = target?.getConstructor()?.newInstance()
            if (instance is Fragment) {
                instance.arguments = extras
            } else if (instance is android.support.v4.app.Fragment) {
                instance.arguments = extras
            }

            callback?.onSuccess(this)
            return instance as R
        } catch (e: Exception) {
            if (callback != null) {
                callback?.onError(this, RudolphException(ErrorCode.FRAGMENT_CREATE_FAILED, "Fragment 创建失败！", e))
            } else {
                RLog.e("FragmentRouter", "Fragment 创建失败！", e)
            }
        }
        return null
    }

    open class Builder<T, R> : RouteBuilder<Builder<T, R>?, FragmentRouter<R>?> {
        constructor(cls: Class<*>?) : super(cls)
        constructor(path: String) : super(path)

        override fun build(): FragmentRouter<R> {
            return FragmentRouter(this)
        }
    }
}