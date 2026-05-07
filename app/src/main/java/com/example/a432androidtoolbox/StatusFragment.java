package com.example.a432androidtoolbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        RecyclerView rv = view.findViewById(R.id.rv_list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ToolItem> list = new ArrayList<>();
        list.add(new ToolItem("CPU 频率",CpuDetailActivity.class));
        list.add(new ToolItem("RAM 状态",RamDetailActivity.class));
        list.add(new ToolItem("电池信息",BatteryDetailActivity.class));
        list.add(new ToolItem("ROM 信息",RomDetailActivity.class));
        list.add(new ToolItem("网络信息",NetworkDetailActivity.class));

        MainAdapter adapter = new MainAdapter(list, position -> {
            // 获取对应的 Class 跳转
            Class<?> clazz = list.get(position).getTargetActivity();
            startActivity(new Intent(getActivity(), clazz));
        });
        rv.setAdapter(adapter);
        return view;
    }
}