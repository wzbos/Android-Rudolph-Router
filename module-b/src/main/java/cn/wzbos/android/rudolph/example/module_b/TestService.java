package cn.wzbos.android.rudolph.example.module_b;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.annotations.Arg;
import cn.wzbos.android.rudolph.annotations.Route;
import cn.wzbos.android.rudolph.example.module_b_api.ITestService;

@Route(value = "/service/test", export = true, singleton = true, clazz = ITestService.class)
public class TestService implements ITestService {

    @Arg
    int userId;

    @Arg
    String userName;

    public void showMessage(Context context, String msg) {
        Toast.makeText(context, msg + "\nuserId:" + userId + ",userName:" + userName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init(Bundle bundle) {
        Log.d("TestService", "afterInject");
        Rudolph.bind(TestService.this, bundle);
    }
}
