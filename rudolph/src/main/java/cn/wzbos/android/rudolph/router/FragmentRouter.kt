package cn.wzbos.android.rudolph.router

import androidx.fragment.app.Fragment
import cn.wzbos.android.rudolph.exception.ErrorCode
import cn.wzbos.android.rudolph.exception.ErrorMessage
import cn.wzbos.android.rudolph.exception.RudolphException
import cn.wzbos.android.rudolph.logger.RLog

class FragmentRouter<R> : Router<R?> {
    private constructor(builder: Builder<*, *>) : super(builder)
    constructor(builder: UriRouter.Builder<*>) : super(builder)

    @Suppress("UNCHECKED_CAST")
    override fun execute(): R? {
        if (super.intercept(null)) return null
        if (target == null) {
            callback?.onError(
                this,
                RudolphException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND_ERROR)
            )
            return null
        }

        try {
            val constructor = target?.getConstructor()
            if (constructor == null) {
                RLog.e("FragmentRouter", "$target 必须提供一个无参的构造函数！")
                return null
            }

            val instance = target?.getConstructor()?.newInstance()
            if (instance is Fragment) {
                instance.arguments = extras
            }

            callback?.onSuccess(this)
            return instance as R
        } catch (e: Exception) {
            if (callback != null) {
                callback?.onError(
                    this,
                    RudolphException(ErrorCode.FRAGMENT_CREATE_FAILED, "Fragment 创建失败！", e)
                )
            } else {
                RLog.e("FragmentRouter", "Fragment 创建失败！", e)
            }
        }
        return null
    }

    open class Builder<T, R>(path: String) :
        RouteBuilder<Builder<T, R>?, FragmentRouter<R>?>(path) {

        override fun build(): FragmentRouter<R> {
            return FragmentRouter(this)
        }

        fun execute(): R? {
            return build().execute()
        }
    }
}