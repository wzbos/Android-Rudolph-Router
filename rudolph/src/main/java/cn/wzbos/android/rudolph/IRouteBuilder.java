package cn.wzbos.android.rudolph;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import cn.wzbos.android.rudolph.router.Router;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface IRouteBuilder<B extends IRouteBuilder<B, R>, R extends Router<?>> {

    B putExtra(Bundle map);

    B putExtra(String key, String value);

    B putExtra(String key, boolean value);

    B putExtra(String key, short value);

    B putExtra(String key, int value);

    B putExtra(String key, long value);

    B putExtra(String key, double value);

    B putExtra(String key, byte value);

    B putExtra(String key, char value);

    B putExtra(String key, float value);

    B putExtra(String key, CharSequence value);

    B putExtra(String key, Parcelable value);

    B putExtra(String key, Parcelable[] value);

    B putParcelableArrayListExtra(String key, ArrayList<? extends Parcelable> value);

    B putExtra(String key, SparseArray<? extends Parcelable> value);

    B putIntegerArrayListExtra(String key, ArrayList<Integer> value);

    B putStringArrayListExtra(String key, ArrayList<String> value);

    B putCharSequenceArrayListExtra(String key, ArrayList<CharSequence> value);

    B putExtra(String key, Serializable value);

    B putExtra(String name, boolean[] value);

    B putExtra(String key, byte[] value);

    B putExtra(String key, short[] value);

    B putExtra(String key, char[] value);

    B putExtra(String key, int[] value);

    B putExtra(String key, long[] value);

    B putExtra(String key, float[] value);

    B putExtra(String key, double[] value);

    B putExtra(String key, String[] value);

    B putExtra(String key, CharSequence[] value);

    B putExtra(String key, Bundle value);

    /**
     * 获取所有参数
     *
     * @return Bundle
     */
    Bundle getExtras();

    /**
     * 设置callback
     *
     * @param callback RouteCallback
     * @return IRouteBuilder
     */
    B onListener(OnRouteListener callback);

    /**
     * 生成Router
     *
     * @return Router
     */
    R build();
}