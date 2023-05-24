package cn.wzbos.android.rudolph.annotations

import cn.wzbos.android.rudolph.ExtraType

/**
 * Created by wuzongbo on 2017/5/30.
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Extra(
    /**
     * 参数名称
     */
    val value: String = "",
    /**
     * 是否导出，false不生成参数传递方法
     */
    val export: Boolean = true,
    @Deprecated(
        "请使用objectType=ExtraObjectType.json",
        replaceWith = ReplaceWith("paramType=ExtraObjectType.json")
    )
    val json: Boolean = false,
    @Deprecated(
        "请使用objectType=ExtraObjectType.base64",
        replaceWith = ReplaceWith("paramType=ExtraObjectType.base64")
    )
    val base64: Boolean = false,
    /**
     * 序列化类型
     */
    val paramsType: ExtraType = ExtraType.Default
)