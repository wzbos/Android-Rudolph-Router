package cn.wzbos.android.rudolph.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 错误码
 */
@IntDef({
        ErrorCode.NOT_FOUND,
        ErrorCode.METHOD_INVOKE_FAILED,
        ErrorCode.SERVICE_CREATE_FAILED,
        ErrorCode.FRAGMENT_CREATE_FAILED
})
@Retention(RetentionPolicy.SOURCE)
public @interface ErrorCode {
    /**
     * 未匹配到路由，路由地址不存在
     */
    int NOT_FOUND = 404;
    /**
     * 方法执行失败
     */
    int METHOD_INVOKE_FAILED = 2000;
    /**
     * 服务创建失败
     */
    int SERVICE_CREATE_FAILED = 3000;
    /**
     * FRAGMENT创建失败
     */
    int FRAGMENT_CREATE_FAILED = 4000;
}


