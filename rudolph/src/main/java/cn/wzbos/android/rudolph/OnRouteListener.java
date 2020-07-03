package cn.wzbos.android.rudolph;

import cn.wzbos.android.rudolph.router.Router;

/**
 * Created by wuzongbo on 2017/4/12.
 */
public interface OnRouteListener {

    void onSuccess(Router<?> router);

    void onError(Router<?> router, Throwable throwable);
}