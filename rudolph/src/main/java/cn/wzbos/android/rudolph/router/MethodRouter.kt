package cn.wzbos.android.rudolph.router

import android.app.Application
import android.content.Context
import cn.wzbos.android.rudolph.Consts
import cn.wzbos.android.rudolph.RLog
import cn.wzbos.android.rudolph.Rudolph.application
import cn.wzbos.android.rudolph.annotations.Route
import cn.wzbos.android.rudolph.exception.ErrorCode
import cn.wzbos.android.rudolph.exception.ErrorMessage
import cn.wzbos.android.rudolph.exception.RudolphException
import cn.wzbos.android.rudolph.utils.TypeUtils
import java.util.*

class MethodRouter internal constructor(builder: UriRouter.Builder<*>) : Router<Any?>(builder) {
    private val TAG = "MethodRouter"
    private val uriKvs: MutableMap<String, String?>? = builder.uriAllParams
    override fun open(): Any? {
        return open(null)
    }

    fun open(context: Context?): Any? {
        if (super.intercept(context)) return null
        if (target == null) {
            callback?.onError(this, RudolphException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND_ERROR))
            return null
        }
        try {
            target?.methods?.forEach { m ->
                val route = m.getAnnotation(Route::class.java)
                if (route != null && route.value.equals(routePath, ignoreCase = true)) {
                    val values: MutableList<Any?> = ArrayList()
                    val params = extraTypes
                    if (null != params && params.isNotEmpty()) {
                        for ((argName, argType) in params) {
                            var argValue: Any? = null
                            if (Consts.RAW_URI == argName) {
                                argValue = rawUrl
                            } else {
                                if (Application::class.java.name == argType) {
                                    argValue = application
                                } else if (Context::class.java.name == argType) {
                                    argValue = context ?: application
                                } else {
                                    for ((key, value) in uriKvs!!) {
                                        if (argName.equals(key, ignoreCase = true)) {
                                            argValue = if (value.isNullOrEmpty()) {
                                                null
                                            } else {
                                                TypeUtils.getObject(context, argName, value, argType)
                                            }
                                            break
                                        }
                                    }
                                }
                            }
                            values.add(argValue)
                        }
                    }
                    val obj = m.invoke(null, *values.toTypedArray())
                    callback?.onSuccess(this)
                    return obj
                }
            }
        } catch (e: Exception) {
            if (null != callback) {
                callback?.onError(this, RudolphException(ErrorCode.METHOD_INVOKE_FAILED, "方法调用异常!", e))
            } else {
                RLog.e(TAG, "方法调用异常!", e)
            }
        }
        return null
    }

}