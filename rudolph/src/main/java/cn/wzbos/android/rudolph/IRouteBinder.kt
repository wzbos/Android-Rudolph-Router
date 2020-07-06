package cn.wzbos.android.rudolph

import android.os.Bundle

interface IRouteBinder {
    fun bind(target: Any?, bundle: Bundle?)
}