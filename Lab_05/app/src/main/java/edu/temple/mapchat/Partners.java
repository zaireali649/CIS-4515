package edu.temple.mapchat;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ziggy on 3/19/2018.
 */

public class Partners  {
    private final double lat, lon;
    public List<User> partners;

    Integer[][] list;

    public Partners(List<User> userList, double latitudeLast, double longitudeLast) {
        this.partners = userList;
        this.lat = latitudeLast;
        this.lon = longitudeLast;
        this.list = new Integer[partners.size()][2];
    }


    public void sort() {

        Collections.sort(partners, new Comparator<User>(){

            public int compare(User o1, User o2)
            {
                return o1.compareTo(o2);
            }
        });

        /*Collections.sort(partners, new Comparator<User>(){

            public int compare(User o1, User o2)
            {
                String a = o1.UserName;
                String b = o2.UserName;

                return a.compareTo(b);
            }
        });*/

        //Collections.sort(partners);

        for (int i = 0; i <partners.size(); i++)
        {
            Log.e("SORT: ", partners.get(i).UserName);
        }

    }



}
