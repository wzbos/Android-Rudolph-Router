package cn.wzbos.android.rudolph;

import android.util.Log;

/**
 * Rudolph Log
 * Created by wuzongbo on 20/08/26.
 */
public class RLog {
    public static void v(String tag, String message) {
        if (Rudolph.logger != null) {
            Rudolph.logger.v(tag, message);
        } else {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (Rudolph.logger != null) {
            Rudolph.logger.d(tag, message);
        } else {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (Rudolph.logger != null) {
            Rudolph.logger.i(tag, message);
        } else {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (Rudolph.logger != null) {
            Rudolph.logger.w(tag, message);
        } else {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (Rudolph.logger != null) {
            Rudolph.logger.e(tag, message);
        } else {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (Rudolph.logger != null) {
            Rudolph.logger.e(tag, throwable);
        } else {
            Log.e(tag, "", throwable);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (Rudolph.logger != null) {
            Rudolph.logger.e(tag, throwable);
        } else {
            Log.e(tag, message, throwable);
        }
    }
}
