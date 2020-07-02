package cn.wzbos.android.rudolph.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Route {

    /**
     * 当前路由地址
     */
    String value() default "";

    /**
     * 当前路由自定义标签
     */
    String tag() default "";

    /**
     * 当前路由实现的接口类或继承的父类（仅在class注解上生效）
     */
    Class<?> clazz() default Object.class;

    /**
     * 指定当前类是否为单例模式（仅在class注解上生效）
     *
     * @return true:单例，false:非单例
     */
    boolean singleton() default false;


}
