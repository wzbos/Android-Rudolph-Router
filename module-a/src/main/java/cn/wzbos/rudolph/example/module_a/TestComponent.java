package cn.wzbos.rudolph.example.module_a;

import android.app.Application;
import android.widget.Toast;

import cn.wzbos.rudolph.annotations.Component;
import cn.wzbos.rudolph.IRouteTable;

@Component
public class TestComponent implements IRouteTable {

    @Override
    public void init(Application application) {
        Toast.makeText(application.getApplicationContext(), "Module A initialized！", Toast.LENGTH_SHORT).show();
    }
}
