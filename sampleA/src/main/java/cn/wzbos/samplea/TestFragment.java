package cn.wzbos.samplea;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.Consts;
import cn.wzbos.android.rudolph.annotations.Extra;
import cn.wzbos.android.rudolph.annotations.Route;

import java.util.Locale;


@Route("/fragment/test")
public class TestFragment extends Fragment {
    @Extra(Consts.RAW_URI)
    String routeUri;

    @Extra("userId")
    int userId;

    @Extra("userName")
    String userName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);

        Rudolph.bind(this);

        ((TextView) rootView.findViewById(R.id.tvMessage)).setText(String.format(Locale.getDefault(),
                "RawUri:%s\nUserId:%d\nUserName:%s",
                routeUri, userId, userName));
        return rootView;
    }
}
