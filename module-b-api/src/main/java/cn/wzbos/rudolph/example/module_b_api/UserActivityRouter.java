package cn.wzbos.rudolph.example.module_b_api;

import cn.wzbos.rudolph.router.ActivityRouter;
import java.lang.String;

/**
 * Generated code from rudolph. Do not modify!
 */
public class UserActivityRouter {
  public static UserActivityRouter.Builder builder() {
    return new UserActivityRouter.Builder();
  }

  public static class Builder extends ActivityRouter.Builder<UserActivityRouter.Builder> {
    Builder() {
      super("/user");
    }

    public UserActivityRouter.Builder userId(int val) {
      super.arg("userId",val);
      return this;
    }

    public UserActivityRouter.Builder userName(String val) {
      super.arg("userName",val);
      return this;
    }
  }
}
