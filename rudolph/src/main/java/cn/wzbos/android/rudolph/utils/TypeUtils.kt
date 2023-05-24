package cn.wzbos.android.rudolph.utils

import android.app.Application
import android.content.Context
import android.util.Base64
import cn.wzbos.android.rudolph.ExtraInfo
import cn.wzbos.android.rudolph.Rudolph
import cn.wzbos.android.rudolph.logger.RLog
import cn.wzbos.android.rudolph.router.RouteBuilder
import com.google.gson.Gson

/**
 * Created by wuzongbo on 2017/6/21.
 */
object TypeUtils {
    private fun toInteger(value: String?, default: Int? = null): Int? {
        var ret = default
        if (!value.isNullOrEmpty()) {
            ret = if (value.startsWith("0x", true)) {
                Integer.valueOf(value.substring(2), 16)
            } else {
                value.toInt()
            }
        }
        return ret
    }

    private fun toBoolean(value: String?, default: Boolean? = false): Boolean? {
        var ret = default
        if (!value.isNullOrEmpty()) {
            ret =
                if (value.equals("true", true) || value.equals("false", true)) {
                    java.lang.Boolean.parseBoolean(value)
                } else {
                    value.toInt() > 0
                }
        }
        return ret
    }

    private fun toDouble(value: String?, default: Double? = 0.0): Double? {
        var ret = default
        if (!value.isNullOrEmpty()) ret = value.toDouble()
        return ret
    }

    private fun toFloat(value: String?, default: Float? = 0f): Float? {
        var ret = default
        if (!value.isNullOrEmpty()) ret = value.toFloat()
        return ret
    }

    private fun toLong(value: String?, default: Long? = 0): Long? {
        var ret = default
        if (!value.isNullOrEmpty()) ret = value.toLong()
        return ret
    }

    private fun toShort(value: String?, default: Short? = 0): Short? {
        var ret = default
        if (!value.isNullOrEmpty()) {
            ret = if (value.startsWith("0x", true)) {
                value.substring(2).toShort(16)
            } else {
                value.toShort()
            }
        }
        return ret
    }

    private fun toByte(value: String?, default: Byte? = 0): Byte? {
        var ret = default
        if (!value.isNullOrEmpty()) {
            ret = if (value.startsWith("0x", true)) {
                java.lang.Byte.valueOf(value.substring(2), 16)
            } else {
                value.toByte()
            }
        }
        return ret
    }

    private fun toChar(value: String?, default: Char? = Char.MIN_VALUE): Char? {
        var ret = default
        if (!value.isNullOrEmpty()) ret = value[0]
        return ret
    }

    fun getObject(context: Context?, name: String, value: String?, type: ExtraInfo): Any? {
        return getObject(context, name, value, type, null)
    }

    fun getObject(
        context: Context?,
        name: String,
        value: String?,
        extraType: ExtraInfo,
        builder: RouteBuilder<*, *>?,
    ): Any? {
        RLog.d("TypeUtils", "$name = $value, type:$extraType, bundle:$builder")
        return when (extraType.type) {
            String::class.java, CharSequence::class.java -> value?.also {
                builder?.putExtra(
                    name,
                    it
                )
            }
            Int::class.java -> toInteger(value, 0)?.also { builder?.putExtra(name, it) }
            Boolean::class.java -> toBoolean(value, false)?.also { builder?.putExtra(name, it) }
            Double::class.java -> toDouble(value, 0.0)?.also { builder?.putExtra(name, it) }
            Float::class.java -> toFloat(value, 0f)?.also { builder?.putExtra(name, it) }
            Long::class.java -> toLong(value, 0L)?.also { builder?.putExtra(name, it) }
            Short::class.java -> toShort(value)?.also { builder?.putExtra(name, it) }
            Byte::class.java -> toByte(value)?.also { builder?.putExtra(name, it) }
            Char::class.java -> toChar(value)?.also { builder?.putExtra(name, it) }
            Integer::class.java -> toInteger(value)?.also { builder?.putExtra(name, it) }
            java.lang.Boolean::class.java -> toBoolean(value)?.also { builder?.putExtra(name, it) }
            java.lang.Double::class.java -> toDouble(value)?.also { builder?.putExtra(name, it) }
            java.lang.Float::class.java -> toFloat(value)?.also { builder?.putExtra(name, it) }
            java.lang.Long::class.java -> toLong(value)?.also { builder?.putExtra(name, it) }
            java.lang.Short::class.java -> toShort(value)?.also { builder?.putExtra(name, it) }
            java.lang.Byte::class.java -> toByte(value)?.also { builder?.putExtra(name, it) }
            java.lang.Character::class.java -> toChar(value)?.also { builder?.putExtra(name, it) }
            Application::class.java -> Rudolph.context
            Context::class.java -> context ?: Rudolph.context
            else -> {
                var strValue = value
                var ret: Any? = null
                if (extraType.base64) {
                    strValue = String(Base64.decode(strValue, Base64.NO_PADDING or Base64.URL_SAFE))
                }
                if (extraType.json) {
                    ret = Gson().toJson(strValue, extraType.type)
                }
                //extra 应为原始值
                builder?.putExtra(name, value)
                ret
            }
        }
    }
}