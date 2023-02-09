package cn.wzbos.sampleb;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.annotations.Export;
import cn.wzbos.android.rudolph.annotations.Extra;
import cn.wzbos.android.rudolph.annotations.Route;
import cn.wzbos.protocol.sampleb.ITestService;

@Route(urls = "/service/test", clazz = ITestService.class, singleton = true)
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
    public ITestService setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public ITestService setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public void showMessage(Context context, String msg) {
        Toast.makeText(context, msg + "\nuserId:" + userId + ",userName:" + userName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init(Bundle bundle) {
        Rudolph.bind(TestService.this, bundle);
    }
}
