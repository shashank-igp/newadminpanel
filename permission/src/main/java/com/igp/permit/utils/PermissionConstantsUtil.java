package com.igp.permit.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by suditi on 18/7/18.
 */
public class PermissionConstantsUtil {
    public static final Map<String, Integer> permission = new HashMap<String, Integer>();

    public static Map<String, Integer> getPermission() {
        Map<String,Integer> temp = new HashMap<String, Integer>();
        temp.put("r",1);
        temp.put("w",2);
        temp.put("d",3);
        return temp;
    }
}
