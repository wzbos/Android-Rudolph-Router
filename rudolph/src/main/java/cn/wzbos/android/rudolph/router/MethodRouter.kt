package cn.wzbos.android.rudolph.router

import android.app.Application
import android.content.Context
import cn.wzbos.android.rudolph.Consts
import cn.wzbos.android.rudolph.Rudolph
import cn.wzbos.android.rudolph.annotations.Route
import cn.wzbos.android.rudolph.exception.ErrorCode
import cn.wzbos.android.rudolph.exception.ErrorMessage
import cn.wzbos.android.rudolph.exception.RudolphException
import cn.wzbos.android.rudolph.logger.RLog
import cn.wzbos.android.rudolph.utils.TypeUtils
import cn.wzbos.android.rudolph.utils.match

class MethodRouter internal constructor(builder: UriRouter.Builder<*>) : Router<Any?>(builder) {
    companion object {
        const val TAG = "MethodRouter"
    }

    private val uriKvs: MutableMap<String, String?>? = builder.uriAllParams

    @Deprecated(message = "use execute", replaceWith = ReplaceWith("execute(context)"))
    fun open(context: Context?): Any? {
        return execute(context)
    }

    override fun execute(): Any? {
        return execute(null)
    }

    fun execute(context: Context?): Any? {
        if (super.intercept(context)) return null
        if (target == null) {
            callback?.onError(
                this,
                RudolphException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND_ERROR)
            )
            return null
        }
        try {
            target?.methods?.forEach { m ->
                val route = m.getAnnotation(Route::class.java)
                if (route != null && route.match(rawUrl)) {
                    val values: MutableList<Any?> = ArrayList()
                    val params = extraTypes
                    params?.forEach { (argName, argType) ->
                        var argValue: Any? = null
                        if (Consts.RAW_URI == argName) {
                            argValue = rawUrl
                        } else {
                            when {
                                Application::class.java == argType -> {
                                    argValue = Rudolph.context
                                }
                                Context::class.java == argType -> {
                                    argValue = context ?: Rudolph.context
                                }
                                else -> {
                                    kotlin.run {
                                        uriKvs?.forEach { (key, value) ->
                                            if (argName.equals(key, ignoreCase = true)) {
                                                argValue = if (value.isNullOrEmpty()) {
                                                    null
                                                } else {
                                                    TypeUtils.getObject(
                                                        context,
                                                        argName,
                                                        value,
                                                        argType
                                                    )
                                                }
                                                return@run
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        values.add(argValue)
                    }
                    val obj = m.invoke(null, *values.toTypedArray())
                    callback?.onSuccess(this)
                    return obj
                }
            }
        } catch (e: Exception) {
            if (null != callback) {
                callback?.onError(
                    this,
                    RudolphException(ErrorCode.METHOD_INVOKE_FAILED, "方法调用异常!", e)
                )
            } else {
                RLog.e(TAG, "方法调用异常!", e)
            }
        }
        return null
    }

}