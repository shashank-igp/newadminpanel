package com.igp.admin.mappers.marketPlace;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by suditi on 19/1/18.
 */
public class Constants {
    public static final String STANDARD = "Standard Delivery";
    public static final String FIXED = "Fixed Time Delivery";
    public static final String MIDNIGHT = "Midnight Delivery";
    public static final String SAME_DAY = "Fix Date Delivery";

    public static String getSTANDARD() {
        return STANDARD;
    }

    public static String getFIXED() {
        return FIXED;
    }

    public static String getMIDNIGHT() {
        return MIDNIGHT;
    }

    public static String getSameDay() {
        return SAME_DAY;
    }


}
