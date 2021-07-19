package cn.wzbos.android.rudolph.example;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cn.wzbos.android.rudolph.Rudolph;
import cn.wzbos.android.rudolph.annotations.Route;
import cn.wzbos.samplea.MainActivityRouter;

@SuppressLint("CustomSplashScreen")
@Route
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Uri uri = getIntent().getData();
        if (uri != null) {
            Rudolph.builder(uri).delayFinish().execute(SplashActivity.this);
        } else {
            MainActivityRouter.builder().delayFinish().buildStart(this);
        }
    }

}
