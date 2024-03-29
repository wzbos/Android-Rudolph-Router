package cn.wzbos.android.rudolph

import java.lang.reflect.Type

open class ExtraInfo @JvmOverloads constructor(
    var type: Type,
    val base64: Boolean = false,
    val json: Boolean = false
)

class UnknownExtraType(
    val className: String,
) : ExtraInfo(type = Any::class.java, json = true, base64 = true)