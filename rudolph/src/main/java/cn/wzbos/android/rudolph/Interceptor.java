package cn.wzbos.android.rudolph;


import android.content.Context;

import cn.wzbos.android.rudolph.router.Router;


/**
 * Created by wuzongbo on 2017/4/11.
 */
public interface Interceptor {
    boolean intercept(Context context, Router<?> router);
}
