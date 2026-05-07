package com.example.a432androidtoolbox;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class NetworkDetailActivity extends AppCompatActivity {
    private TextView tvNetType, tvSpeedDown, tvSpeedUp, tvIp;
    private Handler handler = new Handler(Looper.getMainLooper());

    private long lastTotalRxBytes = 0;
    private long lastTotalTxBytes = 0;
    private long lastTimeStamp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_detail);

        tvNetType = findViewById(R.id.tv_net_type);
        tvSpeedDown = findViewById(R.id.tv_net_speed_down);
        tvSpeedUp = findViewById(R.id.tv_net_speed_up);
        tvIp = findViewById(R.id.tv_net_ip);

        lastTotalRxBytes = TrafficStats.getTotalRxBytes();
        lastTotalTxBytes = TrafficStats.getTotalTxBytes();
        lastTimeStamp = System.currentTimeMillis();

        handler.post(speedTask);
    }

    private Runnable speedTask = new Runnable() {
        @Override
        public void run() {
            updateNetworkInfo();
            handler.postDelayed(this, 1000);
        }
    };

    private void updateNetworkInfo() {
        // 1. 获取网络类型
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

        String type = "未连接";
        if (nc != null) {
            if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) type = "Wi-Fi 局域网";
            else if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) type = "移动数据网络";
        }
        tvNetType.setText("连接类型: " + type);

        // 2. 计算实时网速 (ΔBytes / ΔTime)
        long currentTotalRxBytes = TrafficStats.getTotalRxBytes();
        long currentTotalTxBytes = TrafficStats.getTotalTxBytes();
        long currentTimeStamp = System.currentTimeMillis();

        long timeInterval = (currentTimeStamp - lastTimeStamp) / 1000;
        if (timeInterval == 0) timeInterval = 1;

        long speedDown = (currentTotalRxBytes - lastTotalRxBytes) / timeInterval / 1024; // KB/s
        long speedUp = (currentTotalTxBytes - lastTotalTxBytes) / timeInterval / 1024;   // KB/s

        tvSpeedDown.setText(String.format("下载速度: %d KB/s", speedDown));
        tvSpeedUp.setText(String.format("上传速度: %d KB/s", speedUp));

        lastTotalRxBytes = currentTotalRxBytes;
        lastTotalTxBytes = currentTotalTxBytes;
        lastTimeStamp = currentTimeStamp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(speedTask);
    }
}