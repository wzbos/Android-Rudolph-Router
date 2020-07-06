package cn.wzbos.android.rudolph

import cn.wzbos.android.rudolph.router.Router

/**
 * Created by wuzongbo on 2017/4/12.
 */
interface OnRouteListener {
    fun onSuccess(router: Router<*>?)
    fun onError(router: Router<*>?, throwable: Throwable?)
}