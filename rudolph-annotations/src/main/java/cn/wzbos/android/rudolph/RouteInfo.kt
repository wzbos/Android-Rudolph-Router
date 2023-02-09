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
    val extras: MutableMap<String, Type>?
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
        var extras: MutableMap<String, Type>? = null
        var routeType: RouteType? = null
        var tag: String? = null
        var interceptors: MutableList<Class<out RouteInterceptor>>? = null

        @Deprecated("已过期")
        fun path(path: String): Builder {
            this.url = mutableListOf(path)
            return this
        }

        fun url(vararg url: String): Builder {
            this.url = url.toMutableList()
            return this
        }


        fun target(target: String): Builder {
            this.target = target
            return this
        }

        fun targetClass(targetClass: Class<*>): Builder {
            this.targetClass = targetClass
            return this
        }

        fun extra(key: String, className: String): Builder {
            val type = when (className) {
                Boolean::class.java.name -> Boolean::class.java
                Byte::class.java.name -> Byte::class.java
                Char::class.java.name -> Char::class.java
                Short::class.java.name -> Short::class.java
                Double::class.java.name -> Double::class.java
                Float::class.java.name -> Float::class.java
                Int::class.java.name -> Int::class.java
                Long::class.java.name -> Long::class.java
                else -> Class.forName(className)
            }
            extra(key, type)
            return this
        }

        fun extra(key: String, cls: Type): Builder {
            if (extras == null) {
                extras = LinkedHashMap()
            }
            extras?.put(key, cls)
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