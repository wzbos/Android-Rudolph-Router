package cn.wzbos.samplea;

import android.app.Application;
import android.widget.Toast;

import cn.wzbos.android.rudolph.annotations.Component;
import cn.wzbos.android.rudolph.IRouteTable;

@Component
public class TestComponent implements IRouteTable {

    @Override
    public void init(Application application) {
        Toast.makeText(application.getApplicationContext(), "Module A initialized！", Toast.LENGTH_SHORT).show();
    }
}
