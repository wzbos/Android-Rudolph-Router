package cn.wzbos.android.rudolph.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface Export {
    /**
     * export .class name
     */
    String value() default "";

    /**
     * export singleton class
     */
    boolean singleton() default false;
}
