package cn.wzbos.android.rudolph

import android.os.Bundle
import android.util.Base64
import com.google.gson.Gson
import java.lang.reflect.Type

interface IRouteBinder {
    fun bind(target: Any?, bundle: Bundle?)

    @JvmDefault
    fun getBase64(bundle: Bundle, key: String, default: String): String {
        bundle.getString(key)?.let { base64 ->
            try {
                return String(
                    Base64.decode(
                        base64.toByteArray(),
                        Base64.NO_PADDING or Base64.URL_SAFE
                    )
                )
            } catch (e: Exception) {
                return default
            }
        }
        return default
    }

    @JvmDefault
    fun <T> getBase64Json(bundle: Bundle, key: String, default: T, cls: Type): T {
        bundle.getString(key)?.let { base64 ->
            try {
                val json = String(
                    Base64.decode(
                        base64.toByteArray(),
                        Base64.NO_PADDING or Base64.URL_SAFE
                    )
                )
                return Gson().fromJson(json, cls)
            } catch (e: Exception) {
                return default
            }
        }
        return default
    }

    @JvmDefault
    fun <T> getJson(bundle: Bundle, key: String, default: T, cls: Type): T {
        bundle.getString(key)?.let { json ->
            try {
                return Gson().fromJson(json, cls)
            } catch (e: Exception) {
                return default
            }
        }
        return default
    }
}