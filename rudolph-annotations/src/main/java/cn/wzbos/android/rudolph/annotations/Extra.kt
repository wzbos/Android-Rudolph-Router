package cn.wzbos.android.rudolph.annotations

/**
 * Created by wuzongbo on 2017/5/30.
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Extra(
    val value: String = "",
    val export: Boolean = true,
    val json: Boolean = false,
    val base64: Boolean = false
)