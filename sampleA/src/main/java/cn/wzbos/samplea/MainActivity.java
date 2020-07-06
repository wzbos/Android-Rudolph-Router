package cn.wzbos.samplea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.annotations.Extra;
import cn.wzbos.android.rudolph.annotations.Route;
import cn.wzbos.protocol.samplea.TestActivityRouter;
import cn.wzbos.protocol.samplea.bean.Broker;
import cn.wzbos.protocol.samplea.bean.User;
import cn.wzbos.protocol.sampleb.ITestService;
import cn.wzbos.protocol.sampleb.TestServiceRouter;
import cn.wzbos.protocol.sampleb.UserActivityRouter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


@Route
public class MainActivity extends AppCompatActivity {

    @Extra("index")
    int index;

    @Extra("name")
    String name;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Rudolph.bind(this);

        linearLayout = findViewById(R.id.llMain);

        Map<String, View.OnClickListener> array = new LinkedHashMap<>();

        array.put("Test support parameter types!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> stringArrayList = new ArrayList<>();
                stringArrayList.add("Jim");
                stringArrayList.add("HanMeimei");
                stringArrayList.add("LiLei");

                ArrayList<Integer> integerArrayListArg = new ArrayList<>();
                integerArrayListArg.add(1);
                integerArrayListArg.add(2);
                integerArrayListArg.add(3);

                ArrayList<CharSequence> charSequenceArrayListArg = new ArrayList<>();
                charSequenceArrayListArg.add("Jim");
                charSequenceArrayListArg.add("HanMeimei");
                charSequenceArrayListArg.add("LiLei");

                ArrayList<Broker> parcelableArrayListArg = new ArrayList<>();
                parcelableArrayListArg.add(new Broker("Jim", 12));
                parcelableArrayListArg.add(new Broker("HanMeimei", 18));
                parcelableArrayListArg.add(new Broker("LiLei", 20));

                TestActivityRouter.builder()
                        .stringArg("Jim")
                        .string64Arg("HanMeimei")
                        .stringArrayArg(new String[]{"jim", "HanMeimei", "LiLei"})
                        .boolArg(true)
                        .booleanArrayArg(new boolean[]{true, false})
                        .byteArg((byte) 0x10)
                        .byteArrayArg(new byte[]{0x10, 0x11, 0x12})
                        .shortArg((short) 2000)
                        .shortArrayArg(new short[]{1000, 1001, 1002, 1003})
                        .intArg(10)
                        .intArrayArg(new int[]{10, 11, 12, 13})
                        .longArg(20)
                        .longArrayArg(new long[]{20, 21, 22, 23})
                        .charArg('a')
                        .charArrayArg(new char[]{'a', 'b', 'c', 'd'})
                        .floatArg(0.12f)
                        .floatArrayArg(new float[]{0.12f, 0.13f, 0.14f})
                        .doubleArg(0.20d)
                        .doubleArrayArg(new double[]{0.20, 0.21, 0.22, 0.23})
                        .characterArg('a')
                        .stringArrayListArg(stringArrayList)
                        .integerArrayListArg(integerArrayListArg)
                        .charSequenceArrayListArg(charSequenceArrayListArg)
                        .parcelableArrayListArg(parcelableArrayListArg)
                        .parcelableParam(new Broker("Jim", 28))
                        .jsonParam(new User<>("Midlife crisis!", new Broker("Wrinkly", 30)))
                        .base64jsonParam(new User<>("Happy new year！", new Broker("Merry Christmas!", 60)))
                        .build()
                        .start(MainActivity.this);
            }
        });

        array.put("ModuleA->ModuleB.UserActivity(Router)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserActivityRouter.builder().userId(11).userName("John").transition(R.anim.in_left, R.anim.out_right).build().start(MainActivity.this);
            }
        });

        array.put("ModuleA->ModuleB.UserActivity(URL)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rudolph.builder("/user?userId=11&userName=John").build().open(MainActivity.this, 1000);
            }
        });


        array.put("Get fragment(Router)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = TestFragmentRouter.builder().userId(11).userName("John").build().open();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flMain, fragment);
                ft.commit();
            }
        });

        array.put("Get fragment(URL)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = (Fragment) Rudolph.builder("/fragment/test?userId=11&userName=John").build().open();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.flMain, fragment);
                ft.commit();
            }
        });


        array.put("ModuleA->ModuleB.TestService(Router)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestServiceRouter.get().setUserId(11).setUserName("John").showMessage(MainActivity.this, "Hello Provider!");
            }
        });

        array.put("ModuleA->ModuleB.TestService(URL)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ITestService service = (ITestService) Rudolph.builder("/service/test?userId=11&userName=John").build().open();
                service.showMessage(MainActivity.this, "Hello Provider!");
            }
        });

        array.put("Invoke static method(URL)", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = (String) Rudolph.builder("/method/test?userId=1&userName=John").build().open();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        for (Map.Entry<String, View.OnClickListener> entry : array.entrySet()) {
            Button btn = new Button(this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setTextSize(16);
            btn.setAllCaps(false);
            btn.setText(entry.getKey());
            btn.setOnClickListener(entry.getValue());
            linearLayout.addView(btn);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Rudolph.onNewIntent(this, intent);
    }
}
