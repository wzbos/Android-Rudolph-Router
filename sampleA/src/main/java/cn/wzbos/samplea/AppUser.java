package cn.wzbos.samplea;

import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import cn.wzbos.android.rudolph.annotations.Route;
import cn.wzbos.protocol.samplea.IAppUser;

@Route(urls = "/app/user", singleton = true, clazz = IAppUser.class)
public class AppUser implements IAppUser {
    private boolean isLogin = false;

    @Override
    public boolean isLogin() {
        return isLogin;
    }

    @Override
    public IAppUser setLogin(boolean login) {
        isLogin = login;
        return this;
    }

    @Override
    public void init(@Nullable Bundle bundle) {

    }
}
