package cn.wzbos.android.rudolph.router;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;

import cn.wzbos.android.rudolph.Interceptor;
import cn.wzbos.android.rudolph.OnRouteListener;
import cn.wzbos.android.rudolph.RouteType;
import cn.wzbos.android.rudolph.Rudolph;

public abstract class Router<T> {

    String rawUrl;
    Bundle bundle;
    Class<?> target;
    OnRouteListener callback;
    String routePath;
    RouteType routeType;
    String routeTag;
    Map<String, String> queryParameters;

    public String getRawUrl() {
        return rawUrl;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public Class<?> getTarget() {
        return target;
    }

    public String getRoutePath() {
        return routePath;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public String getRouteTag() {
        return routeTag;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public Uri getUriData() {
        if (rawUrl.contains("://")) {
            return Uri.parse(rawUrl);
        }

        if(!TextUtils.isEmpty(Rudolph.getScheme())){
            return Uri.parse(Rudolph.getScheme() + "://" + rawUrl);
        }

        return Uri.EMPTY;
    }

    Router(RouteBuilder<?,?> builder) {
        this.rawUrl = builder.rawUrl;
        this.callback = builder.callback;
        this.bundle = builder.args;
        this.target = builder.target;
        this.routePath = builder.routePath;
        this.routeType = builder.routeType;
        this.routeTag = builder.routeTag;
        this.queryParameters = builder.queryParameters;
    }

    boolean intercept(Context context) {

        List<Interceptor> interceptors = Rudolph.getInterceptors();
        if (null != interceptors && interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                if (interceptor.intercept(context, this)) {
                    return true;
                }
            }
        }

        return false;
    }

    public abstract T open();
}
