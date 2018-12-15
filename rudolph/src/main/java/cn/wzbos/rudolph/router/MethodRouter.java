package cn.wzbos.rudolph.router;

import android.app.Application;
import android.content.Context;

import cn.wzbos.rudolph.Rudolph;
import cn.wzbos.rudolph.Consts;
import cn.wzbos.rudolph.annotations.Route;
import cn.wzbos.rudolph.utils.TypeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodRouter extends Router {

    private Map<String, String> uriKvs;

    MethodRouter(UriRouter.Builder builder) {
        super(builder);
        this.uriKvs = builder.getUriAllParams();
    }

    public Object open() {
        return open(null);
    }

    public Object open(Context context) {
        if (super.intercept(context))
            return null;

        try {
            Method[] methods = this.target.getMethods();

            for (Method method : methods) {

                Route route = method.getAnnotation(Route.class);
                if (route != null && route.value().equalsIgnoreCase(this.routePath)) {
                    List<Object> values = new ArrayList<>();

                    Map<String, Type> params = this.queryParameters;

                    if (null != params && params.size() > 0) {

                        for (Map.Entry<String, Type> kv : params.entrySet()) {
                            String argName = kv.getKey();
                            Type argType = kv.getValue();
                            Object argValue = null;
                            if (Consts.RAW_URI.equals(argName)) {
                                argValue = rawUrl;
                            } else {
                                if (kv.getValue() == Application.class) {
                                    argValue = Rudolph.getApplication();
                                } else if (kv.getValue() == Context.class) {
                                    argValue = (null != context) ? context : Rudolph.getApplication();
                                } else {
                                    for (Map.Entry<String, String> uriKv : uriKvs.entrySet()) {
                                        if (argName.equalsIgnoreCase(uriKv.getKey())) {
                                            argValue = TypeUtils.getObject(context, argName, uriKv.getValue(), argType);
                                            break;
                                        }
                                    }
                                }
                            }

                            values.add(argValue);
                        }
                    }

                    Object obj = method.invoke(null, values.toArray());

                    if (null != callback)
                        callback.onSucceed();

                    return obj;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (null != callback)
                callback.onFailed(e);
        }
        return null;
    }

}
