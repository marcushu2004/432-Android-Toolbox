package com.example.a432androidtoolbox;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.RandomAccessFile;

public class CpuDetailActivity extends AppCompatActivity {
    private TextView tvCpuList;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 需手动创建对应的 activity_cpu_detail.xml
        setContentView(R.layout.activity_cpu_detail);
        tvCpuList = findViewById(R.id.tv_cpu_detail_content);

        runnable = new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append("--- CPU 核心频率监控 ---\n\n");
                // 假设最多 8 核，嵌入式常见的循环读取节点方式
                for (int i = 0; i < 8; i++) {
                    String freq = readNode("/sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq");
                    if (!freq.equals("N/A")) {
                        sb.append("Core ").append(i).append(": ").append(freq).append(" MHz\n");
                    }
                }
                tvCpuList.setText(sb.toString());
                handler.postDelayed(this, 1000); // 1秒轮询一次
            }
        };
        handler.post(runnable);
    }

    private String readNode(String path) {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            String line = raf.readLine();
            raf.close();
            if (line != null) return String.valueOf(Integer.parseInt(line.trim()) / 1000);
        } catch (Exception e) { return "N/A"; }
        return "N/A";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // 销毁时停止线程，防止内存泄漏
    }
}