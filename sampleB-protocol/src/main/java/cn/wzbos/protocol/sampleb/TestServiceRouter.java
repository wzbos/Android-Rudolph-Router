package cn.wzbos.protocol.sampleb;

import cn.wzbos.android.rudolph.Rudolph;

/**
 * Generated code from Rudolph. Do not modify!
 */
public class TestServiceRouter {
  private static volatile ITestService instance;

  public static ITestService get() {
    if (instance == null) {
      synchronized (TestServiceRouter.class) {
        if (instance == null) {
          Object result = Rudolph.builder("/service/test").build().open();
          if(result instanceof ITestService) {
            instance = (ITestService)result;
          }
        }
        return instance;
      }
    }
    return instance;
  }
}
