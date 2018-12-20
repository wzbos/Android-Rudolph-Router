package cn.wzbos.android.rudolph;

/**
 * Created by wuzongbo on 2017/4/12.
 */
public interface RouteCallback {

    void onSucceed();

    void onFailed(Throwable throwable);
}