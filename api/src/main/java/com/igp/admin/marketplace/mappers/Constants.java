package com.igp.admin.marketplace.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igp.config.SelectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suditi on 19/1/18.
 */
public class Constants {
    private static final Logger logger   = LoggerFactory.getLogger(Constants.class);
    public static final  String STANDARD = "Standard Delivery";
    public static final  String FIXED    = "Fixed Time Delivery";
    public static final  String MIDNIGHT = "Midnight Delivery";
    public static final  String SAME_DAY = "Fix Date Delivery";

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

    public static  String getDeliveryType(String deliveryTypeIntValue){
        String deliveryType="";
        Map<String,String> deliveryTypeMap=new HashMap<>();
        deliveryTypeMap.put("1","Any time");
        deliveryTypeMap.put("2","Fix Time");
        deliveryTypeMap.put("3","Midnight");
        deliveryTypeMap.put("4","Same Day");

        deliveryType=deliveryTypeMap.get(deliveryTypeIntValue);

        return deliveryType;
    }
    public  Map<String,String> getStoreIdMap()
    {
        Map<String,String> storeIdMap=new HashMap<>();
        String propFileName = "StoreIdMap.json";
        try{
            InputStream inputStream=SelectionCode.class.getClassLoader().getResourceAsStream(propFileName);
            ObjectMapper mapper = new ObjectMapper();
            storeIdMap=mapper.readValue(inputStream,Map.class);
        }catch (Exception exception){
            logger.error("Exception while getting store name from file", exception);
        }
        return storeIdMap;
    }
    public String getActualOrderStatus(String status){ // func used to find out status according to database
        String actualStatus="";

        try{
            Map<String,String> orderStatusMap=new HashMap<>();
            orderStatusMap.put("Processing","Processing");
            orderStatusMap.put("Processed","Processed");
            orderStatusMap.put("Confirmed","Confirmed");
            orderStatusMap.put("OutForDelivery","Shipped");
            orderStatusMap.put("Delivered","Shipped");
            orderStatusMap.put("Rejected","Rejected");
            actualStatus=orderStatusMap.get(status);
        }catch (Exception exception){
            logger.error("Exception while getting store name from file", exception);
        }
        return actualStatus;
    }

}
