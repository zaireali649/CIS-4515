package com.codeali.lab04;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Ziggy on 3/19/2018.
 */

public class User implements Comparable<User>{

    public String UserName;
    public String Latitude;
    public String Longitude;

    public String ULongitude;
    public String ULatitude;

    public int Distance;

    @Override
    public int compareTo(@NonNull User o) {
        float[] distance = new float[2];

        User u = (User) o;

        Location.distanceBetween( Double.parseDouble(u.ULatitude), Double.parseDouble(u.ULongitude),
                Double.parseDouble(u.Latitude), Double.parseDouble(u.Longitude), distance);

        Log.e("BLAH", String.valueOf(distance[0]));



        return (int) distance[0];
    }
}
