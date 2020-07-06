package cn.wzbos.android.rudolph

import android.os.Bundle
import android.util.Log
import android.util.LruCache

/**
 * Created by wuzongbo on 2017/6/4.
 */
internal class RouteBinder private constructor() {
    private val classCache: LruCache<String, IRouteBinder?> = LruCache(50)
    fun bind(instance: Any, bundle: Bundle?) {
        val className = instance.javaClass.name
        try {
            var binder = classCache[className]
            if (null == binder) {
                val clsName = instance.javaClass.name + "Binder"
                binder = Class.forName(clsName).getConstructor().newInstance() as IRouteBinder
            }
            binder.bind(instance, bundle)
            classCache.put(className, binder)
        } catch (ex: Exception) {
            RLog.e("RouteBinder", "Unable to find binder for $className")
            ex.printStackTrace()
        }
    }

    companion object {
        @Volatile
        var instance: RouteBinder? = null
            get() {
                if (field == null) {
                    synchronized(RouteBinder::class.java) {
                        if (field == null) {
                            field = RouteBinder()
                        }
                    }
                }
                return field
            }
            private set
    }

}