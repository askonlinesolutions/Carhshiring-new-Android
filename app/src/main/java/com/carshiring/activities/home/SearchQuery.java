package com.carshiring.activities.home;

import java.io.Serializable;

/**
 * Created by Muhib on 8/2/2018.
 * Contact Number : +91 9796173066
 */

public class SearchQuery implements Serializable {
    public String pickupLocation = "" ;
    public boolean isDestAsPickup = true ;
    public boolean isDriverAged = true ;
    public boolean isSearchByMap = false ;
}