package cn.wzbos.protocol.samplea;

import cn.wzbos.android.rudolph.IRouteService;

public interface IAppUser extends IRouteService {
    boolean isLogin();

    IAppUser setLogin(boolean login);
}
