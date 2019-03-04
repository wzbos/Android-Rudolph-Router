package cn.wzbos.android.rudolph;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import cn.wzbos.android.rudolph.router.UriRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * Rudolph
 * Created by wuzongbo on 17/4/10.
 */
public class Rudolph {

    private final static String TAG = "Rudolph";
    private static List<Interceptor> mInterceptors;
    private static List<RouteInfo> routes = new ArrayList<>();
    private static Application app;
    private static String scheme;

    public static Application getApplication() {
        return app;
    }

    public static String getScheme() {
        return scheme;
    }

    /**
     * 初始化
     */
    public static void init(Application application) {
        Log.v(TAG, "init");
        app = application;
        AssetManager assetManager = application.getResources().getAssets();
        try {
            String[] list = assetManager.list("rudolph");
            if (null != list && list.length > 0) {
                for (String className : list) {
                    try {
                        Class clazz = Class.forName("cn.wzbos.android.rudolph.routes." + className);
                        if (IRouteTable.class.isAssignableFrom(clazz)) {
                            IRouteTable iGroupInstance = (IRouteTable) clazz.newInstance();
                            iGroupInstance.init(application);
                        }
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "初始化\"" + className + "\"组件失败，请检查包名是否正确！");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setScheme(String schemeStr) {
        scheme = schemeStr;
    }

    public static void addRoute(cn.wzbos.android.rudolph.RouteInfo routeInfo) {
        routes.add(routeInfo);
    }

    public static List<Interceptor> getInterceptors() {
        return mInterceptors;
    }

    /**
     * 添加拦截器
     */
    public static void addInterceptor(Interceptor interceptor) {
        if (null == mInterceptors) {
            mInterceptors = new ArrayList<>();
        }
        mInterceptors.add(interceptor);
    }

    /**
     * 移除拦截器
     */
    public static void removeInterceptor(Interceptor interceptor) {
        if (null != mInterceptors) {
            mInterceptors.remove(interceptor);
        }
    }

    public static RouteInfo getRouter(String path) {
        RouteInfo meta;
        int count = routes.size();
        for (int i = 0; i < count; i++) {
            meta = routes.get(i);
            if (meta.equals(path)) {
                return meta;
            }
        }
        return null;
    }

    /**
     * bind activity
     */
    public static void bind(Activity activity) {
        RouteBinder.getInstance().bind(activity, activity.getIntent().getExtras());
    }

    /**
     * onNewIntent activity
     */
    public static void onNewIntent(Activity activity, Intent intent) {
        RouteBinder.getInstance().bind(activity, intent.getExtras());
    }

    /**
     * bind android.app.Fragment
     */
    public static void bind(Fragment fragment) {
        RouteBinder.getInstance().bind(fragment, fragment.getArguments());
    }

    /**
     * bind android.support.v4.app.Fragment
     */
    public static void bind(android.support.v4.app.Fragment fragment) {
        RouteBinder.getInstance().bind(fragment, fragment.getArguments());
    }

    /**
     * bind service
     */
    public static void bind(IRouteService service, Bundle bundle) {
        RouteBinder.getInstance().bind(service, bundle);
    }


    public static UriRouter.Builder builder(@NonNull Uri url) {
        return builder(url.toString());
    }

    public static UriRouter.Builder builder(@NonNull String url) {
        return new UriRouter.Builder(url);
    }


}
