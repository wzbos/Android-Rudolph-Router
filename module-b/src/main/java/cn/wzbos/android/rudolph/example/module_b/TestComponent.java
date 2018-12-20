package cn.wzbos.android.rudolph.example.module_b;

import android.app.Application;
import android.widget.Toast;

import cn.wzbos.android.rudolph.IRouteTable;
import cn.wzbos.android.rudolph.annotations.Component;

@Component
public class TestComponent implements IRouteTable {

    @Override
    public void init(Application application) {
        Toast.makeText(application.getApplicationContext(), "Module B initialized！", Toast.LENGTH_SHORT).show();
    }
}
