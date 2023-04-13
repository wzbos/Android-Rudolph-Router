package cn.wzbos.android.rudolph.router

import cn.wzbos.android.rudolph.IRouteService
import cn.wzbos.android.rudolph.exception.ErrorCode
import cn.wzbos.android.rudolph.exception.ErrorMessage
import cn.wzbos.android.rudolph.exception.RudolphException
import cn.wzbos.android.rudolph.logger.RLog
import java.lang.reflect.Constructor

@Suppress("UNCHECKED_CAST")
open class ServiceRouter<R> : Router<R?> {
    protected constructor(builder: Builder<*, out IRouteService?>) : super(builder)
    constructor(builder: UriRouter.Builder<*>) : super(builder)

    override fun execute(): R? {
        if (super.intercept(null)) return null
        if (target == null) {
            callback?.onError(
                this,
                RudolphException(ErrorCode.NOT_FOUND, ErrorMessage.NOT_FOUND_ERROR)
            )
            return null
        }
        try {
            val constructor: Constructor<*> = target!!.getConstructor()
            if (constructor.parameterTypes.isNotEmpty()) {
                callback?.onError(
                    this,
                    RudolphException(ErrorCode.SERVICE_CREATE_FAILED, "创建服务失败,请提供一个无参数构造方法！")
                )
            }
            if (!constructor.isAccessible) {
                constructor.isAccessible = true
            }
            val instance: Any = constructor.newInstance()
            if (instance is IRouteService) {
                val component: IRouteService = instance
                component.init(extras)
                callback?.onSuccess(this)
                return component as R
            }
        } catch (e: Exception) {
            if (callback != null) {
                callback?.onError(
                    this,
                    RudolphException(ErrorCode.SERVICE_CREATE_FAILED, "创建服务失败！", e)
                )
            } else {
                RLog.e("ServiceRouter", "创建服务失败!")
            }
        }
        return null
    }

    open class Builder<T : Builder<T, R>?, R : IRouteService?>(path: String) :
        RouteBuilder<Builder<T, R>?, ServiceRouter<R>?>(path) {

        override fun build(): ServiceRouter<R> {
            return ServiceRouter(this)
        }
    }
}