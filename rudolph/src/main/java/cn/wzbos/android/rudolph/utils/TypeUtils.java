package cn.wzbos.android.rudolph.utils;

import android.content.Context;


import cn.wzbos.android.rudolph.RLog;
import cn.wzbos.android.rudolph.router.RouteBuilder;


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

    public static Object getObject(Context context, String name, String value, String target) {
        return getObject(context, name, value, target, null);
    }

    public static Object getObject(Context context, String name, String value, String target, RouteBuilder<?, ?> builder) {
        RLog.d("TypeUtils", name + " = " + value + ", target:" + target + ", bundle:" + builder);

        if ("java.lang.String".equals(target) || "java.lang.CharSequence".equals(target)) {  //String、CharSequence
            if (builder != null)
                builder.putExtra(name, value);
            return value;
        } else if ("int".equals(target) || "java.lang.Integer".equals(target)) {    //int、Integer
            int val = toInteger(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if ("boolean".equals(target) || "java.lang.Boolean".equals(target)) {//boolean、Boolean
            boolean val = toBoolean(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if ("double".equals(target) || "java.lang.Double".equals(target)) {  //double、Double
            double val = toDouble(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if ("float".equals(target) || "java.lang.Float".equals(target)) {    //float、Float
            float val = toFloat(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if ("long".equals(target) || "java.lang.Long".equals(target)) {      //long、Long
            long val = toLong(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if ("short".equals(target) || "java.lang.Short".equals(target)) {    //short、Short
            short val = toShort(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if ("byte".equals(target) || "java.lang.Byte".equals(target)) {      //byte、Byte
            byte val = toByte(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if ("char".equals(target) || "java.lang.Character".equals(target)) { //char、Character
            char val = toChar(value);
            if (builder != null)
                builder.putExtra(name, val);
            return val;
        } else if ("android.content.Context".equals(target)) {                         //Context
            return context;
        } else {                                                    //Object
            if (builder != null)
                builder.putExtra(name, value);
            return value;
        }
    }
}
