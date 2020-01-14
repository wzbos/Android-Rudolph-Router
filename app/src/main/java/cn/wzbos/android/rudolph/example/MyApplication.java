package cn.wzbos.android.rudolph.example;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.Interceptor;
import cn.wzbos.android.rudolph.router.Router;

/**
 * Created by wuzongbo on 2017/4/11.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        InitRouter();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    private void InitRouter() {
        Rudolph.init(this);
        Rudolph.addInterceptor(new Interceptor() {
            @Override
            public boolean intercept(Context context, Router routeInfo) {
                if ("/test4".equalsIgnoreCase(routeInfo.getRawUrl())) {
                    Toast.makeText(MyApplication.this, "intercept,path:" + routeInfo.getRawUrl(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

}
