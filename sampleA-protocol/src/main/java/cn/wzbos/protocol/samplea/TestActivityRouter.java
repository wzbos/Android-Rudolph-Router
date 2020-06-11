package cn.wzbos.protocol.samplea;

import android.util.Base64;
import cn.wzbos.android.rudolph.router.ActivityRouter;
import cn.wzbos.protocol.samplea.bean.Broker;
import cn.wzbos.protocol.samplea.bean.User;
import com.google.gson.Gson;
import java.lang.CharSequence;
import java.lang.Character;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;

/**
 * Generated code from Rudolph. Do not modify!
 */
public class TestActivityRouter {
  public static TestActivityRouter.Builder builder() {
    return new TestActivityRouter.Builder();
  }

  public static class Builder extends ActivityRouter.Builder<TestActivityRouter.Builder> {
    Builder() {
      super("/activity/test");
    }

    public TestActivityRouter.Builder stringArg(String val) {
      super.arg("stringArg",val);
      return this;
    }

    public TestActivityRouter.Builder string64Arg(String val) {
      String base64 = new String(Base64.encode(val.getBytes(),Base64.NO_PADDING|Base64.URL_SAFE));
      super.arg("string64",base64);
      return this;
    }

    public TestActivityRouter.Builder stringArrayArg(String[] val) {
      super.arg("stringArray",val);
      return this;
    }

    public TestActivityRouter.Builder boolArg(boolean val) {
      super.arg("boolArg",val);
      return this;
    }

    public TestActivityRouter.Builder booleanArrayArg(boolean[] val) {
      super.arg("booleanArray",val);
      return this;
    }

    public TestActivityRouter.Builder byteArg(byte val) {
      super.arg("byteArg",val);
      return this;
    }

    public TestActivityRouter.Builder byteArrayArg(byte[] val) {
      super.arg("byteArray",val);
      return this;
    }

    public TestActivityRouter.Builder shortArg(short val) {
      super.arg("shortArg",val);
      return this;
    }

    public TestActivityRouter.Builder shortArrayArg(short[] val) {
      super.arg("shortArray",val);
      return this;
    }

    public TestActivityRouter.Builder intArg(int val) {
      super.arg("intArg",val);
      return this;
    }

    public TestActivityRouter.Builder intArrayArg(int[] val) {
      super.arg("intArrayArg",val);
      return this;
    }

    public TestActivityRouter.Builder longArg(long val) {
      super.arg("longArg",val);
      return this;
    }

    public TestActivityRouter.Builder longArrayArg(long[] val) {
      super.arg("longArray",val);
      return this;
    }

    public TestActivityRouter.Builder charArg(char val) {
      super.arg("charArg",val);
      return this;
    }

    public TestActivityRouter.Builder charArrayArg(char[] val) {
      super.arg("charArray",val);
      return this;
    }

    public TestActivityRouter.Builder floatArg(float val) {
      super.arg("floatArg",val);
      return this;
    }

    public TestActivityRouter.Builder floatArrayArg(float[] val) {
      super.arg("floatArray",val);
      return this;
    }

    public TestActivityRouter.Builder doubleArg(double val) {
      super.arg("doubleArg",val);
      return this;
    }

    public TestActivityRouter.Builder doubleArrayArg(double[] val) {
      super.arg("doubleArray",val);
      return this;
    }

    public TestActivityRouter.Builder characterArg(Character val) {
      super.arg("characterArg",val);
      return this;
    }

    public TestActivityRouter.Builder stringArrayListArg(ArrayList<String> val) {
      super.stringArrayListArg("stringArrayListArg",val);
      return this;
    }

    public TestActivityRouter.Builder integerArrayListArg(ArrayList<Integer> val) {
      super.integerArrayListArg("integerArrayListArg",val);
      return this;
    }

    public TestActivityRouter.Builder charSequenceArrayListArg(ArrayList<CharSequence> val) {
      super.charSequenceArrayListArg("charSequenceArrayList",val);
      return this;
    }

    public TestActivityRouter.Builder parcelableArrayListArg(ArrayList<Broker> val) {
      super.parcelableArrayListArg("parcelableArrayList",val);
      return this;
    }

    public TestActivityRouter.Builder serializedParam(Broker val) {
      super.arg("serialized",val);
      return this;
    }

    public TestActivityRouter.Builder jsonParam(User<Broker> val) {
      String json = new Gson().toJson(val);
      super.arg("json",json);
      return this;
    }

    public TestActivityRouter.Builder base64jsonParam(User<Broker> val) {
      String json = new Gson().toJson(val);
      String base64 = new String(Base64.encode(json.getBytes(),Base64.NO_PADDING|Base64.URL_SAFE));
      super.arg("base64json",base64);
      return this;
    }
  }
}
