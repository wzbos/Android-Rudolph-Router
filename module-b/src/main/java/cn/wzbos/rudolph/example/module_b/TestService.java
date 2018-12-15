package cn.wzbos.rudolph.example.module_b;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.wzbos.rudolph.Rudolph;
import cn.wzbos.rudolph.IRouteService;
import cn.wzbos.rudolph.annotations.Export;
import cn.wzbos.rudolph.annotations.Arg;
import cn.wzbos.rudolph.annotations.Route;

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
