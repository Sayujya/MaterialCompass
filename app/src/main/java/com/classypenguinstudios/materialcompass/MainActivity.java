package com.classypenguinstudios.materialcompass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity {

    AccelEventListener mAccelRawListener;
    HeadingListener mHeadingListener;
    Sensor mAccelRawSensor;
    Sensor mHeadingSensor;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DayChecker.setAppContext(getApplicationContext());
        DayChecker.updateMode();
        checkAndSetNightMode();
        Compass mainCompass = createMainCompass();
        startSensors(mainCompass.getLayout().getContext(), mainCompass);

    }

    protected void checkAndSetNightMode() {

        if (DayChecker.getMode().equalsIgnoreCase("Night")) {
            final Window currentWindow = this.getWindow();
            int darkColor = getApplicationContext().getResources().getColor(R.color.primary_night);
            int darKColorAccent = getApplicationContext().getResources().getColor(R.color.primary_dark_night);
            RelativeLayout revealRL = (RelativeLayout) findViewById(R.id.RLreveal);
            revealRL.setBackgroundColor(darkColor);
            currentWindow.setNavigationBarColor(darkColor);
            currentWindow.setStatusBarColor(darkColor);
            getActionBar().setBackgroundDrawable(new ColorDrawable(darKColorAccent));
        }
    }

    private Compass createMainCompass() {
        DirectionIndicator northDI = (DirectionIndicator) findViewById(R.id.DInorth);
        DirectionIndicator eastDI = (DirectionIndicator) findViewById(R.id.DIeast);
        DirectionIndicator southDI = (DirectionIndicator) findViewById(R.id.DIsouth);
        DirectionIndicator westDI = (DirectionIndicator) findViewById(R.id.DIwest);

        RelativeLayout mainRL = (RelativeLayout) findViewById(R.id.RLmain);

        ImageView circleIV = (ImageView) findViewById(R.id.IBcircle);

        ImageView innerCircleIV = (ImageView) findViewById(R.id.IBinnerCircle);

        TextView headingTV = (TextView) findViewById(R.id.TVheading);

        Compass mainCompass = new Compass(circleIV, headingTV, new DirectionIndicator[]{northDI, eastDI, southDI, westDI}, mainRL, innerCircleIV, getApplicationContext());

        mainCompass.startAnimation();

        return mainCompass;
    }

    private void startSensors(Context layoutContext, Compass mainCompass) {
        mSensorManager = (SensorManager) layoutContext
                .getSystemService(SENSOR_SERVICE);

        mAccelRawSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelRawListener = new AccelEventListener();
        mHeadingListener = new HeadingListener(mAccelRawListener, mainCompass);
        mHeadingSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        registerListeners();
    }

    private void registerListeners() {
        mSensorManager.registerListener(mAccelRawListener, mAccelRawSensor,
                SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mHeadingListener, mHeadingSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterListeners() {
        mSensorManager.unregisterListener(mAccelRawListener);
        mSensorManager.unregisterListener(mHeadingListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
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


