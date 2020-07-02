package cn.wzbos.protocol.sampleb;

import android.content.Context;
import android.os.Bundle;

import cn.wzbos.android.rudolph.IRouteService;

import java.lang.String;

/**
 * Generated code from Rudolph. Do not modify!
 */
public interface ITestService extends IRouteService {
    int getUserId();

    void setUserId(int userId);

    String getUserName();

    void setUserName(String userName);

    void showMessage(Context context, String msg);
}
