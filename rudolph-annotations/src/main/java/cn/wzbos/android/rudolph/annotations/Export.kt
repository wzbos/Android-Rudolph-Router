package cn.wzbos.android.rudolph.annotations

/**
 * 标记当前路由需要导出到协议层
 */
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class Export(
    /**
     * 指定导出到协议层类名
     */
    val value: String = ""
)