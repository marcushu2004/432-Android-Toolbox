package com.example.a432androidtoolbox;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SpiritLevelDetailActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accel, mag;
    private float[] lastAccel = new float[3];
    private float[] lastMag = new float[3];
    private boolean hasAccel = false, hasMag = false;

    private TextView tvDirection;
    private FrameLayout levelContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使用你上传的布局文件
        setContentView(R.layout.activity_spirit_level_detail);

        tvDirection = findViewById(R.id.tv_direction);
        levelContainer = findViewById(R.id.spirit_level_container);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, lastAccel, 0, 3);
            hasAccel = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, lastMag, 0, 3);
            hasMag = true;
        }

        if (hasAccel && hasMag) {
            float[] R = new float[9];
            float[] I = new float[9];
            if (SensorManager.getRotationMatrix(R, I, lastAccel, lastMag)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);

                // orientation[1] 是俯仰角 (Pitch)，绕 X 轴旋转
                // orientation[2] 是翻滚角 (Roll)，绕 Y 轴旋转
                float pitch = (float) Math.toDegrees(orientation[1]);
                float roll = (float) Math.toDegrees(orientation[2]);

                // 更新界面文字
                tvDirection.setText(String.format("Pitch: %.1f°\nRoll: %.1f°", pitch, roll));

                // 简单的视觉反馈：让矩形框根据 Roll 角度旋转
                if (levelContainer != null) {
                    levelContainer.setRotation(-roll);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accel != null) sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        if (mag != null) sensorManager.registerListener(this, mag, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}