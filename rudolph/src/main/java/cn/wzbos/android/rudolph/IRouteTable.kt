package cn.wzbos.android.rudolph

import android.app.Application

/**
 * 路由表接口
 */
interface IRouteTable {
    /**
     * 初始化方法
     *
     * @param application Application
     */
    fun init(application: Application?)
}