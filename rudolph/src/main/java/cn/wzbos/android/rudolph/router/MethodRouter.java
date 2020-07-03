package cn.wzbos.android.rudolph.router;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.Consts;
import cn.wzbos.android.rudolph.annotations.Route;
import cn.wzbos.android.rudolph.exception.ErrorCode;
import cn.wzbos.android.rudolph.exception.ErrorMessage;
import cn.wzbos.android.rudolph.exception.RudolphException;
import cn.wzbos.android.rudolph.utils.TypeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodRouter extends Router<Object> {

    private Map<String, String> uriKvs;

    MethodRouter(UriRouter.Builder<?> builder) {
        super(builder);
        this.uriKvs = builder.getUriAllParams();
    }

    public Object open() {
        return open(null);
    }

    public Object open(Context context) {
        if (super.intercept(context))
            return null;

        if (target == null) {
            if (callback != null)
                callback.onError(this, new RudolphException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND_ERROR));
            return null;
        }

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
                        callback.onSuccess(this);

                    return obj;
                }
            }
        } catch (Exception e) {
            if (null != callback) {
                callback.onError(this, new RudolphException(ErrorCode.METHOD_INVOKE_FAILED, "方法调用异常!", e));
            } else {
                Log.e("rudolph", "方法调用异常!", e);
            }
        }
        return null;
    }

}
