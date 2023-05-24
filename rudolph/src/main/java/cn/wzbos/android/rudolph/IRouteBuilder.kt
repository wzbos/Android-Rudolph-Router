package cn.wzbos.android.rudolph

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import cn.wzbos.android.rudolph.router.Router
import java.io.Serializable
import java.util.*

interface IRouteBuilder<B : IRouteBuilder<B, R>?, R : Router<*>?> {
    fun putExtra(map: Bundle?): B
    fun putExtra(key: String?, value: String?): B
    fun putExtra(key: String?, value: Boolean): B
    fun putExtra(key: String?, value: Short): B
    fun putExtra(key: String?, value: Int): B
    fun putExtra(key: String?, value: Long): B
    fun putExtra(key: String?, value: Double): B
    fun putExtra(key: String?, value: Byte): B
    fun putExtra(key: String?, value: Char): B
    fun putExtra(key: String?, value: Float): B
    fun putExtra(key: String?, value: CharSequence?): B
    fun putExtra(key: String?, value: Parcelable?): B
    fun putExtra(key: String?, value: Array<Parcelable?>?): B
    fun putParcelableArrayListExtra(key: String?, value: ArrayList<out Parcelable?>?): B
    fun putExtra(key: String?, value: SparseArray<out Parcelable?>?): B
    fun putIntegerArrayListExtra(key: String?, value: ArrayList<Int?>?): B
    fun putStringArrayListExtra(key: String?, value: ArrayList<String?>?): B
    fun putCharSequenceArrayListExtra(key: String?, value: ArrayList<CharSequence?>?): B
    fun putExtra(key: String?, value: Serializable?): B
    fun putExtra(name: String?, value: BooleanArray?): B
    fun putExtra(key: String?, value: ByteArray?): B
    fun putExtra(key: String?, value: ShortArray?): B
    fun putExtra(key: String?, value: CharArray?): B
    fun putExtra(key: String?, value: IntArray?): B
    fun putExtra(key: String?, value: LongArray?): B
    fun putExtra(key: String?, value: FloatArray?): B
    fun putExtra(key: String?, value: DoubleArray?): B
    fun putExtra(key: String?, value: Array<String?>?): B
    fun putExtra(key: String?, value: Array<CharSequence?>?): B
    fun putExtra(key: String?, value: Bundle?): B

    /**
     * 获取所有参数
     *
     * @return Bundle
     */
    var extras: Bundle

    /**
     * 设置callback
     *
     * @param callback RouteCallback
     * @return IRouteBuilder
     */
    fun onListener(callback: OnRouteListener?): B

    /**
     * 生成Router
     *
     * @return Router
     */
    fun build(): R

    fun putSerializable(key: String?, value: Serializable?): B
    fun putParcelable(key: String?, value: Parcelable?): B
    fun putBase64(key: String?, value: String): B
    fun putBase64Json(key: String?, value: Any): B
    fun putBase64(key: String?, value: ByteArray): B
    fun putJson(key: String?, value: Any): B
}