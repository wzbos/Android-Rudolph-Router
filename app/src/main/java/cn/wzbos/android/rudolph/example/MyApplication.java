package cn.wzbos.android.rudolph.example;

import android.app.Application;

import cn.wzbos.android.rudolph.RouteType;
import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.router.ActivityRouter;
import cn.wzbos.samplea.AppUserRouter;
import cn.wzbos.samplea.LoginActivityRouter;

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
        Rudolph.addInterceptor((context, router) -> {
            if ("isNeedLogin".equals(router.getRouteTag()) && !AppUserRouter.get().isLogin()) {
                if (router.getRouteType() == RouteType.ACTIVITY) {
                    ActivityRouter activityRouter = (ActivityRouter) router;
                    LoginActivityRouter.builder()
                            .to(activityRouter.getRawUrl())
                            .toExtras(activityRouter.getExtras())
                            .transition(activityRouter.getEnterAnim(), activityRouter.getExitAnim())
                            .flags(activityRouter.getFlags())
                            .options(activityRouter.getOptions())
                            .buildStart(context);
                    return true;
                }
            }
            return false;
        });
    }

}
