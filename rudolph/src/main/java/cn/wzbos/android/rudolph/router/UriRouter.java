package cn.wzbos.android.rudolph.router;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;


public class UriRouter extends Router<Object> {

    private Router<Object> router;

    private UriRouter(Builder<?> builder) {
        super(builder);
        if (null != routeType) {
            switch (routeType) {
                case ACTIVITY:
                    router = new ActivityRouter(builder);
                    break;
                case FRAGMENT:
                case FRAGMENT_V4:
                case FRAGMENT_AndroidX:
                    router = new FragmentRouter<>(builder);
                    break;
                case SERVICE:
                    router = new ServiceRouter<>(builder);
                    break;
                case METHOD:
                    router = new MethodRouter(builder);
                    break;
            }
        } else {
            Uri data = getUriData();
            if (data != Uri.EMPTY) {
                router = new ActivityRouter(builder);
            }
        }
    }

    /**
     * open route,support route type: fragment/fragmentV4、provider,method
     *
     * @return return route result.
     */
    public Object open() {
        if (null == router) {
            return null;
        }
        return router.open();
    }

    /**
     * open route,support all route type
     *
     * @return return route result.
     * Activity return null
     * Fragment return a new instance
     * Provide return a IRouteService instance
     * Method return result
     */
    public Object open(Context context) {
        if (null == router)
            return null;

        if (router instanceof ActivityRouter) {
            ((ActivityRouter) router).start(context);
            return null;
        } else if (router instanceof MethodRouter) {
            return ((MethodRouter) router).open(context);
        } else {
            return router.open();
        }
    }

    /**
     * open route
     *
     * @param fragment fragment
     */
    public Object open(android.app.Fragment fragment) {
        return open(fragment.getActivity());
    }

    /**
     * open route
     *
     * @param fragmentV4 fragmentV4
     */
    public Object open(android.support.v4.app.Fragment fragmentV4) {
        return open(fragmentV4.getContext());
    }

    /**
     * start Activity for fragment
     *
     * @param fragment    fragment
     * @param requestCode requestCode
     */
    public void open(android.app.Fragment fragment, int requestCode) {
        if (null == router)
            return;

        if (router instanceof ActivityRouter) {
            ((ActivityRouter) router).startForResult(fragment, requestCode);
        } else {
            open(fragment.getActivity());
        }
    }

    /**
     * start Activity for fragmentV4
     *
     * @param fragmentV4  fragmentV4
     * @param requestCode requestCode
     */
    public void open(android.support.v4.app.Fragment fragmentV4, int requestCode) {
        if (null == router)
            return;
        if (router instanceof ActivityRouter) {
            ((ActivityRouter) router).startForResult(fragmentV4, requestCode);
        } else {
            open(fragmentV4.getActivity());
        }
    }

    /**
     * start Activity for activity
     *
     * @param activity    activity
     * @param requestCode requestCode
     */
    public void open(Activity activity, int requestCode) {
        if (null == router)
            return;
        if (router instanceof ActivityRouter) {
            ((ActivityRouter) router).startForResult(activity, requestCode);
        } else {
            open(activity);
        }
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends Builder<T>> extends RouteBuilder<Builder<T>, UriRouter> {
        Bundle options;
        int flags = -1;

        int enterAnim;
        int exitAnim;

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
        public UriRouter build() {
            return new UriRouter(this);
        }
    }
}