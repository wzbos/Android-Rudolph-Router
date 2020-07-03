package cn.wzbos.android.rudolph.exception;

/**
 * Created by wuzongbo on 2017/4/19.
 */
public class RudolphException extends RuntimeException {
    public RudolphException(String message) {
        super(message);
    }

    private int code;

    /**
     * 获取错误码
     *
     * @return 错误码
     * @see ErrorCode
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RudolphException(@ErrorCode int code, String message) {
        super(message);
        this.code = code;
    }

    public RudolphException(@ErrorCode int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
