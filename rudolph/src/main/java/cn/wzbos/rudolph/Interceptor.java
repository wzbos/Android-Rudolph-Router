package cn.wzbos.rudolph;


import android.content.Context;

import cn.wzbos.rudolph.router.Router;


/**
 * Created by wuzongbo on 2017/4/11.
 */
public interface Interceptor {
    boolean intercept(Context context, Router router);
}
