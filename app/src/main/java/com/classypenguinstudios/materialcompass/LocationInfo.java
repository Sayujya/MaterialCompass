package com.classypenguinstudios.materialcompass;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;


public class LocationInfo extends Activity {

    TextView locationInfoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        checkAndSetNightMode();

        // location info
        String info = "Raw Location Info:\n";
        info += "   Latitude: " + DayChecker.getLocation()[0] + "\n";
        info += "   Longitude: " + DayChecker.getLocation()[1] + "\n";
        if (DayChecker.getLocation()[2] != 0) {
            info += "   Altiude: " + DayChecker.getLocation()[2] + "\n";
        }
        info += "\n\n";

        //calculate declination
        info += "Declination Info:\n";
        info += "   Calculated Declination: " + Compass.getDeclination() + " degrees \n";
        info += "\n\n";

        // sunrise sunset info
        info += "Time, Sunrise and Sunset Info:\n";
        Calendar[] suntimes = DayChecker.getSunTimes();
        info += "   Timezone name: " + suntimes[0].getTimeZone().getDisplayName() + "\n";
        info += "   Timezone id: " + suntimes[0].getTimeZone().getID() + "\n";
        info += "   Timezone shift from UTC: " + suntimes[0].getTimeZone().getOffset(System.currentTimeMillis()) / 3600000.0d + "\n";
        info += "   Timezone DST use: " + (suntimes[0].getTimeZone().useDaylightTime() ? " Uses DST" : "Does not use DST") + "\n";
        info += "   DST in effect: " + (suntimes[0].getTimeZone().getDSTSavings() == 0 ? "Not in effect" : "In Effect") + "\n";
        info += "   Today's date (DD/MM/YYYY): " + String.format("%02d", suntimes[0].get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", suntimes[0].get(Calendar.MONTH)) + "/" + suntimes[0].get(Calendar.YEAR) + "\n";
        info += "   Sunrise time (Civil Twilight): " + suntimes[0].get(Calendar.HOUR_OF_DAY) + ":" + suntimes[0].get(Calendar.MINUTE) + "\n";
        info += "   Sunset Time (Civil Twilight): " + suntimes[1].get(Calendar.HOUR_OF_DAY) + ":" + suntimes[1].get(Calendar.MINUTE) + "\n";
        info += "   Declination calculated at time: " + Compass.getTime() / 1000 + " POSIX time" + "\n";

        locationInfoTV.setText(info);
    }

    protected void checkAndSetNightMode() {

        locationInfoTV = (TextView) findViewById(R.id.TVaboutLocation);
        if (DayChecker.getMode().equalsIgnoreCase("Night")) {
            final Window currentWindow = this.getWindow();
            int darkColor = getApplicationContext().getResources().getColor(R.color.primary_night);
            int darKColorAccent = getApplicationContext().getResources().getColor(R.color.primary_dark_night);
            int defaultDark = getApplicationContext().getResources().getColor(R.color.default_dark);
            int defaultWhite = getApplicationContext().getResources().getColor(R.color.default_white);
            RelativeLayout mainRLAboutLocation = (RelativeLayout) findViewById(R.id.RLmainAboutLocation);
            TextView locationInfoMainTV = (TextView) findViewById(R.id.TVmainAboutLocation);
            locationInfoMainTV.setTextColor(defaultWhite);
            locationInfoTV.setTextColor(defaultWhite);
            mainRLAboutLocation.setBackgroundColor(defaultDark);
            currentWindow.setNavigationBarColor(darkColor);
            currentWindow.setStatusBarColor(darkColor);
            getActionBar().setBackgroundDrawable(new ColorDrawable(darKColorAccent));
        }
    }
}
