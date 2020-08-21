package cn.wzbos.android.rudolph;

interface ILogger {
    void v(String tag, String message);

    void d(String tag, String message);

    void i(String tag, String message);

    void w(String tag, String message);

    void e(String tag, String message);

    void e(String tag, Throwable throwable);

    void e(String tag, String message, Throwable throwable);
}
