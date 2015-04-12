package com.classypenguinstudios.materialcompass;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;


public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        checkAndSetNightMode();
    }

    protected void checkAndSetNightMode() {

        if (DayChecker.getMode().equalsIgnoreCase("Night")) {
            final Window currentWindow = this.getWindow();
            int darkColor = getApplicationContext().getResources().getColor(R.color.primary_night);
            int darKColorAccent = getApplicationContext().getResources().getColor(R.color.primary_dark_night);
            int defaultDark = getApplicationContext().getResources().getColor(R.color.default_dark);
            int defaultWhite = getApplicationContext().getResources().getColor(R.color.default_white);
            LinearLayout mainLLAbout = (LinearLayout) findViewById(R.id.LLmainAbout);
            TextView madebyTV = (TextView) findViewById(R.id.TVmadeby);
            TextView contributionsTV = (TextView) findViewById(R.id.TVcontributions);
            madebyTV.setTextColor(defaultWhite);
            contributionsTV.setTextColor(defaultWhite);
            mainLLAbout.setBackgroundColor(defaultDark);
            currentWindow.setNavigationBarColor(darkColor);
            currentWindow.setStatusBarColor(darkColor);
            getActionBar().setBackgroundDrawable(new ColorDrawable(darKColorAccent));
        }
    }
}
