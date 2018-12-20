package cn.wzbos.android.rudolph.router;

import cn.wzbos.android.rudolph.exception.RudolphException;

public class FragmentRouter<R> extends Router<R> {

    FragmentRouter(RouteBuilder builder) {
        super(builder);
    }

    private FragmentRouter(Builder builder) {
        super(builder);
    }

    public R open() {
        if (super.intercept(null))
            return null;

        if (target == null) {
            if (callback != null)
                callback.onFailed(new RudolphException("Not found!"));
            return null;
        }
        try {
            Object instance = target.getConstructor().newInstance();
            if (instance instanceof android.app.Fragment) {
                ((android.app.Fragment) instance).setArguments(this.bundle);
            } else if (instance instanceof android.support.v4.app.Fragment) {
                ((android.support.v4.app.Fragment) instance).setArguments(this.bundle);
            }

            if (callback != null)
                callback.onSucceed();

            return (R) instance;
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null)
                callback.onFailed(e);
        }
        return null;
    }

    public static class Builder<T, R> extends RouteBuilder<Builder<T, R>, FragmentRouter<R>> {

        public Builder(Class<?> cls) {
            super(cls);
        }

        public Builder(String path) {
            super(path);
        }


        @Override
        public FragmentRouter<R> build() {
            return new FragmentRouter<>(this);
        }
    }
}
