package com.codeali.lab04;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ziggy on 3/19/2018.
 */

public class Partners  {
    private final double lat, lon;
    public List<User> partners;
    public List<User> partnersSort;

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

    }



}
