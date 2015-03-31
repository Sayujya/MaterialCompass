package com.classypenguinstudios.materialcompass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by sayujyarijal on 15-03-30.
 */
public class HeadingListener implements SensorEventListener {

    protected final static float smoothingConst = 0.95f;
    private AccelEventListener accelListener;
    private float[] orientation = new float[3];
    private float lastValue = -1;
    private TextView headingTV;

    public HeadingListener(AccelEventListener accelListener, TextView headingTV) {
        super();
        this.accelListener = accelListener;
        this.headingTV = headingTV;
    }

    private static float getLowPass(float value, float lastValue) {
        if ((lastValue - value) > 180) {
            value += 360;
        }
        if ((value - lastValue) > 180) {
            lastValue += 360;
        }
        lastValue = smoothingConst * lastValue
                + (1.0f - smoothingConst) * value;

        return lastValue;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
                && accelListener.getLastValue() != null) {
            float[] R = new float[9];
            if (SensorManager.getRotationMatrix(R, new float[9],
                    accelListener.getLastValue(), event.values)) {
                SensorManager.getOrientation(R, orientation);
                if (lastValue != -1) {
                    lastValue = getLowPass((float) ((360 + orientation[0] * 180 / (Math.PI))
                            % 360), lastValue) % 360;
                } else {
                    lastValue = (float) ((360 + orientation[0] * 180 / (Math.PI))
                            % 360);
                }
                headingTV.setText(String.format("%03d"
                        , (int) lastValue) + "°");
                MainActivity.moveNeedle((int) lastValue);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }
}
