package cn.wzbos.android.rudolph

import java.util.*

/**
 * Route Meta
 */
class RouteInfo private constructor(builder: Builder) {
    var path: String?
    val target: String?
    val extras: MutableMap<String, String>?
    var type: RouteType?
        private set
    val tag: String?

    init {
        path = builder.path
        target = builder.target
        type = builder.routeType
        extras = builder.extras
        tag = builder.tag
    }

    /**
     * 比较路由地址
     */
    fun equalPath(currPath: String?): Boolean {
        if (currPath.isNullOrEmpty())
            return false
        //path params
        val segments = path?.split("/".toRegex())?.toTypedArray()
        val currSegments = currPath.split("/".toRegex()).toTypedArray()

        //segments
        if (segments?.size != currSegments.size) {
            return false
        }
        for (i in segments.indices) {
            if (segments[i].startsWith(":")) continue
            if (!segments[i].equals(currSegments[i], ignoreCase = true)) return false
        }
        return true
    }

    class Builder {
        var path: String? = null
        var target: String? = null
        var extras: MutableMap<String, String>? = null
        var routeType: RouteType? = null
        var tag: String? = null
        fun path(path: String?): Builder {
            this.path = path
            return this
        }

        fun target(target: String): Builder {
            this.target = target
            return this
        }

        fun extra(key: String, type: String): Builder {
            if (extras == null) {
                extras = LinkedHashMap()
            }
            extras?.put(key, type)
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

        fun build(): RouteInfo {
            return RouteInfo(this)
        }
    }

}