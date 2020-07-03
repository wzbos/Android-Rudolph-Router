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
      super.putExtra("stringArg",val);
      return this;
    }

    public TestActivityRouter.Builder string64Arg(String val) {
      String base64 = new String(Base64.encode(val.getBytes(),Base64.NO_PADDING|Base64.URL_SAFE));
      super.putExtra("string64",base64);
      return this;
    }

    public TestActivityRouter.Builder stringArrayArg(String[] val) {
      super.putExtra("stringArray",val);
      return this;
    }

    public TestActivityRouter.Builder boolArg(boolean val) {
      super.putExtra("boolArg",val);
      return this;
    }

    public TestActivityRouter.Builder booleanArrayArg(boolean[] val) {
      super.putExtra("booleanArray",val);
      return this;
    }

    public TestActivityRouter.Builder byteArg(byte val) {
      super.putExtra("byteArg",val);
      return this;
    }

    public TestActivityRouter.Builder byteArrayArg(byte[] val) {
      super.putExtra("byteArray",val);
      return this;
    }

    public TestActivityRouter.Builder shortArg(short val) {
      super.putExtra("shortArg",val);
      return this;
    }

    public TestActivityRouter.Builder shortArrayArg(short[] val) {
      super.putExtra("shortArray",val);
      return this;
    }

    public TestActivityRouter.Builder intArg(int val) {
      super.putExtra("intArg",val);
      return this;
    }

    public TestActivityRouter.Builder intArrayArg(int[] val) {
      super.putExtra("intArrayArg",val);
      return this;
    }

    public TestActivityRouter.Builder longArg(long val) {
      super.putExtra("longArg",val);
      return this;
    }

    public TestActivityRouter.Builder longArrayArg(long[] val) {
      super.putExtra("longArray",val);
      return this;
    }

    public TestActivityRouter.Builder charArg(char val) {
      super.putExtra("charArg",val);
      return this;
    }

    public TestActivityRouter.Builder charArrayArg(char[] val) {
      super.putExtra("charArray",val);
      return this;
    }

    public TestActivityRouter.Builder floatArg(float val) {
      super.putExtra("floatArg",val);
      return this;
    }

    public TestActivityRouter.Builder floatArrayArg(float[] val) {
      super.putExtra("floatArray",val);
      return this;
    }

    public TestActivityRouter.Builder doubleArg(double val) {
      super.putExtra("doubleArg",val);
      return this;
    }

    public TestActivityRouter.Builder doubleArrayArg(double[] val) {
      super.putExtra("doubleArray",val);
      return this;
    }

    public TestActivityRouter.Builder characterArg(Character val) {
      super.putExtra("characterArg",val);
      return this;
    }

    public TestActivityRouter.Builder stringArrayListArg(ArrayList<String> val) {
      super.putStringArrayListExtra("stringArrayListArg",val);
      return this;
    }

    public TestActivityRouter.Builder integerArrayListArg(ArrayList<Integer> val) {
      super.putIntegerArrayListExtra("integerArrayListArg",val);
      return this;
    }

    public TestActivityRouter.Builder charSequenceArrayListArg(ArrayList<CharSequence> val) {
      super.putCharSequenceArrayListExtra("charSequenceArrayList",val);
      return this;
    }

    public TestActivityRouter.Builder parcelableArrayListArg(ArrayList<Broker> val) {
      super.putParcelableArrayListExtra("parcelableArrayList",val);
      return this;
    }

    public TestActivityRouter.Builder serializedParam(Broker val) {
      super.putExtra("serialized",val);
      return this;
    }

    public TestActivityRouter.Builder jsonParam(User<Broker> val) {
      String json = new Gson().toJson(val);
      super.putExtra("json",json);
      return this;
    }

    public TestActivityRouter.Builder base64jsonParam(User<Broker> val) {
      String json = new Gson().toJson(val);
      String base64 = new String(Base64.encode(json.getBytes(),Base64.NO_PADDING|Base64.URL_SAFE));
      super.putExtra("base64json",base64);
      return this;
    }
  }
}
