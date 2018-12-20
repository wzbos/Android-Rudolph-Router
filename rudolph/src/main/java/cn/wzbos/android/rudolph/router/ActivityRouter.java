package cn.wzbos.android.rudolph.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.exception.RudolphException;

public class ActivityRouter extends Router {
    private Bundle options;
    private int flags = -1;
    private int enterAnim = -1;
    private int exitAnim = -1;

    public Bundle getOptions() {
        return options;
    }

    public int getFlags() {
        return flags;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }


    ActivityRouter(RouteBuilder builder) {
        super(builder);
    }


    private ActivityRouter(Builder builder) {
        super(builder);
        this.options = builder.options;
        this.flags = builder.flags;
        this.enterAnim = builder.enterAnim;
        this.exitAnim = builder.exitAnim;
    }

    private Intent getIntent(Context context) {
        Intent intent;
        if (target == null) {
            Uri data = getUriData();
            if (data == Uri.EMPTY) {
                if (callback != null)
                    callback.onFailed(new RudolphException("Not found!"));
                return null;
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(data);
            }
        } else {
            intent = new Intent(context, target);
        }

        if (-1 != flags)
            intent.setFlags(flags);

        if (context == context.getApplicationContext()) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (null != bundle)
            intent.putExtras(bundle);

        return intent;
    }


    @Override
    public Object open() {
        start(Rudolph.getApplication());
        return null;
    }

    public void start(Context context) {
        if (super.intercept(context))
            return;

        Intent intent = getIntent(context);
        if (null == intent)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent, options);
        } else {
            context.startActivity(intent);
        }
        startOver(context);
    }

    public void start(android.app.Fragment fragment) {
        start(fragment.getActivity());
    }

    public void start(android.support.v4.app.Fragment fragmentV4) {
        start(fragmentV4.getContext());
    }

    /**
     * startForResult for android.app.Fragment
     */
    public void startForResult(android.app.Fragment fragment, int requestCode) {
        if (super.intercept(fragment.getActivity()))
            return;
        Intent intent = getIntent(fragment.getActivity());
        if (null == intent)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            fragment.startActivityForResult(intent, requestCode, options);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
        startOver(fragment.getActivity());
    }


    /**
     * startForResult for android.support.v4.app.Fragment
     */
    public void startForResult(android.support.v4.app.Fragment fragmentV4, int requestCode) {
        if (super.intercept(fragmentV4.getContext()))
            return;
        Intent intent = getIntent(fragmentV4.getContext());
        if (null == intent)
            return;
        fragmentV4.startActivityForResult(intent, requestCode);
        startOver(fragmentV4.getActivity());
    }


    /**
     * startForResult for Activity
     */
    public void startForResult(Activity activity, int requestCode) {
        if (super.intercept(activity))
            return;
        Intent intent = getIntent(activity);
        if (null == intent)
            return;
        activity.startActivityForResult(intent, requestCode, options);
        startOver(activity);
    }

    private void startOver(Context context) {
        if (-1 != enterAnim && -1 != exitAnim && context instanceof Activity) {
            ((Activity) context).overridePendingTransition(enterAnim, exitAnim);
        }

        if (callback != null)
            callback.onSucceed();
    }

    /**
     * ActivityRouter Builder
     */
    public static class Builder<T extends Builder<T>> extends RouteBuilder<Builder<T>, ActivityRouter> {
        Bundle options;
        int flags = -1;

        int enterAnim;
        int exitAnim;

        public Builder(Class<?> cls) {
            super(cls);
        }

        public Builder(String url) {
            super(url);
        }

        public T flags(int flag) {
            this.flags = flag;
            return (T) this;
        }

        public T options(Bundle options) {
            this.options = options;
            return (T) this;
        }

        public T transition(int enterAnim, int exitAnim) {
            this.enterAnim = enterAnim;
            this.exitAnim = exitAnim;
            return (T) this;
        }

        @Override
        public ActivityRouter build() {
            return new ActivityRouter(this);
        }
    }
}
