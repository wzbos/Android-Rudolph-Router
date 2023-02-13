package cn.wzbos.android.rudolph.router

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import cn.wzbos.android.rudolph.*
import cn.wzbos.android.rudolph.Rudolph.getRouter
import cn.wzbos.android.rudolph.logger.RLog
import cn.wzbos.android.rudolph.utils.TypeUtils
import java.io.Serializable
import java.net.URLDecoder
import java.util.*

/**
 * Bundle IBuilder
 * Created by wuzongbo on 2017/9/13.
 */
@Suppress("UNCHECKED_CAST")
abstract class RouteBuilder<B : RouteBuilder<B, R>?, R : Router<*>?>(val rawUrl: String) :
    IRouteBuilder<RouteBuilder<B, R>?, R> {
    companion object {
        const val TAG = "RouteBuilder"
    }

    override var extras: Bundle = Bundle()
    var scheme: MutableList<String>? = null
    var host: MutableList<String>? = null
    var path: MutableList<String>? = null

    var callback: OnRouteListener? = null
        private set
    var target: Class<*>? = null
        private set
    var routeType: RouteType? = null
        private set
    var routeTag: String? = null
        private set
    var extraTypes: MutableMap<String, ExtraType>? = null
        private set
    var interceptors: MutableList<Class<out RouteInterceptor>>? = null
        private set

    private var encodedQuery: String? = null


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

    override fun putParcelableArrayListExtra(
        key: String?,
        value: ArrayList<out Parcelable?>?,
    ): B {
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

    override fun putCharSequenceArrayListExtra(
        key: String?,
        value: ArrayList<CharSequence?>?,
    ): B {
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

    private fun putUriAllParams() {
        //返回的所有的地址参数与查询参数值
        val resultMap = uriAllParams
        for (kv: Map.Entry<String, String?> in resultMap.entries) {
            addParams(kv.key, kv.value)
        }
    }

    private fun addParams(key: String, value: String?) {
        if (extraTypes?.containsKey(key) == true) {
            val type = extraTypes?.get(key)
            if (!value.isNullOrEmpty() && type != null) {
                TypeUtils.getObject(null, key, value, type, this)
            }
        } else {
            //未用 @Extra 注解声明的参数类型，
            extras.putString(key, value)
        }
    }

    init {
        RLog.i(TAG, "rawUrl=${rawUrl}")

        if (rawUrl.isNotBlank()) {
            val uri = Uri.parse(rawUrl)
            uri.path?.let {
                this.path = mutableListOf(it)
            }

            encodedQuery = rawUrl.indexOf("?").let {
                if (it > -1) {
                    rawUrl.substring(it + 1)
                } else {
                    null
                }
            }
            val info = getRouter(rawUrl)
            if (info != null) {
                setRouterInfo(info)
            } else {
                RLog.e(TAG, "没有路由信息，rawUrl=${rawUrl}")
            }
            putUriAllParams()
        }
    }

    private fun setRouterInfo(it: RouteInfo) {
        val targetName = it.target
        if (!targetName.isNullOrEmpty()) {
            try {
                target = Class.forName(targetName)
            } catch (e: ClassNotFoundException) {
                RLog.e(TAG, "错误：路由类加载失败！$target", e)
            }
        } else {
            target = it.targetClass
        }
        routeTag = it.tag
        routeType = it.type
        extraTypes = it.extras
        interceptors = it.interceptors
    }


    /**
     * 获取当前URL地址的所有参数
     *
     * @return 返回参数kv集合
     */
    val uriAllParams: MutableMap<String, String?>
        get() {
            val params: MutableMap<String, String?> = LinkedHashMap()
            //raw uri
            params[Consts.RAW_URI] = rawUrl
            //put query params
            val query = encodedQuery
            if (!query.isNullOrEmpty()) {
                val queryParameters = query.split("&".toRegex()).toTypedArray()
                for (str: String in queryParameters) {
                    val kv = str.split("=".toRegex()).toTypedArray()
                    if (kv.size == 2) {
                        try {
                            val name = URLDecoder.decode(kv[0], "utf-8")
                            val value = URLDecoder.decode(kv[1], "utf-8")
                            params[name] = value.ifEmpty { null }
                        } catch (e: Exception) {
                            RLog.e(TAG, "getUriAllParams failed!", e)
                        }
                    }
                }
            }
            return params
        }

}