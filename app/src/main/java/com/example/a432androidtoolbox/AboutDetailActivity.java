package com.example.a432androidtoolbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AboutDetailActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // 如果你想动态设置文字，可以在这里使用 findViewById(R.id.tv_about_content).setText(...)
    }
}
