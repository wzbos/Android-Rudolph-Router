package cn.wzbos.rudolph.example.module_a;

import android.content.Context;


import cn.wzbos.rudolph.annotations.Arg;
import cn.wzbos.rudolph.annotations.Route;

public class TestMethod {

    @Route("/method/test")
    public static String Test(@Arg Context context, @Arg int userId, @Arg String userName) {
        return "Hello Method!\nPackageName:" + context.getPackageName() + "\nuserId:" + userId + ",userName:" + userName;
    }

}
