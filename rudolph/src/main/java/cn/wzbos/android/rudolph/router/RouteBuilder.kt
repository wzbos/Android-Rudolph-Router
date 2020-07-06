package cn.wzbos.android.rudolph.router

import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.SparseArray
import cn.wzbos.android.rudolph.*
import cn.wzbos.android.rudolph.Rudolph.getRouter
import cn.wzbos.android.rudolph.Rudolph.routers
import cn.wzbos.android.rudolph.utils.TypeUtils
import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.*

/**
 * Bundle IBuilder
 * Created by wuzongbo on 2017/9/13.
 */
@Suppress("UNCHECKED_CAST")
abstract class RouteBuilder<B : RouteBuilder<B, R>?, R : Router<*>?> : IRouteBuilder<RouteBuilder<B, R>?, R> {
    private val TAG = "RouteBuilder"
    override var extras: Bundle = Bundle()
    var callback: OnRouteListener? = null
        private set
    var target: Class<*>? = null
        private set
    var rawUrl: String? = null
        private set
    var routePath: String? = null
        private set
    var routeType: RouteType? = null
        private set
    var routeTag: String? = null
        private set
    var extraTypes: MutableMap<String, String>? = null
        private set

    override fun putExtra(map: Bundle?): B {
        extras.putAll(map)
        return this as B
    }

    override fun putExtra(key: String?, value: String?): B {
        extras.putString(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Boolean): B {
        extras.putBoolean(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Short): B {
        extras.putShort(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Int): B {
        extras.putInt(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Long): B {
        extras.putLong(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Double): B {
        extras.putDouble(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Byte): B {
        extras.putByte(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Char): B {
        extras.putChar(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Float): B {
        extras.putFloat(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: CharSequence?): B {
        extras.putCharSequence(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Parcelable?): B {
        extras.putParcelable(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Array<Parcelable?>?): B {
        extras.putParcelableArray(key, value)
        return this as B
    }

    override fun putParcelableArrayListExtra(key: String?, value: ArrayList<out Parcelable?>?): B {
        extras.putParcelableArrayList(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: SparseArray<out Parcelable?>?): B {
        extras.putSparseParcelableArray(key, value)
        return this as B
    }

    override fun putIntegerArrayListExtra(key: String?, value: ArrayList<Int?>?): B {
        extras.putIntegerArrayList(key, value)
        return this as B
    }

    override fun putStringArrayListExtra(key: String?, value: ArrayList<String?>?): B {
        extras.putStringArrayList(key, value)
        return this as B
    }

    override fun putCharSequenceArrayListExtra(key: String?, value: ArrayList<CharSequence?>?): B {
        extras.putCharSequenceArrayList(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Serializable?): B {
        extras.putSerializable(key, value)
        return this as B
    }

    override fun putExtra(name: String?, value: BooleanArray?): B {
        extras.putBooleanArray(name, value)
        return this as B
    }

    override fun putExtra(key: String?, value: ByteArray?): B {
        extras.putByteArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: ShortArray?): B {
        extras.putShortArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: CharArray?): B {
        extras.putCharArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: IntArray?): B {
        extras.putIntArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: LongArray?): B {
        extras.putLongArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: FloatArray?): B {
        extras.putFloatArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: DoubleArray?): B {
        extras.putDoubleArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Array<String?>?): B {
        extras.putStringArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Array<CharSequence?>?): B {
        extras.putCharSequenceArray(key, value)
        return this as B
    }

    override fun putExtra(key: String?, value: Bundle?): B {
        extras.putBundle(key, value)
        return this as B
    }

    override fun onListener(callback: OnRouteListener?): B {
        this.callback = callback
        return this as B
    }


    private constructor() {
        extras = Bundle()
    }

    constructor(target: Class<*>?) : this() {
        this.target = target
    }

    constructor(rawUrl: String) : this() {
        this.rawUrl = rawUrl
        if (!TextUtils.isEmpty(rawUrl)) {
            val routeInfo = getRouter(path)
            if (null == routeInfo) {
                if (routers.isEmpty()) {
                    RLog.e(TAG, "错误：没有匹配到相关路由:$rawUrl,当前路由表为空,请确认是否已进行 Rudolph.init() 初始化操作！")
                } else {
                    RLog.e(TAG, "错误：没有匹配到相关路由:$rawUrl")
                }
                return
            }

            routeInfo.target?.also {
                try {
                    target = Class.forName(it)
                } catch (e: ClassNotFoundException) {
                    RLog.e(TAG, "错误：路由类加载失败！$target", e)
                }
            }


            routePath = routeInfo.path
            routeTag = routeInfo.tag
            routeType = routeInfo.type
            extraTypes = routeInfo.extras

            //返回的所有的地址参数与查询参数值
            val resultMap = uriAllParams
            if (resultMap != null) {
                //页面接收参数
                for (kv: Map.Entry<String, String?> in resultMap.entries) {
                    addParams(kv.key, kv.value)
                }
            }
        }
    }

    private fun addParams(key: String, value: String?) {
        if (extraTypes != null && extraTypes!!.isNotEmpty() && extraTypes!!.containsKey(key)) {
            val type = extraTypes!![key]
            if (value != null && value.isNotEmpty() && type != null) {
                TypeUtils.getObject(null, key, value, type, this)
            }
        } else {
            extras.putString(key, value)
        }
    }

    val path: String
        get() {
            val n = rawUrl!!.indexOf("://")
            val stx: Int
            val etx: Int
            if (n > -1) {
                stx = n + 3
                etx = rawUrl!!.indexOf("?", stx)
            } else {
                stx = 0
                etx = rawUrl!!.indexOf("?")
            }
            val path: String
            path = if (etx > -1) {
                rawUrl!!.substring(stx, etx)
            } else {
                rawUrl!!.substring(stx)
            }
            return path
        }

    private val segments: List<String>
        get() {
            val segments: MutableList<String> = ArrayList()
            val values = path.split("/".toRegex()).toTypedArray()
            for (v: String in values) {
                try {
                    if (!TextUtils.isEmpty(v)) {
                        segments.add(URLDecoder.decode(v, "utf-8"))
                    }
                } catch (e: UnsupportedEncodingException) {
                    RLog.e(TAG, "getSegments failed!", e)
                }
            }
            return segments
        }

    private val encodedQuery: String?
        get() {
            val n = rawUrl!!.indexOf("?")
            return if (n > -1) {
                rawUrl!!.substring(n + 1)
            } else {
                null
            }
        }

    /**
     * 获取当前URL地址的所有参数
     *
     * @return 返回参数kv集合
     */
    val uriAllParams: MutableMap<String, String?>?
        get() {
            val params: MutableMap<String, String?> = LinkedHashMap()
            val routeSegments = routePath!!.substring(1).split("/".toRegex()).toTypedArray()
            val pathSegments = segments

            //segments个数不匹配
            if (routeSegments.size != pathSegments.size) return null
            //raw uri
            params[Consts.RAW_URI] = rawUrl
            //path params
            for (i in routeSegments.indices) {
                if (routeSegments[i].startsWith(":")) {
                    //put path params
                    params[routeSegments[i].substring(1)] = pathSegments[i]
                    continue
                }
                if (!routeSegments[i].equals(pathSegments[i], ignoreCase = true)) return null
            }

            //put query params
            val query = encodedQuery
            if (!TextUtils.isEmpty(query)) {
                val queryParameters = query!!.split("&".toRegex()).toTypedArray()
                for (str: String in queryParameters) {
                    val kv = str.split("=".toRegex()).toTypedArray()
                    if (kv.size == 2) {
                        try {
                            val name = URLDecoder.decode(kv[0], "utf-8")
                            val value = URLDecoder.decode(kv[1], "utf-8")
                            params[name] = value
                        } catch (e: Exception) {
                            RLog.e(TAG, "getUriAllParams failed!", e)
                        }
                    }
                }
            }
            return params
        }

}