package com.classypenguinstudios.materialcompass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.Arrays;

/**
 * Created by sayujyarijal on 15-03-30.
 */
public class AccelEventListener implements SensorEventListener {

    private float[] lastValue;

    public AccelEventListener() {
        super();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            lastValue = Arrays.copyOfRange(event.values, 0, 3);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public float[] getLastValue() {
        return lastValue;
    }
}
