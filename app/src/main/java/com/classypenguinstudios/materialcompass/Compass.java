package com.classypenguinstudios.materialcompass;

import android.animation.Animator;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sayujyarijal on 15-03-31.
 */
public class Compass {
    private ImageView needleIV;
    private TextView headingTV;
    private DirectionIndicator northDI, eastDI, westDI, southDI;
    private RelativeLayout mainRL;
    private int heading = 0;

    public Compass(ImageView needleIV, TextView headingTV, DirectionIndicator[] directionIndicators, RelativeLayout mainRL) {
        this.needleIV = needleIV;
        this.headingTV = headingTV;
        this.northDI = directionIndicators[0];
        this.eastDI = directionIndicators[1];
        this.southDI = directionIndicators[2];
        this.westDI = directionIndicators[3];
        this.mainRL = mainRL;
    }

    public void updateHeading(float heading) {
        moveNeedle(heading);
        headingTV.setText(String.format("%03d"
                , (int) heading) + "°");
        this.heading = (int) heading;
        elevateViews();
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

                int cx = mainRL.getRight();
                int cy = mainRL.getBottom();

                int finalRadius = (int) (1.45 * Math.max(mainRL.getWidth(), mainRL.getHeight()));

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

    public RelativeLayout getLayout() {
        return this.mainRL;
    }

}