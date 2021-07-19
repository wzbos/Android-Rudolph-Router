package cn.wzbos.android.rudolph.utils

import android.content.Context
import android.text.TextUtils
import cn.wzbos.android.rudolph.logger.RLog
import cn.wzbos.android.rudolph.router.RouteBuilder

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

    fun getObject(context: Context?, name: String, value: String, type: String): Any? {
        return getObject(context, name, value, type, null)
    }

    fun getObject(
        context: Context?,
        name: String,
        value: String,
        target: String,
        builder: RouteBuilder<*, *>?
    ): Any? {
        RLog.d("TypeUtils", "$name = $value, target:$target, bundle:$builder")
        return when (target) {
            "java.lang.String", "java.lang.CharSequence" -> {  //String、CharSequence
                builder?.putExtra(name, value)
                value
            }
            "int", "java.lang.Integer" -> {    //int、Integer
                val ret = toInteger(value)
                builder?.putExtra(name, ret)
                ret
            }
            "boolean", "java.lang.Boolean" -> { //boolean、Boolean
                val ret = toBoolean(value)
                builder?.putExtra(name, ret)
                ret
            }
            "double", "java.lang.Double" -> {  //double、Double
                val ret = toDouble(value)
                builder?.putExtra(name, ret)
                ret
            }
            "float", "java.lang.Float" -> {    //float、Float
                val ret = toFloat(value)
                builder?.putExtra(name, ret)
                ret
            }
            "long", "java.lang.Long" -> {      //long、Long
                val ret = toLong(value)
                builder?.putExtra(name, ret)
                ret
            }
            "short", "java.lang.Short" -> {    //short、Short
                val ret = toShort(value)
                builder?.putExtra(name, ret)
                ret
            }
            "byte", "java.lang.Byte" -> {      //byte、Byte
                val ret = toByte(value)
                builder?.putExtra(name, ret)
                ret
            }
            "char", "java.lang.Character" -> { //char、Character
                val ret = toChar(value)
                builder?.putExtra(name, ret)
                ret
            }
            "android.content.Context" -> {      //Context
                context
            }
            else -> {                           //Object
                builder?.putExtra(name, value)
                value
            }
        }
    }
}