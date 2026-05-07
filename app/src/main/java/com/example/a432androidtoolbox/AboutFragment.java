package com.example.a432androidtoolbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays; // 手动导入
import java.util.List;   // 同时也需要导入 List
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a432androidtoolbox.MainAdapter;
import com.example.a432androidtoolbox.R;

import java.sql.Array;
import java.util.List;

public class AboutFragment extends Fragment {
    // 建议统一使用这个类成员变量
    private RecyclerView rvAbout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // 修复：将查找到的 View 赋值给 rvAbout，而不是局部变量 rv
        rvAbout = view.findViewById(R.id.rv_list);

        // 核心防御：防止 XML 里的 ID 写错导致 rvAbout 依然为 null
        if (rvAbout != null) {
            rvAbout.setLayoutManager(new LinearLayoutManager(getContext()));

            // 1. 准备数据
            List<ToolItem> aboutItems = new ArrayList<>();
            // 合并你的“作者的话”和“应用信息”
            aboutItems.add(new ToolItem("作者的话", AboutDetailActivity.class));
            aboutItems.add(new ToolItem("应用名称：432 智能工具箱", null));
            aboutItems.add(new ToolItem("当前版本：Inside v3", null));
            aboutItems.add(new ToolItem("开发者：Marcus Hu", null));
            aboutItems.add(new ToolItem("开源协议：暂无", null));

            // 2. 绑定适配器
            MainAdapter adapter = new MainAdapter(aboutItems, position -> {
                ToolItem item = aboutItems.get(position);
                if (item.getTargetActivity() != null) {
                    // 如果有目标 Activity（如作者的话），执行跳转
                    startActivity(new android.content.Intent(getActivity(), item.getTargetActivity()));
                } else {
                    // 否则只弹 Toast
                    Toast.makeText(getActivity(), "信息：" + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

            // 现在的 rvAbout 已经不是 null 了
            rvAbout.setAdapter(adapter);
        }

        return view;
    }
}