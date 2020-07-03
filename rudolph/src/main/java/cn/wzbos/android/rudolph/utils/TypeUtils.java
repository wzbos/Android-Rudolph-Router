package cn.wzbos.android.rudolph.utils;

import android.content.Context;
import android.util.Log;

import cn.wzbos.android.rudolph.router.RouteBuilder;

import java.lang.reflect.Type;


/**
 * Created by wuzongbo on 2017/6/21.
 */
public class TypeUtils {

    private static int toInteger(String value) {
        int val = 0;
        if (!android.text.TextUtils.isEmpty(value)) {
            if (value.toLowerCase().startsWith("0x")) {
                val = Integer.valueOf(value.substring(2), 16);
            } else {
                val = Integer.parseInt(value);
            }
        }
        return val;
    }

    private static boolean toBoolean(String value) {
        boolean val = false;
        if (!android.text.TextUtils.isEmpty(value)) {
            if (value.toLowerCase().equals("true") || value.toLowerCase().equals("false")) {
                val = Boolean.parseBoolean(value);
            } else {
                val = Integer.parseInt(value) > 0;
            }
        }
        return val;
    }


    private static double toDouble(String value) {
        double val = 0;
        if (!android.text.TextUtils.isEmpty(value))
            val = Double.parseDouble(value);
        return val;
    }

    private static float toFloat(String value) {
        float val = 0;
        if (!android.text.TextUtils.isEmpty(value))
            val = Float.parseFloat(value);

        return val;
    }

    private static long toLong(String value) {
        long val = 0;
        if (!android.text.TextUtils.isEmpty(value))
            val = Long.parseLong(value);
        return val;
    }

    private static short toShort(String value) {
        short val = 0;
        if (!android.text.TextUtils.isEmpty(value)) {
            if (value.toLowerCase().startsWith("0x")) {
                val = Short.valueOf(value.substring(2), 16);
            } else {
                val = Short.parseShort(value);
            }
        }
        return val;
    }

    private static byte toByte(String value) {
        byte val = 0;
        if (!android.text.TextUtils.isEmpty(value)) {
            if (value.toLowerCase().startsWith("0x")) {
                val = Byte.valueOf(value.substring(2), 16);
            } else {
                val = Byte.parseByte(value);
            }
        }
        return val;
    }

    private static char toChar(String value) {
        char val = 0;
        if (!android.text.TextUtils.isEmpty(value))
            val = value.charAt(0);
        return val;
    }

    public static Object getObject(Context context, String name, String value, Type type) {
        return getObject(context, name, value, type, null);
    }

    public static Object getObject(Context context, String name, String value, Type type, RouteBuilder<?,?> builder) {
        Log.d("TypeUtils", name + " = " + value + ", type:" + type + ", bundle:" + builder);

        if (type == String.class || type == CharSequence.class) {  //String、CharSequence
            if (builder != null)
                builder.putExtra(name, value);
            return value;
        } else if (type == int.class || type == Integer.class) {    //int、Integer
            int val = toInteger(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if (type == boolean.class || type == Boolean.class) {//boolean、Boolean
            boolean val = toBoolean(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if (type == double.class || type == Double.class) {  //double、Double
            double val = toDouble(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if (type == float.class || type == Float.class) {    //float、Float
            float val = toFloat(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if (type == long.class || type == Long.class) {      //long、Long
            long val = toLong(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if (type == short.class || type == Short.class) {    //short、Short
            short val = toShort(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if (type == byte.class || type == Byte.class) {      //byte、Byte
            byte val = toByte(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if (type == char.class || type == Character.class) { //char、Character
            char val = toChar(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if (type == Context.class) {                         //Context
            return context;
        } else {                                                    //Object
            if (builder != null)
                builder.putExtra(name, value);
            return value;
        }
    }
}
