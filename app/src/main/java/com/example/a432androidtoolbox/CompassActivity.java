package com.example.a432androidtoolbox;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accel, mag;
    private float[] lastAccel = new float[3];
    private float[] lastMag = new float[3];
    private boolean hasAccel = false, hasMag = false;

    private TextView tvDirection;
    // 1. 必须在这里声明变量，否则 findViewById 会报错
    private FrameLayout compassContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        // 2. 初始化控件引用
        tvDirection = findViewById(R.id.tv_direction);
        compassContainer = findViewById(R.id.compass_container);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (mag == null) {
            tvDirection.setText("当前设备缺少磁力计\n无法使用指南针");
        }
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
                float azimuth = (float) Math.toDegrees(orientation[0]);
                azimuth = (azimuth + 360) % 360;

                // 3. 更新 UI：设置文字并旋转圆盘
                tvDirection.setText(String.format("当前方位: %.0f°", azimuth));
                if (compassContainer != null) {
                    // 反向旋转圆盘，使 N 始终指北
                    compassContainer.setRotation(-azimuth);
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}