package cn.wzbos.android.rudolph

import android.content.Context
import cn.wzbos.android.rudolph.router.Router

/**
 * Created by wuzongbo on 2017/4/11.
 */
interface Interceptor {
    fun intercept(context: Context?, router: Router<*>): Boolean
}