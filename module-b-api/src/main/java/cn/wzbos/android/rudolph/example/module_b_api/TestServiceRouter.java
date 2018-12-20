package cn.wzbos.android.rudolph.example.module_b_api;

import cn.wzbos.android.rudolph.router.ServiceRouter;
import java.lang.String;

/**
 * Generated code from Rudolph. Do not modify!
 */
public class TestServiceRouter {
  public static TestServiceRouter.Builder builder() {
    return new TestServiceRouter.Builder();
  }

  public static class Builder extends ServiceRouter.Builder<TestServiceRouter.Builder, ITestService> {
    Builder() {
      super("/service/test");
    }

    public TestServiceRouter.Builder userId(int val) {
      super.arg("userId",val);
      return this;
    }

    public TestServiceRouter.Builder userName(String val) {
      super.arg("userName",val);
      return this;
    }
  }
}
