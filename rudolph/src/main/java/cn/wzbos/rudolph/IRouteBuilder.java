package cn.wzbos.rudolph;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import cn.wzbos.rudolph.router.Router;

import java.io.Serializable;
import java.util.ArrayList;

public interface IRouteBuilder<B extends IRouteBuilder<B, R>, R extends Router> {

    B arg(Bundle map);

    B arg(String key, String value);

    B arg(String key, boolean value);

    B arg(String key, short value);

    B arg(String key, int value);

    B arg(String key, long value);

    B arg(String key, double value);

    B arg(String key, byte value);

    B arg(String key, char value);

    B arg(String key, float value);

    B arg(String key, CharSequence value);

    B arg(String key, Parcelable value);

    B arg(String key, Parcelable[] value);

    B parcelableArrayListArg(String key, ArrayList<? extends Parcelable> value);

    B arg(String key, SparseArray<? extends Parcelable> value);

    B integerArrayListArg(String key, ArrayList<Integer> value);

    B stringArrayListArg(String key, ArrayList<String> value);

    B charSequenceArrayListArg(String key, ArrayList<CharSequence> value);

    B arg(String key, Serializable value);

    B arg(String name, boolean[] value);

    B arg(String key, byte[] value);

    B arg(String key, short[] value);

    B arg(String key, char[] value);

    B arg(String key, int[] value);

    B arg(String key, long[] value);

    B arg(String key, float[] value);

    B arg(String key, double[] value);

    B arg(String key, String[] value);

    B arg(String key, CharSequence[] value);

    B arg(String key, Bundle value);

    Bundle args();

    B callback(RouteCallback callback);

    R build();

    B url(String url);

    B addParams(String key, String val);
}