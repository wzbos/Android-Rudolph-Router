package cn.wzbos.android.rudolph.router;

import android.util.Log;

import cn.wzbos.android.rudolph.exception.ErrorCode;
import cn.wzbos.android.rudolph.exception.ErrorMessage;
import cn.wzbos.android.rudolph.exception.RudolphException;

public class FragmentRouter<R> extends Router<R> {

    FragmentRouter(RouteBuilder<?, ?> builder) {
        super(builder);
    }

    private FragmentRouter(Builder<?, ?> builder) {
        super(builder);
    }

    @Override
    public R open() {
        if (super.intercept(null))
            return null;

        if (target == null) {
            if (callback != null)
                callback.onError(this, new RudolphException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND_ERROR));
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
                callback.onSuccess(this);

            return (R) instance;
        } catch (Exception e) {
            if (callback != null) {
                callback.onError(this, new RudolphException(ErrorCode.FRAGMENT_CREATE_FAILED, "Fragment 创建失败！", e));
            } else {
                Log.e("rudolph", "Fragment 创建失败！", e);
            }
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
