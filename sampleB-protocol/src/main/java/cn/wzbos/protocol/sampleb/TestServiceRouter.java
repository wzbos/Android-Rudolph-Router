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
          instance = newInstance();
        }
        return instance;
      }
    }
    return instance;
  }

  /**
   * create new instance
   */
  public static ITestService newInstance() {
    return (ITestService)Rudolph.builder("/service/test").build().open();
  }
}
