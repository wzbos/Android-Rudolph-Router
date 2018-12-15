package cn.wzbos.rudolph;

import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;


/**
 * Created by wuzongbo on 2017/6/4.
 */
class RouteBinder {
    private volatile static RouteBinder instance = null;

    static RouteBinder getInstance() {
        if (instance == null) {
            synchronized (RouteBinder.class) {
                if (instance == null) {
                    instance = new RouteBinder();
                }
            }
        }
        return instance;
    }

    private LruCache<String, IRouteBinder> classCache;

    private RouteBinder() {
        classCache = new LruCache<>(50);
    }

    void bind(Object instance, Bundle bundle) {
        String className = instance.getClass().getName();
        try {
            IRouteBinder binder = classCache.get(className);
            if (null == binder) {
                String clsName = instance.getClass().getName() + "Binder";
                binder = (IRouteBinder) Class.forName(clsName).getConstructor().newInstance();
            }

            binder.bind(instance, bundle);
            classCache.put(className, binder);
        } catch (Exception ex) {
            Log.e("Rudolph", "Unable to find binder for " + className);
            ex.printStackTrace();
        }
    }
}
