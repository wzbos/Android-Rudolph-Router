package cn.wzbos.android.rudolph;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cn.wzbos.android.rudolph.annotations.Extra;
import cn.wzbos.android.rudolph.router.UriRouter;

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
    private static Boolean initialized = false;
    static ILogger logger;

    public static Application getApplication() {
        return app;
    }

    public static String getScheme() {
        return scheme;
    }


    /**
     * Rudolph 初始化,建议方法application中
     *
     * @param application Application
     */
    public static void init(Application application) {
        RLog.v(TAG, "init");
        if (initialized)
            return;

        app = application;
        AssetManager assetManager = application.getResources().getAssets();
        try {
            String[] list = assetManager.list("rudolph");
            if (null != list && list.length > 0) {
                for (String className : list) {
                    try {
                        Class<?> clazz = Class.forName("cn.wzbos.android.rudolph.routes." + className);
                        if (IRouteTable.class.isAssignableFrom(clazz)) {
                            IRouteTable iGroupInstance = (IRouteTable) clazz.newInstance();
                            iGroupInstance.init(application);
                        }
                    } catch (ClassNotFoundException e) {
                        RLog.e(TAG, "初始化\"" + className + "\"组件失败，请检查包名是否正确！", e);
                    }
                }
            }
            initialized = true;
        } catch (Exception e) {
            RLog.e(TAG, "路由初始化异常！", e);
        }
    }

    /**
     * 设置日志
     *
     * @param iLogger ILogger
     */
    public static void setLogger(ILogger iLogger) {
        logger = iLogger;
    }

    /**
     * 获取是否已初始化路由表
     */
    public static Boolean isInitialized() {
        return initialized;
    }

    /**
     * 设置scheme
     *
     * @param schemeStr scheme
     */
    public static void setScheme(String schemeStr) {
        scheme = schemeStr;
    }

    public static void addRoute(RouteInfo routeInfo) {
        routes.add(routeInfo);
    }

    /**
     * 获取所有拦截器
     *
     * @return Interceptor集合
     */
    public static List<Interceptor> getInterceptors() {
        return mInterceptors;
    }

    /**
     * 添加拦截器
     *
     * @param interceptor 拦截器
     */
    public static void addInterceptor(Interceptor interceptor) {
        if (null == mInterceptors) {
            mInterceptors = new ArrayList<>();
        }
        mInterceptors.add(interceptor);
    }


    /**
     * 移除拦截器
     *
     * @param interceptor 通过 addInterceptor 添加的拦截器
     */
    public static void removeInterceptor(Interceptor interceptor) {
        if (null != mInterceptors) {
            mInterceptors.remove(interceptor);
        }
    }

    /**
     * 获取已加载所有路由信息
     *
     * @return RouteInfo 集合
     */
    public static List<RouteInfo> getRouters() {
        return routes;
    }

    /**
     * 根据路由地址获取一个路由信息
     *
     * @param path 路由地址（如：/user/info 或 demo://my.app.com/user/info）
     * @return RouteInfo
     */
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
     * 将 @Extra 注解的字段赋值
     *
     * @param activity Activity
     * @see Extra
     */
    public static void bind(Activity activity) {
        RouteBinder.getInstance().bind(activity, activity.getIntent().getExtras());
    }

    /**
     * 将 @Extra 注解的字段重新赋值（对应Activity的onNewIntent生命周期）
     *
     * @param activity Activity
     * @param intent   Intent
     * @see Extra
     */
    public static void onNewIntent(Activity activity, Intent intent) {
        RouteBinder.getInstance().bind(activity, intent.getExtras());
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param fragment Fragment
     * @see Extra
     */
    public static void bind(Fragment fragment) {
        RouteBinder.getInstance().bind(fragment, fragment.getArguments());
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param fragment Fragment
     * @see Extra
     */
    public static void bind(android.support.v4.app.Fragment fragment) {
        RouteBinder.getInstance().bind(fragment, fragment.getArguments());
    }

    /**
     * 将 @Extra 注解的字段赋值
     *
     * @param service IRouteService
     * @param bundle  Bundle
     * @see Extra
     */
    public static void bind(IRouteService service, Bundle bundle) {
        RouteBinder.getInstance().bind(service, bundle);
    }


    /**
     * 通过路由地址获取一个路由构造器
     *
     * @param url 路由地址
     * @return UriRouter.Builder
     */
    public static UriRouter.Builder<?> builder(@NonNull Uri url) {
        return builder(url.toString());
    }

    /**
     * 通过路由地址获取一个路由构造器
     *
     * @param url 路由地址
     * @return UriRouter.Builder
     */
    public static UriRouter.Builder<?> builder(@NonNull String url) {
        return new UriRouter.Builder<>(url);
    }
}
