package cn.wzbos.android.rudolph

import android.content.Context

/**
 * 路由表接口
 */
interface IRouteComponent {
    /**
     * 初始化组件
     *
     * @param context Context
     */
    fun init(context: Context)
}