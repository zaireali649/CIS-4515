package edu.temple.mapchat;

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
        float[] distance2 = new float[2];

        User u = (User) o;

        Location.distanceBetween( Double.parseDouble(ULatitude), Double.parseDouble(ULongitude),
                Double.parseDouble(Latitude), Double.parseDouble(Longitude), distance);

        Location.distanceBetween( Double.parseDouble(u.ULatitude), Double.parseDouble(u.ULongitude),
                Double.parseDouble(u.Latitude), Double.parseDouble(u.Longitude), distance2);

        Log.e("DISTANCE", String.valueOf(distance[0]));


        float x = distance[0];
        float y = distance2[0];


        if (x > y )
        {
            return 1;
        }
        else if( x == y )
        {
            return 0;
        }
        else
            return -1;
    }
}
