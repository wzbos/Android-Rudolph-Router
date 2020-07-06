package cn.wzbos.android.rudolph.exception

/**
 * Created by wuzongbo on 2017/4/19.
 */
class RudolphException : RuntimeException {
    constructor(message: String?) : super(message)

    /**
     * 获取错误码
     *
     * @return 错误码
     * @see ErrorCode
     */
    var code = 0

    constructor(@ErrorCode code: Int, message: String?) : super(message) {
        this.code = code
    }

    constructor(@ErrorCode code: Int, message: String?, cause: Throwable?) : super(message, cause) {
        this.code = code
    }
}