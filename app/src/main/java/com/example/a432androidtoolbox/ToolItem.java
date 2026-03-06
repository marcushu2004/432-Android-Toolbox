package com.example.a432androidtoolbox;

public class ToolItem {
    private String title;
    private Class<?> targetActivity; // 存储 Activity 的类名

    public ToolItem(String title, Class<?> targetActivity) {
        this.title = title;
        this.targetActivity = targetActivity;
    }

    public String getTitle() { return title; }
    public Class<?> getTargetActivity() { return targetActivity; }
}