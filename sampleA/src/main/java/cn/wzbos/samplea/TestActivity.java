package cn.wzbos.samplea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.annotations.Extra;
import cn.wzbos.android.rudolph.annotations.Export;
import cn.wzbos.android.rudolph.annotations.Route;
import cn.wzbos.protocol.samplea.bean.Broker;
import cn.wzbos.protocol.samplea.bean.User;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static cn.wzbos.android.rudolph.Consts.RAW_URI;


@Route(value = "/activity/test")
@Export
public class TestActivity extends AppCompatActivity {
    @Extra(RAW_URI)
    String routeUri;

    @Extra("stringArg")
    String stringArg;

    @Extra(value = "string64", base64 = true)
    String string64Arg;

    @Extra("stringArray")
    String[] stringArrayArg;


    @Extra("boolArg")
    boolean boolArg;

    @Extra("booleanArray")
    boolean[] booleanArrayArg;

    @Extra("byteArg")
    byte byteArg;

    @Extra("byteArray")
    byte[] byteArrayArg;

    @Extra("shortArg")
    short shortArg;

    @Extra("shortArray")
    short[] shortArrayArg;

    @Extra("intArg")
    int intArg;

    @Extra("intArrayArg")
    int[] intArrayArg;

    @Extra("longArg")
    long longArg;

    @Extra("longArray")
    long[] longArrayArg;

    @Extra("charArg")
    char charArg;

    @Extra("charArray")
    char[] charArrayArg;

    @Extra("floatArg")
    float floatArg;

    @Extra("floatArray")
    float[] floatArrayArg;

    @Extra("doubleArg")
    double doubleArg;

    @Extra("doubleArray")
    double[] doubleArrayArg;

    @Extra("characterArg")
    Character characterArg;

    //ArrayList
    @Extra
    ArrayList<String> stringArrayListArg;

    @Extra
    ArrayList<Integer> integerArrayListArg;

    @Extra(value = "charSequenceArrayList")
    ArrayList<CharSequence> charSequenceArrayListArg;

    @Extra(value = "parcelableArrayList")
    ArrayList<Broker> parcelableArrayListArg;

    @Extra(value = "serialized")
    Broker serializedParam;

    //json
    @Extra(value = "json", json = true)
    User<Broker> jsonParam;

    //encode：json->base64，decode：base64->json
    @Extra(value = "base64json", json = true, base64 = true)
    User<Broker> base64jsonParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        TextView tvMessage = findViewById(R.id.tvMessage);

        Rudolph.bind(this);

        StringBuffer stringBuffer = new StringBuffer();
        for (Field filed : this.getClass().getDeclaredFields()) {
            try {
                stringBuffer.append(filed.getName()).append("=").append(filed.get(this)).append("\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        tvMessage.setText(stringBuffer);

    }
}