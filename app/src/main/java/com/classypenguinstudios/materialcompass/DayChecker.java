package com.classypenguinstudios.materialcompass;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.luckycatlabs.sunrisesunset.SunriseSunsetCalculator;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sayujyarijal on 15-04-12.
 */
public class DayChecker {
    private static String mode = "Day";
    private static Context appContext;
    private static double[] location;

    public static void setAppContext(Context appContext) {
        DayChecker.appContext = appContext;
    }

    public static void updateMode() {
        DayChecker.getGPS();
        Calendar mainCal = Calendar.getInstance();
        final com.luckycatlabs.sunrisesunset.dto.Location stringLocation = new com.luckycatlabs.sunrisesunset.dto.Location(Double.toString(location[0]), Double.toString(location[1]));
        final SunriseSunsetCalculator nightChecker = new SunriseSunsetCalculator(stringLocation, mainCal.getTimeZone());
        Calendar[] sunRiseSetTimes = new Calendar[2];
        sunRiseSetTimes[0] = nightChecker.getCivilSunriseCalendarForDate(mainCal);
        sunRiseSetTimes[1] = nightChecker.getCivilSunsetCalendarForDate(mainCal);
        if (mainCal.compareTo(sunRiseSetTimes[0]) < 0 || mainCal.compareTo(sunRiseSetTimes[1]) >= 0) {
            DayChecker.mode = "Night";
        } else {
            DayChecker.mode = "Day";
        }
    }

    public static String getMode() {
        return mode;
    }

    public static double[] getLocation() {
        return location;
    }

    private static void getGPS() {
        LocationManager lm = (LocationManager) appContext.getSystemService(
                Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[3];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
            gps[2] = l.getAltitude();
        }
        DayChecker.location = gps;
    }
}
