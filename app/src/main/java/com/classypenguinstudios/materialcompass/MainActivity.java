package com.classypenguinstudios.materialcompass;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    private static float heading = 0;
    private static TextView headingTV;
    private static ImageView circleIV;
    private boolean isAnimationComplete = false;
    private Animator animator;
    private SensorManager mSensorManager;
    private Sensor mAccelRawSensor;
    private AccelEventListener mAccelRawListener;
    private Sensor mHeadingSensor;
    private SensorEventListener mHeadingListener;
    private GeomagneticField field;

    public static void moveNeedle(int heading) {
        circleIV.setRotation(heading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DirectionIndicator northDI = (DirectionIndicator) findViewById(R.id.DInorth);
        final DirectionIndicator eastDI = (DirectionIndicator) findViewById(R.id.DIeast);
        final DirectionIndicator southDI = (DirectionIndicator) findViewById(R.id.DIsouth);
        final DirectionIndicator westDI = (DirectionIndicator) findViewById(R.id.DIwest);

        final RelativeLayout mainRL = (RelativeLayout) findViewById(R.id.RLmain);

        //LinearLayout buttonsLL = (LinearLayout) findViewById(R.id.LLbuttons);

        circleIV = (ImageView) findViewById(R.id.IBcircle);

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
                            elevateViews(valueArray[i] - 5, northDI, eastDI, southDI, westDI);
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
                northDI.setVisibility(View.VISIBLE);
                eastDI.setVisibility(View.VISIBLE);
                southDI.setVisibility(View.VISIBLE);
                westDI.setVisibility(View.VISIBLE);
                animator.start();
            }
        });


        // sensor parts start here
        mSensorManager = (SensorManager) mainRL.getContext()
                .getSystemService(SENSOR_SERVICE);

        mAccelRawSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelRawListener = new AccelEventListener();
        // registering sensor
        mSensorManager.registerListener(mAccelRawListener, mAccelRawSensor,
                SensorManager.SENSOR_DELAY_UI);

        mHeadingListener = new HeadingListener(mAccelRawListener, headingTV);
        mHeadingSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(mHeadingListener, mHeadingSensor,
                SensorManager.SENSOR_DELAY_FASTEST);


    }

    private void elevateViews(int heading, DirectionIndicator northIV,
                              DirectionIndicator eastIV,
                              DirectionIndicator southIV,
                              DirectionIndicator westIV) {
        if (isAnimationComplete) {
            // 23,68,113,158,203,248,293,338
            if (heading > 293 || heading <= 68) {
                // elevate north button
                if (!northIV.isElevated()) {
                    northIV.elevate();
                }
            } else {
                // delevate north button
                if (northIV.isElevated()) {
                    northIV.delevate();
                }
            }

            if (heading > 23 && heading <= 158) {
                // elevate east button
                if (!eastIV.isElevated()) {
                    eastIV.elevate();
                }

            } else {
                // delevate east button
                if (eastIV.isElevated()) {
                    eastIV.delevate();
                }
            }

            if (heading > 113 && heading <= 248) {
                // elevate south button
                if (!southIV.isElevated()) {
                    southIV.elevate();
                }
            } else {
                // delevate south button
                if (southIV.isElevated()) {
                    southIV.delevate();
                }
            }

            if (heading > 203 && heading <= 338) {
                // elevate west button
                if (!westIV.isElevated()) {
                    westIV.elevate();
                }
            } else {
                // delevate west button
                if (westIV.isElevated()) {
                    westIV.delevate();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mAccelRawListener, mAccelRawSensor,
                SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mHeadingListener, mHeadingSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mAccelRawListener);
        mSensorManager.unregisterListener(mHeadingListener);
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
        if (id == R.id.action_about) {
            Intent myIntent = new Intent(MainActivity.this, About.class);
            MainActivity.this.startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


