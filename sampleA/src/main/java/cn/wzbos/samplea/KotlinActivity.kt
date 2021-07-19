package cn.wzbos.samplea

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.wzbos.android.rudolph.Rudolph
import cn.wzbos.android.rudolph.Consts.RAW_URI
import cn.wzbos.android.rudolph.annotations.Extra
import cn.wzbos.android.rudolph.annotations.Route

@Route("/kotlin/test")
class KotlinActivity : AppCompatActivity() {

    @Extra(RAW_URI)
    var routeUri: String? = null

    @Extra("userId")
    var userId: Int = 0

    @Extra("userName")
    var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        Rudolph.bind(this)
    }

}
