package com.example.a432androidtoolbox;

import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class RomDetailActivity extends AppCompatActivity {
    private TextView tvPercent, tvDetails;
    private ProgressBar pbRom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rom_detail);

        tvPercent = findViewById(R.id.tv_rom_percent);
        tvDetails = findViewById(R.id.tv_rom_details);
        pbRom = findViewById(R.id.pb_rom);

        updateRomInfo();
    }

    private void updateRomInfo() {
        // 获取数据目录路径 (/data)
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());

        // 获取块大小及数量（适配新版API使用Long型避免溢出）
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long availableBlocks = stat.getAvailableBlocksLong();

        long totalBytes = totalBlocks * blockSize;
        long availableBytes = availableBlocks * blockSize;
        long usedBytes = totalBytes - availableBytes;

        // 计算占用百分比
        int percent = (int) ((usedBytes / (float) totalBytes) * 100);

        // 使用系统工具类 Formatter 自动将字节转为 GB 或 MB，非常方便
        String totalSize = Formatter.formatFileSize(this, totalBytes);
        String availableSize = Formatter.formatFileSize(this, availableBytes);
        String usedSize = Formatter.formatFileSize(this, usedBytes);

        tvPercent.setText("存储占用: " + percent + "%");
        pbRom.setProgress(percent);

        StringBuilder sb = new StringBuilder();
        sb.append("设备总容量: ").append(totalSize).append("\n");
        sb.append("已使用空间: ").append(usedSize).append("\n");
        sb.append("剩余可用: ").append(availableSize).append("\n\n");
        sb.append("路径: ").append(path.getPath());

        tvDetails.setText(sb.toString());
    }
}