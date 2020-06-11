package cn.wzbos.android.rudolph.router;


import android.util.Log;

import java.lang.reflect.Constructor;

import cn.wzbos.android.rudolph.IRouteService;

public class ServiceRouter<R extends IRouteService> extends Router<R> {

    ServiceRouter(RouteBuilder builder) {
        super(builder);
    }

    protected ServiceRouter(Builder builder) {
        super(builder);
    }


    @Override
    public R open() {
        if (super.intercept(null))
            return null;

        try {
            Constructor<?> constructor = target.getConstructor();
            if (!constructor.isAccessible()) {
                Log.w("rudolph", target.getName() + " constructor method is private!");
                constructor.setAccessible(true);
            }
            Object instance = constructor.newInstance();
            if (instance instanceof IRouteService) {
                IRouteService component = ((IRouteService) instance);
                component.init(this.bundle);
                if (callback != null)
                    callback.onSucceed();
                return (R) component;
            }
        } catch (Exception e) {
            Log.e("rudolph", "open service failed!");
            if (callback != null)
                callback.onFailed(e);
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
