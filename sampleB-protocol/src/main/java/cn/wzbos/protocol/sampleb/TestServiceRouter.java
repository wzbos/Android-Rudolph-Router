package cn.wzbos.protocol.sampleb;

import cn.wzbos.android.rudolph.router.ServiceRouter;
import java.lang.String;

/**
 * Generated code from Rudolph. Do not modify!
 */
public class TestServiceRouter {
  private static volatile ITestService instance;

  public static ITestService get() {
    if (instance == null) {
      synchronized (TestServiceRouter.class) {
        if (instance == null) {
          instance = builder().build().open();
        }
        return instance;
      }
    }
    return instance;
  }

  public static TestServiceRouter.Builder builder() {
    return new TestServiceRouter.Builder();
  }

  public static class Builder extends ServiceRouter.Builder<TestServiceRouter.Builder, ITestService> {
    Builder() {
      super("/service/test");
    }

    public TestServiceRouter.Builder userId(int val) {
      super.putExtra("userId",val);
      return this;
    }

    public TestServiceRouter.Builder userName(String val) {
      super.putExtra("userName",val);
      return this;
    }
  }
}
