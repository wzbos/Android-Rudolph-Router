package cn.wzbos.rudolph.example.module_a;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.wzbos.rudolph.Rudolph;
import cn.wzbos.rudolph.Consts;
import cn.wzbos.rudolph.annotations.Arg;
import cn.wzbos.rudolph.annotations.Route;

import java.util.Locale;


@Route("/fragment/test")
public class TestFragment extends Fragment {
    @Arg(Consts.RAW_URI)
    String routeUri;

    @Arg("userId")
    int userId;

    @Arg("userName")
    String userName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);

        Rudolph.bind(this);

        ((TextView) rootView.findViewById(R.id.tvMessage)).setText(String.format(Locale.getDefault(),
                "RawUri:%s\nUserId:%d\nUserName:%s",
                routeUri, userId, userName));
        return rootView;
    }
}
