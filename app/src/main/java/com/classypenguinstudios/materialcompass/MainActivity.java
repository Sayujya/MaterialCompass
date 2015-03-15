package com.classypenguinstudios.materialcompass;

import android.animation.Animator;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;


public class MainActivity extends Activity {

    private static float heading = 0;
    private static TextView headingTV;
    private boolean northIVelevated = true;
    private boolean eastIVelevated = false;
    private boolean southIVelevated = false;
    private boolean westIVelevated = false;
    private boolean isAnimationComplete = false;
    private Animator animator;

    protected static void updateHeading(float lastValue) {
        MainActivity.heading = lastValue;
        headingTV.setText(String.format("%03d"
                , (int) lastValue) + "°");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView northIV = (ImageView) findViewById(R.id.IVnorth);
        final ImageView eastIV = (ImageView) findViewById(R.id.IVeast);
        final ImageView southIV = (ImageView) findViewById(R.id.IVsouth);
        final ImageView westIV = (ImageView) findViewById(R.id.IVwest);

        final RelativeLayout mainRL = (RelativeLayout) findViewById(R.id.RLmain);

        //LinearLayout buttonsLL = (LinearLayout) findViewById(R.id.LLbuttons);

        final ImageView circleIB = (ImageView) findViewById(R.id.IBcircle);

        // TO DO: IMPLEMENT SIZABLE LINEARLAYOUT

        //RelativeLayout.LayoutParams buttonsLLparams= new RelativeLayout.LayoutParams(buttonsLL.getLayoutParams().width, buttonsLL.getLayoutParams().height*2);
        //buttonsLLparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //buttonsLL.setLayoutParams(buttonsLLparams);

        headingTV = (TextView) findViewById(R.id.TVheading);


        mainRL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainRL.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int cx = mainRL.getRight();
                int cy = mainRL.getBottom();

                int finalRadius = (int) (1.45 * Math.max(mainRL.getWidth(), mainRL.getHeight()));

                animator = ViewAnimationUtils.createCircularReveal(mainRL, cx, cy, 0, finalRadius);
                animator.setDuration(2000);

                animator.addListener(new Animator.AnimatorListener() {


                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimationComplete = true;
                        int[] valueArray = new int[]{23, 68, 113, 158, 203, 248, 293, 338};
                        for (int i = 0; i < 8; i++) {
                            elevateViews(valueArray[i] - 5, northIV, eastIV, southIV, westIV);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

                mainRL.setVisibility(View.VISIBLE);
                northIV.setVisibility(View.VISIBLE);
                eastIV.setVisibility(View.VISIBLE);
                southIV.setVisibility(View.VISIBLE);
                westIV.setVisibility(View.VISIBLE);
                animator.start();
            }
        });


        // sensor parts start here
        SensorManager sensorManager = (SensorManager) mainRL.getContext()
                .getSystemService(SENSOR_SERVICE);

        Sensor accelRawSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorEventListener accelRawListener = new AccelEventListener();
        // registering sensor
        sensorManager.registerListener(accelRawListener, accelRawSensor,
                SensorManager.SENSOR_DELAY_UI);

        SensorEventListener headingListener = new HeadingListener();
        Sensor headingSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(headingListener, headingSensor,
                SensorManager.SENSOR_DELAY_FASTEST);

        headingTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                elevateViews((int) MainActivity.heading, northIV, eastIV, southIV, westIV);
                moveNeedle(circleIB);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void elevateViews(int heading, ImageView northIV,
                              ImageView eastIV,
                              ImageView southIV,
                              ImageView westIV) {
        if (isAnimationComplete) {
            // 23,68,113,158,203,248,293,338
            if (heading > 293 || heading <= 68) {
                // elevate north button
                if (!northIVelevated) {
                    northIVelevated = elevate(northIV);
                }
            } else {
                // delevate north button
                if (northIVelevated) {
                    northIVelevated = !delevate(northIV);
                }
            }

            if (heading > 23 && heading <= 158) {
                // elevate east button
                if (!eastIVelevated) {
                    eastIVelevated = elevate(eastIV);
                }

            } else {
                // delevate east button
                if (eastIVelevated) {
                    eastIVelevated = !delevate(eastIV);
                }
            }

            if (heading > 113 && heading <= 248) {
                // elevate south button
                if (!southIVelevated) {
                    southIVelevated = elevate(southIV);
                }
            } else {
                // delevate south button
                if (southIVelevated) {
                    southIVelevated = !delevate(southIV);
                }
            }

            if (heading > 203 && heading <= 338) {
                // elevate west button
                if (!westIVelevated) {
                    westIVelevated = elevate(westIV);
                }
            } else {
                // delevate west button
                if (westIVelevated) {
                    westIVelevated = !delevate(westIV);
                }
            }
        }
    }

    public void moveNeedle(ImageView needle) {
        needle.setRotation(MainActivity.heading);
    }

    private boolean elevate(ImageView directionIV) {
        directionIV.animate().setDuration(1200).alpha(1.0f);
        return true;
    }

    private boolean delevate(ImageView directionIV) {
        directionIV.animate().setDuration(1200).alpha(0.5f);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class AccelEventListener implements SensorEventListener {
        protected static float[] lastValue;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                lastValue = Arrays.copyOfRange(event.values, 0, 3);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    }

    public static class HeadingListener implements SensorEventListener {

        protected float[] orientation = new float[3];
        protected float lastValue = -1;

        public HeadingListener() {
            super();
        }

        private static float getLowPass(float value, float lastValue) {
            final float smoothingConst = 0.95f;
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
                    && AccelEventListener.lastValue != null) {
                float[] R = new float[9];
                if (SensorManager.getRotationMatrix(R, new float[9],
                        AccelEventListener.lastValue, event.values)) {
                    SensorManager.getOrientation(R, orientation);
                    if (lastValue != -1) {
                        lastValue = getLowPass((float) ((360 + orientation[0] * 360 / (2 * Math.PI))
                                % 360), lastValue) % 360;
                    } else {
                        lastValue = (float) ((360 + orientation[0] * 360 / (2 * Math.PI))
                                % 360);
                    }
                    MainActivity.updateHeading(lastValue);


                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }
    }

}


