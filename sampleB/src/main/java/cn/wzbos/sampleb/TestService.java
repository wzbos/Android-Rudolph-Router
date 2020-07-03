package cn.wzbos.sampleb;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.annotations.Extra;
import cn.wzbos.android.rudolph.annotations.Export;
import cn.wzbos.android.rudolph.annotations.Route;
import cn.wzbos.protocol.sampleb.ITestService;

@Route(value = "/service/test", clazz = ITestService.class, singleton = true)
@Export
public class TestService implements ITestService {

    @Extra
    int userId;

    @Extra
    String userName;

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void showMessage(Context context, String msg) {
        Toast.makeText(context, msg + "\nuserId:" + userId + ",userName:" + userName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init(Bundle bundle) {
        Log.d("TestService", "afterInject");
        Rudolph.bind(TestService.this, bundle);
    }
}
