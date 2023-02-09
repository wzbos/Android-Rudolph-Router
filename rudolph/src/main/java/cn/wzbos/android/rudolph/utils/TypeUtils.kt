package cn.wzbos.android.rudolph.utils

import android.content.Context
import android.text.TextUtils
import cn.wzbos.android.rudolph.logger.RLog
import cn.wzbos.android.rudolph.router.RouteBuilder
import java.lang.reflect.Type

/**
 * Created by wuzongbo on 2017/6/21.
 */
object TypeUtils {
    private fun toInteger(value: String): Int {
        var ret = 0
        if (!TextUtils.isEmpty(value)) {
            ret = if (value.startsWith("0x", true)) {
                Integer.valueOf(value.substring(2), 16)
            } else {
                value.toInt()
            }
        }
        return ret
    }

    private fun toBoolean(value: String): Boolean {
        var ret = false
        if (!TextUtils.isEmpty(value)) {
            ret =
                if (value.equals("true", true) || value.equals("false", true)) {
                    java.lang.Boolean.parseBoolean(value)
                } else {
                    value.toInt() > 0
                }
        }
        return ret
    }

    private fun toDouble(value: String): Double {
        var ret = 0.0
        if (!TextUtils.isEmpty(value)) ret = value.toDouble()
        return ret
    }

    private fun toFloat(value: String): Float {
        var ret = 0f
        if (!TextUtils.isEmpty(value)) ret = value.toFloat()
        return ret
    }

    private fun toLong(value: String): Long {
        var ret: Long = 0
        if (!TextUtils.isEmpty(value)) ret = value.toLong()
        return ret
    }

    private fun toShort(value: String): Short {
        var ret: Short = 0
        if (!TextUtils.isEmpty(value)) {
            ret = if (value.startsWith("0x", true)) {
                value.substring(2).toShort(16)
            } else {
                value.toShort()
            }
        }
        return ret
    }

    private fun toByte(value: String): Byte {
        var ret: Byte = 0
        if (!TextUtils.isEmpty(value)) {
            ret = if (value.startsWith("0x", true)) {
                java.lang.Byte.valueOf(value.substring(2), 16)
            } else {
                value.toByte()
            }
        }
        return ret
    }

    private fun toChar(value: String): Char {
        var ret = 0.toChar()
        if (!TextUtils.isEmpty(value)) ret = value[0]
        return ret
    }

    fun getObject(context: Context?, name: String, value: String, type: Type): Any? {
        return getObject(context, name, value, type, null)
    }

    fun getObject(
        context: Context?,
        name: String,
        value: String,
        type: Type,
        builder: RouteBuilder<*, *>?,
    ): Any? {
        RLog.d("TypeUtils", "$name = $value, type:$type, bundle:$builder")
        return when (type) {
            String::class.java, CharSequence::class.java -> {  //String、CharSequence
                builder?.putExtra(name, value)
                value
            }
            Int::class.java, Integer::class.java -> {    //int、Integer
                val ret = toInteger(value)
                builder?.putExtra(name, ret)
                ret
            }
            Boolean::class.java -> { //boolean、Boolean
                val ret = toBoolean(value)
                builder?.putExtra(name, ret)
                ret
            }
            Double::class.java -> {  //double、Double
                val ret = toDouble(value)
                builder?.putExtra(name, ret)
                ret
            }
            Float::class.java -> {    //float、Float
                val ret = toFloat(value)
                builder?.putExtra(name, ret)
                ret
            }
            Long::class.java -> {      //long、Long
                val ret = toLong(value)
                builder?.putExtra(name, ret)
                ret
            }
            Short::class.java -> {    //short、Short
                val ret = toShort(value)
                builder?.putExtra(name, ret)
                ret
            }
            Byte::class.java -> {      //byte、Byte
                val ret = toByte(value)
                builder?.putExtra(name, ret)
                ret
            }
            Char::class.java -> { //char、Character
                val ret = toChar(value)
                builder?.putExtra(name, ret)
                ret
            }
            Context::class.java -> {      //Context
                context
            }
            else -> {                           //Object
                builder?.putExtra(name, value)
                value
            }
        }
    }
}