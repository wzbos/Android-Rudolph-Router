package cn.wzbos.android.rudolph.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wuzongbo on 2017/5/30.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Arg {
    String value() default "";

    boolean export() default true;

    boolean json() default false;

    boolean base64() default false;
}
