package cn.wzbos.android.rudolph.router

import android.content.Context
import cn.wzbos.android.rudolph.UnknownExtraType
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

    private val uriKvs: MutableMap<String, String?> = builder.uriAllParams

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
                    extraTypes?.onEachIndexed { index, entry ->
                        //兼容2.1.0之前的版本，反射获取参数类型
                        if (entry.value is UnknownExtraType) {
                            entry.value.type = m.parameterTypes[index]
                        }
                    }?.forEach { (argName, argType) ->
                        var value: String? = null
                        for (element in uriKvs) {
                            if (argName.equals(element.key, ignoreCase = true)) {
                                value = element.value
                                break
                            }
                        }

                        val argValue: Any? = TypeUtils.getObject(
                            context,
                            argName,
                            value,
                            argType
                        )
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