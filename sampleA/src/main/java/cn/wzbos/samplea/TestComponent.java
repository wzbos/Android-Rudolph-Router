package cn.wzbos.samplea;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import cn.wzbos.android.rudolph.IRouteComponent;
import cn.wzbos.android.rudolph.annotations.Component;

@Component
public class TestComponent implements IRouteComponent {

    @Override
    public void init(@NotNull Context context) {
        Toast.makeText(context.getApplicationContext(), "Module A initialized！", Toast.LENGTH_SHORT).show();
    }
}
