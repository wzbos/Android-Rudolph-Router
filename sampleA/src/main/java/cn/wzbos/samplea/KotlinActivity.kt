package cn.wzbos.samplea

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.wzbos.android.rudolph.Rudolph
import cn.wzbos.android.rudolph.Consts.RAW_URI
import cn.wzbos.android.rudolph.annotations.Extra
import cn.wzbos.android.rudolph.annotations.Route

@Route(urls = ["/kotlin/test"])
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
        this.findViewById<TextView>(R.id.btnClose).setOnClickListener {
            close()
        }
    }

    private fun close() {
        val resultIntent = Intent().putExtra("testResult", "ok")
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}
