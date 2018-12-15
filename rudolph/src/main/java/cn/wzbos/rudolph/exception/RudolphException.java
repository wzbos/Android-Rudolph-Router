package cn.wzbos.rudolph.exception;

/**
 * Created by wuzongbo on 2017/4/19.
 */

public class RudolphException extends RuntimeException {
    public RudolphException(String message) {
        super(message);
    }

    public RudolphException(String message, Throwable cause) {
        super(message, cause);
    }
}
