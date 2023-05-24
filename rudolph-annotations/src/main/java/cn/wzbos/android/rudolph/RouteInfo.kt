package cn.wzbos.android.rudolph

import java.lang.reflect.Type

/**
 * Route Meta
 */
class RouteInfo(builder: Builder) {
    val url: MutableList<String>?
    val tag: String?
    val interceptors: MutableList<Class<out RouteInterceptor>>?

    @Deprecated("请使用targetClass", replaceWith = ReplaceWith("targetClass"))
    val target: String?
    val targetClass: Class<*>?
    val extras: MutableMap<String, ExtraInfo>?
    var type: RouteType?
        private set

    init {
        url = builder.url
        tag = builder.tag
        interceptors = builder.interceptors
        target = builder.target
        targetClass = builder.targetClass
        type = builder.routeType
        extras = builder.extras
    }


    class Builder {
        var url: MutableList<String>? = null
        var target: String? = null
        var targetClass: Class<*>? = null
        var extras: MutableMap<String, ExtraInfo>? = null
        var routeType: RouteType? = null
        var tag: String? = null
        var interceptors: MutableList<Class<out RouteInterceptor>>? = null

        @Deprecated("已过期", replaceWith = ReplaceWith("url(path)"))
        fun path(path: String): Builder {
            this.url = mutableListOf(path)
            return this
        }

        fun url(vararg url: String): Builder {
            this.url = url.toMutableList()
            return this
        }

        @Deprecated("已过期", replaceWith = ReplaceWith("targetClass(target)"))
        fun target(target: String): Builder {
            this.target = target
            return this
        }

        fun targetClass(targetClass: Class<*>): Builder {
            this.targetClass = targetClass
            return this
        }

        @Deprecated("已过期")
        fun extra(key: String, className: String): Builder {
            return try {
                val type = when (className) {
                    Boolean::class.java.name -> Boolean::class.java
                    Byte::class.java.name -> Byte::class.java
                    Char::class.java.name -> Char::class.java
                    Short::class.java.name -> Short::class.java
                    Double::class.java.name -> Double::class.java
                    Float::class.java.name -> Float::class.java
                    Int::class.java.name -> Int::class.java
                    Long::class.java.name -> Long::class.java
                    Void::class.java.name -> Void::class.java
                    else -> Class.forName(className)
                }
                extra(key, ExtraInfo(type))
            } catch (e: Exception) {
                println("类型转换失败! (target=${target}, key:${key}, className:${className})")
                e.printStackTrace()
                //兼容老版本，此处统一转换为Any类型
                extra(key, UnknownExtraType(className))
            }
        }

        @JvmOverloads
        fun extra(
            key: String,
            type: Type,
            base64: Boolean = false,
            json: Boolean = false
        ): Builder {
            return extra(key, ExtraInfo(type, base64 = base64, json = json))
        }

        fun extra(key: String, extraType: ExtraInfo): Builder {
            if (extras == null) {
                extras = LinkedHashMap()
            }
            extras?.put(key, extraType)
            return this
        }

        fun routeType(routeType: RouteType?): Builder {
            this.routeType = routeType
            return this
        }

        fun tag(tag: String?): Builder {
            this.tag = tag
            return this
        }

        fun interceptors(vararg interceptors: Class<out RouteInterceptor>): Builder {
            this.interceptors = interceptors.toMutableList()
            return this
        }

        fun interceptors(interceptors: MutableList<Class<out RouteInterceptor>>): Builder {
            this.interceptors = interceptors
            return this
        }


        fun build(): RouteInfo {
            return RouteInfo(this)
        }

    }

}