package cn.wzbos.rudolph.example.module_a;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.wzbos.rudolph.Rudolph;
import cn.wzbos.rudolph.annotations.Arg;
import cn.wzbos.rudolph.annotations.Route;
import cn.wzbos.rudolph.example.module_a_api.bean.Broker;
import cn.wzbos.rudolph.example.module_a_api.bean.User;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static cn.wzbos.rudolph.Consts.RAW_URI;


@Route(value = "/activity/test", export = true)
public class TestActivity extends AppCompatActivity {
    @Arg(RAW_URI)
    String routeUri;

    @Arg("string")
    String stringArg;

    @Arg(value = "string64", base64 = true)
    String string64Arg;

    @Arg("stringArray")
    String[] stringArrayArg;


    @Arg("boolArg")
    boolean boolArg;

    @Arg("booleanArray")
    boolean[] booleanArrayArg;

    @Arg("byteArg")
    byte byteArg;

    @Arg("byteArray")
    byte[] byteArrayArg;

    @Arg("shortArg")
    short shortArg;

    @Arg("shortArray")
    short[] shortArrayArg;

    @Arg("intArg")
    int intArg;

    @Arg("intArrayArg")
    int[] intArrayArg;

    @Arg("longArg")
    long longArg;

    @Arg("longArray")
    long[] longArrayArg;

    @Arg("charArg")
    char charArg;

    @Arg("charArray")
    char[] charArrayArg;

    @Arg("floatArg")
    float floatArg;

    @Arg("floatArray")
    float[] floatArrayArg;

    @Arg("doubleArg")
    double doubleArg;

    @Arg("doubleArray")
    double[] doubleArrayArg;

    @Arg("characterArg")
    Character characterArg;

    //ArrayList
    @Arg
    ArrayList<String> stringArrayListArg;

    @Arg
    ArrayList<Integer> integerArrayListArg;

    @Arg(value = "charSequenceArrayList")
    ArrayList<CharSequence> charSequenceArrayListArg;

    @Arg(value = "parcelableArrayList")
    ArrayList<Broker> parcelableArrayListArg;

    @Arg(value = "serialized")
    Broker serializedParam;

    //json
    @Arg(value = "json", json = true)
    User<Broker> jsonParam;

    //encode：json->base64，decode：base64->json
    @Arg(value = "base64json", json = true, base64 = true)
    User<Broker> base64jsonParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        TextView tvMessage = findViewById(R.id.tvMessage);

        Rudolph.bind(this);

        StringBuffer stringBuffer = new StringBuffer();
        for (Field filed : this.getClass().getDeclaredFields()) {
            try {
                stringBuffer.append(filed.getName()).append("=").append(filed.get(this)).append("\n");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        tvMessage.setText(stringBuffer);

    }
}