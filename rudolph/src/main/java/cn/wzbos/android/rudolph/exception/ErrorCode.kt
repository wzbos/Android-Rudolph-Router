package cn.wzbos.android.rudolph.exception

import androidx.annotation.IntDef


/**
 * 错误码
 */
@IntDef(ErrorCode.NOT_FOUND, ErrorCode.METHOD_INVOKE_FAILED, ErrorCode.SERVICE_CREATE_FAILED, ErrorCode.FRAGMENT_CREATE_FAILED)
@Retention(AnnotationRetention.SOURCE)
annotation class ErrorCode {
    companion object {
        /**
         * 未匹配到路由，路由地址不存在
         */
        const val NOT_FOUND = 404

        /**
         * 方法执行失败
         */
        const val METHOD_INVOKE_FAILED = 2000

        /**
         * 服务创建失败
         */
        const val SERVICE_CREATE_FAILED = 3000

        /**
         * FRAGMENT创建失败
         */
        const val FRAGMENT_CREATE_FAILED = 4000
    }
}