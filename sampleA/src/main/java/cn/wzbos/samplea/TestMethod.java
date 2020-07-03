package cn.wzbos.samplea;

import android.content.Context;


import cn.wzbos.android.rudolph.annotations.Extra;
import cn.wzbos.android.rudolph.annotations.Route;

public class TestMethod {

    @Route("/method/test")
    public static String Test(@Extra Context context, @Extra int userId, @Extra String userName) {
        return "Hello Method!\nPackageName:" + context.getPackageName() + "\nuserId:" + userId + ",userName:" + userName;
    }

}
