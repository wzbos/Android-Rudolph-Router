package cn.wzbos.android.rudolph.logger

import android.util.Log
import cn.wzbos.android.rudolph.Rudolph

/**
 * Rudolph Log
 * Created by wuzongbo on 20/08/26.
 */
object RLog {
    fun v(tag: String?, message: String) {
        if (Rudolph.logger != null) {
            Rudolph.logger?.v(tag, message)
        } else {
            Log.v(tag, message)
        }
    }

    fun d(tag: String?, message: String) {
        if (Rudolph.logger != null) {
            Rudolph.logger?.d(tag, message)
        } else {
            Log.d(tag, message)
        }
    }

    fun i(tag: String?, message: String) {
        if (Rudolph.logger != null) {
            Rudolph.logger?.i(tag, message)
        } else {
            Log.i(tag, message)
        }
    }

    fun w(tag: String?, message: String) {
        if (Rudolph.logger != null) {
            Rudolph.logger?.w(tag, message)
        } else {
            Log.w(tag, message)
        }
    }

    fun e(tag: String?, message: String) {
        if (Rudolph.logger != null) {
            Rudolph.logger?.e(tag, message)
        } else {
            Log.e(tag, message)
        }
    }

    fun e(tag: String?, throwable: Throwable?) {
        if (Rudolph.logger != null) {
            Rudolph.logger?.e(tag, throwable)
        } else {
            Log.e(tag, "", throwable)
        }
    }

    fun e(tag: String?, message: String?, throwable: Throwable?) {
        if (Rudolph.logger != null) {
            Rudolph.logger!!.e(tag, throwable)
        } else {
            Log.e(tag, message, throwable)
        }
    }
}