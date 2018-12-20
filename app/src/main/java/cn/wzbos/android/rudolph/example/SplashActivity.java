package cn.wzbos.android.rudolph.example;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.example.module_a.MainActivityRouter;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Uri uri = getIntent().getData();
        if (uri != null) {
            Rudolph.builder(uri).build().open(SplashActivity.this);
        } else {
            MainActivityRouter.builder().build().start(this);
        }
        finish();
    }

}
