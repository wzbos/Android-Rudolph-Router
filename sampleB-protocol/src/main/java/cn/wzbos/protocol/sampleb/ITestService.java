package cn.wzbos.protocol.sampleb;

import android.content.Context;

import cn.wzbos.android.rudolph.IRouteService;

import java.lang.String;


public interface ITestService extends IRouteService {
    int getUserId();

    void setUserId(int userId);

    String getUserName();

    void setUserName(String userName);

    void showMessage(Context context, String msg);
}
