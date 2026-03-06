package com.example.a432androidtoolbox;

import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RamDetailActivity extends AppCompatActivity {
    private TextView tvPercent, tvDetails;
    private ProgressBar pbRam;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram_detail);

        tvPercent = findViewById(R.id.tv_ram_percent);
        tvDetails = findViewById(R.id.tv_ram_details);
        pbRam = findViewById(R.id.pb_ram);

        updateTask = new Runnable() {
            @Override
            public void run() {
                updateMemoryInfo();
                handler.postDelayed(this, 1000); // 每秒刷新
            }
        };
    }

    private void updateMemoryInfo() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        // 字节转为 MB (1024 * 1024)
        long totalMem = memoryInfo.totalMem / (1024 * 1024);
        long availMem = memoryInfo.availMem / (1024 * 1024);
        long usedMem = totalMem - availMem;

        // 计算占用百分比
        int percent = (int) ((usedMem / (float) totalMem) * 100);

        tvPercent.setText("占用率: " + percent + "%");
        pbRam.setProgress(percent);

        StringBuilder sb = new StringBuilder();
        sb.append("总内存: ").append(totalMem).append(" MB\n");
        sb.append("已用内存: ").append(usedMem).append(" MB\n");
        sb.append("可用内存: ").append(availMem).append(" MB\n");
        sb.append("低内存预警: ").append(memoryInfo.lowMemory ? "是 (警告!)" : "否");

        tvDetails.setText(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTask);
    }
}