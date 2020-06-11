package cn.wzbos.samplea;

import android.content.Context;


import cn.wzbos.android.rudolph.annotations.Arg;
import cn.wzbos.android.rudolph.annotations.Route;

public class TestMethod {

    @Route("/method/test")
    public static String Test(@Arg Context context, @Arg int userId, @Arg String userName) {
        return "Hello Method!\nPackageName:" + context.getPackageName() + "\nuserId:" + userId + ",userName:" + userName;
    }

}
