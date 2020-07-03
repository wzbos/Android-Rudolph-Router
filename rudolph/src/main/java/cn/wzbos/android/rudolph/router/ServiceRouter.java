package cn.wzbos.android.rudolph.router;


import android.util.Log;

import java.lang.reflect.Constructor;

import cn.wzbos.android.rudolph.IRouteService;
import cn.wzbos.android.rudolph.exception.ErrorCode;
import cn.wzbos.android.rudolph.exception.ErrorMessage;
import cn.wzbos.android.rudolph.exception.RudolphException;

public class ServiceRouter<R> extends Router<R> {

    ServiceRouter(RouteBuilder<?, ?> builder) {
        super(builder);
    }

    protected ServiceRouter(Builder<?, ? extends IRouteService> builder) {
        super(builder);
    }


    @Override
    @SuppressWarnings("unchecked")
    public R open() {
        if (super.intercept(null))
            return null;

        if (target == null) {
            if (callback != null)
                callback.onError(this, new RudolphException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND_ERROR));
            return null;
        }

        try {
            Constructor<?> constructor = target.getConstructor();
            if (constructor.getParameterTypes().length > 0) {
                if (callback != null)
                    callback.onError(this, new RudolphException(ErrorCode.SERVICE_CREATE_FAILED, "创建服务失败,请提供一个无参数构造方法！"));
            }

            if (!constructor.isAccessible()) {
                Log.w("rudolph", "警告：构造方法 " + target.getName() + " 为私有!");
                constructor.setAccessible(true);
            }
            Object instance = constructor.newInstance();
            if (instance instanceof IRouteService) {
                IRouteService component = ((IRouteService) instance);
                component.init(this.bundle);
                if (callback != null)
                    callback.onSuccess(this);
                return (R) component;
            }
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(this, new RudolphException(ErrorCode.SERVICE_CREATE_FAILED, "创建服务失败！", e));
            } else {
                Log.e("rudolph", "创建服务失败!");
            }
        }
        return null;
    }


    public static class Builder<T extends Builder<T, R>, R extends IRouteService> extends RouteBuilder<Builder<T, R>, ServiceRouter<R>> {

        public Builder(Class<?> target) {
            super(target);
        }

        public Builder(String path) {
            super(path);
        }

        @Override
        public ServiceRouter<R> build() {
            return new ServiceRouter<>(this);
        }
    }
}
