package com.example.customview.gyro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/***
 * @date 2019-10-24 09:56
 * @author BoXun.Zhao
 * @description
 */
public class GyroManager implements SensorEventListener {
    private GyroImageView gyroImageView;
    private long mLastTimestamp;
    private float mMaxAngle = (float) (Math.PI / 2);

    private SensorManager sensorManager;

    /**
     * 将纳秒转化为秒
     */
    private static final float NS2S = 1f / 1000000000;

    public GyroManager(GyroImageView gyroImageView) {
        this.gyroImageView = gyroImageView;
    }

    public boolean register(Context context) {
        boolean success = false;
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        Sensor sensor;
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            //灵敏度从快到慢 可选择: SENSOR_DELAY_FASTEST; SENSOR_DELAY_GAME; SENSOR_DELAY_NORMAL; SENSOR_DELAY_UI
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
            success = true;
        }
        return success;
    }

    public void unRegister() {
        sensorManager.unregisterListener(this);
        sensorManager = null;
        if (gyroImageView != null) {
            gyroImageView = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (gyroImageView == null) {
            return;
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if (mLastTimestamp != 0) {
                gyroImageView.mAngelX +=
                        event.values[0] * (event.timestamp - mLastTimestamp) * NS2S * 2.0f;
                gyroImageView.mAngelY +=
                        event.values[1] * (event.timestamp - mLastTimestamp) * NS2S * 2.0f;
                if (gyroImageView.mAngelX > mMaxAngle) {
                    gyroImageView.mAngelX = mMaxAngle;
                }
                if (gyroImageView.mAngelX < -mMaxAngle) {
                    gyroImageView.mAngelX = -mMaxAngle;
                }
                if (gyroImageView.mAngelY > mMaxAngle) {
                    gyroImageView.mAngelY = mMaxAngle;
                }
                if (gyroImageView.mAngelY < -mMaxAngle) {
                    gyroImageView.mAngelY = -mMaxAngle;
                }
                //这里注意下 顺序
                gyroImageView
                        .update(gyroImageView.mAngelY / mMaxAngle, gyroImageView.mAngelX / mMaxAngle);
            }
            mLastTimestamp = event.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
