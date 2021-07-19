package cn.wzbos.android.rudolph.annotations

import kotlin.reflect.KClass

@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Route(
    /**
     * 当前路由地址
     */
    val value: String = "",

    /**
     * 当前路由自定义标签
     */
    val tag: String = "",

    /**
     * 当前路由实现的接口类或继承的父类（仅在class注解上生效）
     */
    val clazz: KClass<*> = Any::class,

    /**
     * 指定当前类是否为单例模式（仅在class注解上生效）
     *
     * @return true:单例，false:非单例
     */
    val singleton: Boolean = false
)