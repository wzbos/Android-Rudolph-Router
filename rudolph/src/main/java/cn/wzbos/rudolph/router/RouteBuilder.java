package cn.wzbos.rudolph.router;


import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import cn.wzbos.rudolph.Rudolph;
import cn.wzbos.rudolph.Consts;
import cn.wzbos.rudolph.IRouteBuilder;
import cn.wzbos.rudolph.RouteCallback;
import cn.wzbos.rudolph.RouteInfo;
import cn.wzbos.rudolph.RouteType;
import cn.wzbos.rudolph.utils.TypeUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bundle IBuilder
 * Created by wuzongbo on 2017/9/13.
 */
@SuppressWarnings("unchecked")
public abstract class RouteBuilder<B extends RouteBuilder<B, R>, R extends Router> implements IRouteBuilder<RouteBuilder<B, R>, R> {


    Bundle args;

    RouteCallback callback;

    Class<?> target;

    String rawUrl;

    String routePath;

    RouteType routeType;

    String routeTag;

    Map<String, Type> queryParameters;


    @Override
    public B arg(Bundle map) {
        args.putAll(map);
        return (B) this;
    }

    @Override
    public B arg(String key, String value) {
        args.putString(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, boolean value) {
        args.putBoolean(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, short value) {
        args.putShort(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, int value) {
        args.putInt(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, long value) {
        args.putLong(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, double value) {
        args.putDouble(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, byte value) {
        args.putByte(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, char value) {
        args.putChar(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, float value) {
        args.putFloat(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, CharSequence value) {
        args.putCharSequence(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, Parcelable value) {
        args.putParcelable(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, Parcelable[] value) {
        args.putParcelableArray(key, value);
        return (B) this;
    }

    @Override
    public B parcelableArrayListArg(String key, ArrayList<? extends Parcelable> value) {
        args.putParcelableArrayList(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, SparseArray<? extends Parcelable> value) {
        args.putSparseParcelableArray(key, value);
        return (B) this;
    }

    @Override
    public B integerArrayListArg(String key, ArrayList<Integer> value) {
        args.putIntegerArrayList(key, value);
        return (B) this;
    }

    @Override
    public B stringArrayListArg(String key, ArrayList<String> value) {
        args.putStringArrayList(key, value);
        return (B) this;
    }

    @Override
    public B charSequenceArrayListArg(String key, ArrayList<CharSequence> value) {
        args.putCharSequenceArrayList(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, Serializable value) {
        args.putSerializable(key, value);
        return (B) this;
    }

    @Override
    public B arg(String name, boolean[] value) {
        args.putBooleanArray(name, value);
        return (B) this;
    }

    @Override
    public B arg(String key, byte[] value) {
        args.putByteArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, short[] value) {
        args.putShortArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, char[] value) {
        args.putCharArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, int[] value) {
        args.putIntArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, long[] value) {
        args.putLongArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, float[] value) {
        args.putFloatArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, double[] value) {
        args.putDoubleArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, String[] value) {
        args.putStringArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, CharSequence[] value) {
        args.putCharSequenceArray(key, value);
        return (B) this;
    }

    @Override
    public B arg(String key, Bundle value) {
        args.putBundle(key, value);
        return (B) this;
    }

    @Override
    public Bundle args() {
        return args;
    }

    @Override
    public B callback(RouteCallback callback) {
        this.callback = callback;
        return (B) this;
    }

    @Override
    public B url(String url) {
        this.rawUrl = url;
        return (B) this;
    }


    private RouteBuilder() {
        this.args = new Bundle();
    }

    RouteBuilder(Class<?> target) {
        this();
        this.target = target;
    }


    RouteBuilder(String rawUrl) {
        this();
        this.rawUrl = rawUrl;

        if (!TextUtils.isEmpty(rawUrl)) {
            RouteInfo routeInfo = Rudolph.getRouter(getPath());
            if (null == routeInfo) {
                Log.e("Rudolph", "没有匹配到相关路由,Path:" + rawUrl);
                return;
            }

            this.target = routeInfo.getTarget();
            this.routePath = routeInfo.getPath();
            this.routeTag = routeInfo.getTag();
            this.routeType = routeInfo.getRouteType();
            this.queryParameters = routeInfo.getParams();

            //返回的所有的地址参数与查询参数值
            Map<String, String> resultMap = getUriAllParams();

            if (resultMap != null) {
                //页面接收参数
                for (Map.Entry<String, String> kv : resultMap.entrySet()) {
                    String key = kv.getKey();
                    String val = kv.getValue();
                    addParams(key, val);
                }
            }
        }
    }


    public B addParams(String key, String val) {
        if (queryParameters != null && !queryParameters.isEmpty() && queryParameters.containsKey(key)) {
            Type type = queryParameters.get(key);
            if (!android.text.TextUtils.isEmpty(val)) {
                TypeUtils.getObject(null, key, val, type, this);
            }
        } else {
            args.putString(key, val);
        }
        return (B) this;
    }

    String getPath() {
        int n = rawUrl.indexOf("://");
        int stx, etx;
        if (n > -1) {
            stx = n + 3;
            etx = rawUrl.indexOf("?", stx);
        } else {
            stx = 0;
            etx = rawUrl.indexOf("?");
        }

        String path;
        if (etx > -1) {
            path = rawUrl.substring(stx, etx);
        } else {
            path = rawUrl.substring(stx);
        }
        return path;
    }

    private List<String> getSegments() {
        List<String> segments = new ArrayList<>();
        String[] values = getPath().split("/");
        for (String val : values) {
            try {
                if (!TextUtils.isEmpty(val)) {
                    segments.add(URLDecoder.decode(val, "utf-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return segments;
    }

    private String getEncodedQuery() {
        int n = this.rawUrl.indexOf("?");
        if (n > -1) {
            return this.rawUrl.substring(n + 1);
        } else {
            return null;
        }
    }

    Map<String, String> getUriAllParams() {
        HashMap<String, String> params = new LinkedHashMap<>();

        String[] routeSegments = routePath.substring(1).split("/");

        List<String> pathSegments = getSegments();

        //segments个数不匹配
        if (routeSegments.length != pathSegments.size())
            return null;
        //raw uri
        params.put(Consts.RAW_URI, this.rawUrl);
        //path params
        for (int i = 0; i < routeSegments.length; i++) {
            if (routeSegments[i].startsWith(":")) {
                //put path params
                params.put(routeSegments[i].substring(1), pathSegments.get(i));
                continue;
            }

            if (!routeSegments[i].equalsIgnoreCase(pathSegments.get(i)))
                return null;
        }

        //put query params
        String query = getEncodedQuery();
        if (!TextUtils.isEmpty(query)) {
            String[] queryParameters = query.split("&");
            for (String str : queryParameters) {

                String[] kv = str.split("=");

                if (kv.length == 2) {

                    try {
                        String name = URLDecoder.decode(kv[0], "utf-8");
                        String value = URLDecoder.decode(kv[1], "utf-8");
                        params.put(name, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return params;
    }

}
