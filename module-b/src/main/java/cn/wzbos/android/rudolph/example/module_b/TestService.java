package cn.wzbos.android.rudolph.example.module_b;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.IRouteService;
import cn.wzbos.android.rudolph.annotations.Export;
import cn.wzbos.android.rudolph.annotations.Arg;
import cn.wzbos.android.rudolph.annotations.Route;

@Route(value = "/service/test", export = true)
public class TestService implements IRouteService {

    @Arg
    int userId;

    @Arg
    String userName;

    @Export
    public void showMessage(Context context, String msg) {
        Toast.makeText(context, msg + "\nuserId:" + userId + ",userName:" + userName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init(Bundle bundle) {
        Log.d("TestService", "afterInject");
        Rudolph.bind(TestService.this, bundle);
    }
}
