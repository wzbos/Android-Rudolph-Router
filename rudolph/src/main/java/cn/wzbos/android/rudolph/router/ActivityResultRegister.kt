package cn.wzbos.android.rudolph.router

import android.content.Intent
import java.util.concurrent.atomic.AtomicInteger

object ActivityResultRegister {
    private val mNextLocalRequestCode = AtomicInteger()
    private var callbacks = mutableMapOf<Int, ActivityResultCallback>()

    fun register(callback: ActivityResultCallback): Int {
        val requestCode = mNextLocalRequestCode.getAndIncrement()
        callbacks[requestCode] = callback
        return requestCode
    }

    fun remove(requestCode: Int) {
        callbacks.remove(requestCode)
    }

    fun dispatchResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        callbacks.remove(requestCode)?.let {
            it.onActivityResult(resultCode, data)
            return true
        }
        return false
    }
}

interface ActivityResultCallback {
    fun onActivityResult(resultCode: Int, intent: Intent?)
}
