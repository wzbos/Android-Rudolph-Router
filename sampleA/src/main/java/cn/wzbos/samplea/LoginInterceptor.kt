package cn.wzbos.samplea

import cn.wzbos.android.rudolph.IRouter
import cn.wzbos.android.rudolph.RouteInterceptor

class LoginInterceptor : RouteInterceptor {

    override fun intercept(router: IRouter<*,*,*>): Boolean {
        return false
    }
}