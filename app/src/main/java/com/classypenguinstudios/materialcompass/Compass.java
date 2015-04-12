package com.classypenguinstudios.materialcompass;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationManager;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sayujyarijal on 15-03-31.
 */
public class Compass {
    private ImageView needleIV, materialIV;
    private TextView headingTV;
    private DirectionIndicator northDI, eastDI, westDI, southDI;
    private RelativeLayout mainRL;
    private Context appContext;
    private int heading = 0;
    private String mode = "Day";
    private double[] location;
    private Calendar[] sunTimes;
    private GeomagneticField field;

    public Compass(ImageView needleIV, TextView headingTV, DirectionIndicator[] directionIndicators, RelativeLayout mainRL, ImageView materialIV, Context appContext) {
        this.needleIV = needleIV;
        this.headingTV = headingTV;
        this.northDI = directionIndicators[0];
        this.eastDI = directionIndicators[1];
        this.southDI = directionIndicators[2];
        this.westDI = directionIndicators[3];
        this.mainRL = mainRL;
        this.materialIV = materialIV;
        this.appContext = appContext;
        sunTimes = getSunriseAndSunset();
        if (mode.equalsIgnoreCase("Night")) {
            setCompassColorDark();
        }

    }

    public void updateHeading(float heading) {
        moveNeedle(heading);
        if (this.heading - (int) heading != 0) {
            this.heading = (int) heading;
            headingTV.setText(String.format("%03d"
                    , (int) heading) + "°");
            elevateViews();
        }
    }

    private void moveNeedle(float heading) {
        needleIV.setRotation(heading);
    }

    private void elevateViews() {
        if (heading > 293 || heading <= 68) {
            // elevate north button
            if (!northDI.isElevated()) {
                northDI.elevate();
            }
        } else {
            // delevate north button
            if (northDI.isElevated()) {
                northDI.delevate();
            }
        }

        if (heading > 23 && heading <= 158) {
            // elevate east button
            if (!eastDI.isElevated()) {
                eastDI.elevate();
            }

        } else {
            // delevate east button
            if (eastDI.isElevated()) {
                eastDI.delevate();
            }
        }

        if (heading > 113 && heading <= 248) {
            // elevate south button
            if (!southDI.isElevated()) {
                southDI.elevate();
            }
        } else {
            // delevate south button
            if (southDI.isElevated()) {
                southDI.delevate();
            }
        }

        if (heading > 203 && heading <= 338) {
            // elevate west button
            if (!westDI.isElevated()) {
                westDI.elevate();
            }
        } else {
            // delevate west button
            if (westDI.isElevated()) {
                westDI.delevate();
            }
        }
    }

    public void startAnimation() {

        mainRL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainRL.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int cx = mainRL.getRight() / 2;
                int cy = mainRL.getBottom() / 6 * 5;

                float finalRadius = (float) (1.45 * Math.max(mainRL.getWidth(), mainRL.getHeight()));

                Animator animator = ViewAnimationUtils.createCircularReveal(mainRL, cx, cy, 0, finalRadius);
                animator.setDuration(2000);

                animator.addListener(new Animator.AnimatorListener() {


                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        int[] valueArray = new int[]{23, 68, 113, 158, 203, 248, 293, 338};
                        for (int i = 0; i < 8; i++) {
                            updateHeading(valueArray[i]);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }
        });
    }

    public void setCompassColorDark() {

        mainRL.setBackgroundColor(appContext.getResources().getColor(R.color.default_dark));
        materialIV.setBackground(appContext.getResources().getDrawable(R.drawable.round_button_night));
        headingTV.setTextColor(appContext.getResources().getColor(R.color.default_white));
        needleIV.setBackground(appContext.getResources().getDrawable(R.drawable.round_button_needle_night));
        Drawable nightAccentColor = appContext.getResources().getDrawable(R.drawable.round_button_accent_night);
        northDI.setBackground(nightAccentColor);
        eastDI.setBackground(nightAccentColor);
        southDI.setBackground(nightAccentColor);
        westDI.setBackground(nightAccentColor);
    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager) appContext.getSystemService(
                Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }

        return gps;
    }

    private Calendar[] getSunriseAndSunset() {
        Calendar mainCal = Calendar.getInstance();
        location = getGPS();
        final com.luckycatlabs.sunrisesunset.dto.Location stringLocation = new com.luckycatlabs.sunrisesunset.dto.Location(Double.toString(location[0]), Double.toString(location[1]));
        final SunriseSunsetCalculator nightChecker = new SunriseSunsetCalculator(stringLocation, mainCal.getTimeZone());
        Calendar[] sunRiseSetTimes = new Calendar[2];
        sunRiseSetTimes[0] = nightChecker.getCivilSunriseCalendarForDate(mainCal);
        sunRiseSetTimes[1] = nightChecker.getCivilSunsetCalendarForDate(mainCal);
        if (mainCal.compareTo(sunRiseSetTimes[0]) < 0 || mainCal.compareTo(sunRiseSetTimes[1]) >= 0) {
            this.mode = "Night";
        } else {
            this.mode = "Day";
        }
        return sunRiseSetTimes;
    }

    public String getMode() {
        return mode;
    }

    public RelativeLayout getLayout() {
        return this.mainRL;
    }

}
