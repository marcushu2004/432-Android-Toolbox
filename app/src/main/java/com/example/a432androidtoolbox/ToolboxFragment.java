package com.example.a432androidtoolbox;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a432androidtoolbox.MainAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ToolboxFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toolbox, container, false);
        RecyclerView rv = view.findViewById(R.id.rv_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ToolItem> list = new ArrayList<>();
        list.add(new ToolItem("指南针",CompassActivity.class));
        list.add(new ToolItem("水平仪",SpiritLevelDetailActivity.class));
        list.add(new ToolItem("镜子",MirrorActivity.class));
        //list.add(new ToolItem("手电筒",.class));

        MainAdapter adapter = new MainAdapter(list, position -> {
            // 获取对应的 Class 跳转
            Class<?> clazz = list.get(position).getTargetActivity();
            startActivity(new Intent(getActivity(), clazz));
        });
        rv.setAdapter(adapter);
        return view;

    }
}