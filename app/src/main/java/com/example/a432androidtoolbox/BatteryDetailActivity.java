package com.example.a432androidtoolbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BatteryDetailActivity extends AppCompatActivity {
    private TextView tvBatteryInfo;

    // 定义广播接收器，当电池状态改变时系统会触发它
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取电量、电压、温度
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);

            float batteryPct = level * 100 / (float) scale;

            // 转换充电状态文字
            String statusString = "未知";
            if (status == BatteryManager.BATTERY_STATUS_CHARGING) statusString = "正在充电 ⚡";
            if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) statusString = "放电中";
            if (status == BatteryManager.BATTERY_STATUS_FULL) statusString = "已充满";

            String healthStatus = "未知";
            switch (health) {
                case BatteryManager.BATTERY_HEALTH_GOOD: healthStatus = "良好"; break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT: healthStatus = "过热"; break;
                case BatteryManager.BATTERY_HEALTH_DEAD: healthStatus = "损坏/报废"; break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE: healthStatus = "电压过高"; break;
                case BatteryManager.BATTERY_HEALTH_COLD: healthStatus = "温度过低"; break;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("当前电量: ").append(batteryPct).append("%\n");
            sb.append("充电状态: ").append(statusString).append("\n");
            sb.append("电池电压: ").append(voltage).append(" mV\n");
            // 电池温度通常是摄氏度*10
            sb.append("电池温度: ").append(temperature / 10.0).append(" ℃\n");
            sb.append("电源类型: ").append(getPlugType(intent)).append("\n");
            sb.append("健康状况: ").append(healthStatus).append("\n");

            tvBatteryInfo.setText(sb.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_detail);
        tvBatteryInfo = findViewById(R.id.tv_battery_info);
    }

    // 获取充电器类型
    private String getPlugType(Intent intent) {
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (plugged == BatteryManager.BATTERY_PLUGGED_AC) return "交流电 (AC)";
        if (plugged == BatteryManager.BATTERY_PLUGGED_USB) return "USB 接口";
        return "电池供电";
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册接收器：监听电池改变动作
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 必须注销，否则后台会一直运行导致耗电
        unregisterReceiver(batteryReceiver);
    }
}