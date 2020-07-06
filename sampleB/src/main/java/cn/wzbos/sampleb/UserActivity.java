package cn.wzbos.sampleb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.annotations.Extra;
import cn.wzbos.android.rudolph.annotations.Export;
import cn.wzbos.android.rudolph.annotations.Route;

import java.util.Locale;

import static cn.wzbos.android.rudolph.Consts.RAW_URI;

@Route(value = "/user", tag = "isNeedLogin")
@Export
public class UserActivity extends AppCompatActivity {
    @Extra(RAW_URI)
    String routeUri;

    @Extra("userId")
    int userId;

    @Extra("userName")
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        Rudolph.bind(this);

        ((TextView) findViewById(R.id.tvMessage)).setText(String.format(Locale.getDefault(),
                "RawUri:%s\nuserId:%d,userName:%s", routeUri, userId, userName));
    }
}