package cn.wzbos.rudolph.example.module_b;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cn.wzbos.rudolph.Rudolph;
import cn.wzbos.rudolph.annotations.Arg;
import cn.wzbos.rudolph.annotations.Route;

import java.util.Locale;

import static cn.wzbos.rudolph.Consts.RAW_URI;

@Route(value = "/user", export = true)
public class UserActivity extends AppCompatActivity {
    @Arg(RAW_URI)
    String routeUri;

    @Arg("userId")
    int userId;

    @Arg("userName")
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        Rudolph.bind(this);

        ((TextView) findViewById(R.id.tvMessage)).setText(String.format(Locale.getDefault(),
                "RawUri:%s\nuserId:%d,userName:%s", routeUri, userId,userName));
    }
}