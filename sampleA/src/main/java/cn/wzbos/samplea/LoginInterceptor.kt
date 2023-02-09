package cn.wzbos.samplea

import cn.wzbos.android.rudolph.RouteInterceptor

class LoginInterceptor : RouteInterceptor {

    override fun intercept(): Boolean {
        return false
    }
}