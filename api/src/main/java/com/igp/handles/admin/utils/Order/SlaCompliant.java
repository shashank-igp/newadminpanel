package com.igp.handles.admin.utils.Order;

import com.igp.handles.vendorpanel.models.Vendor.OrderDetailsPerOrderProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shanky on 9/2/18.
 */
public class SlaCompliant {

    private static final Logger logger = LoggerFactory.getLogger(SlaCompliant.class);
    public int generateSlacodeForAll(OrderDetailsPerOrderProduct orderDetailsPerOrderProduct,int flag)
        // flag 0 for vendors and 1 for admin
    {
        String status,assignTime,shippingType,deliveryDate,deliveryTime,purchasedTime;
        int deliveryStatus;
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");

        status=orderDetailsPerOrderProduct.getOrderProductStatus();
        assignTime=orderDetailsPerOrderProduct.getAssignTime();
        shippingType=orderDetailsPerOrderProduct.getShippingType();
        deliveryDate=formatter1.format(orderDetailsPerOrderProduct.getDeliveryDate());
        deliveryTime=orderDetailsPerOrderProduct.getDeliveryTime();
        purchasedTime=orderDetailsPerOrderProduct.getPurchasedTime();
        deliveryStatus=orderDetailsPerOrderProduct.getDeliveryStatus()==true ? 1 : 0;

        logger.debug(" orderDetailsPerOrderProduct with "+orderDetailsPerOrderProduct.toString());


        int slaCode=-2;
        int slaCodeAdmin = -2;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            Date timeAssigned=formatter.parse(assignTime);
            Date currentDateNoTimeStamp=new Date();
            Date currentDateWithTimeStamp=new Date();

            Date assignTimeNoTimeStamp=formatter1.parse(assignTime);
            Date deliveryDateFormat=formatter1.parse(deliveryDate);
            currentDateNoTimeStamp=formatter1.parse(formatter1.format(currentDateNoTimeStamp));
            currentDateWithTimeStamp=formatter.parse(formatter.format(currentDateWithTimeStamp));
            Calendar cal = Calendar.getInstance();
            cal.setTime(timeAssigned);
            int hourDatePurchase = cal.get(Calendar.HOUR_OF_DAY);
            int minDatePurchase=cal.get(Calendar.MINUTE);
            cal.add(Calendar.MINUTE,30);
            Date assign30Min = cal.getTime();
            cal.add(Calendar.MINUTE,15);
            Date assign45Min = cal.getTime();
            Date today=new Date();
            cal.setTime(today);
            int hourToday = cal.get(Calendar.HOUR_OF_DAY);
            int minToday=cal.get(Calendar.MINUTE);
            Date todayPlus=cal.getTime();

            if(status.equalsIgnoreCase("Processing") && flag == 1){
                Date datePurchased = formatter.parse(purchasedTime);
                cal.setTime(datePurchased);
                cal.add(Calendar.MINUTE,15);
                datePurchased=cal.getTime();
                if (currentDateWithTimeStamp.compareTo(datePurchased) <= 0){
                    slaCodeAdmin = 6;
                }
                else {
                    slaCodeAdmin = 601;
                    // the order is not yet marked processed.
                }
            }
            else if(status.equalsIgnoreCase("Processed")){
                Calendar cal3 = Calendar.getInstance();
                cal3.setTime(currentDateNoTimeStamp);
                cal3.add(Calendar.DATE,-1);
                cal3.add(Calendar.HOUR_OF_DAY,20);
                Date yesterdayDateTimeStampwith20Pm=cal3.getTime();
                cal3.setTime(currentDateNoTimeStamp);
                cal3.add(Calendar.HOUR_OF_DAY,8);
                cal3.add(Calendar.MINUTE,15);
                Date todayDate0815Am=cal3.getTime();
                cal3.setTime(currentDateNoTimeStamp);
                cal3.add(Calendar.HOUR_OF_DAY,20);
                Date todayDate08Pm=cal3.getTime();
                cal3.setTime(currentDateWithTimeStamp);
                if (timeAssigned.compareTo(yesterdayDateTimeStampwith20Pm) < 0) {
                    slaCode = 101;
                    slaCodeAdmin = 101;
                } else if (timeAssigned.compareTo(yesterdayDateTimeStampwith20Pm) >= 0 && timeAssigned.compareTo(todayDate08Pm) < 0) {

                    if(flag == 0) {
                        if (cal3.get(Calendar.HOUR_OF_DAY) <= 9) {
                            if (cal3.get(Calendar.HOUR_OF_DAY) == 9 && cal3.get(Calendar.MINUTE) > 0) {
                                slaCode = 102;
                            } else {
                                slaCode = 2;
                            }
                        } else if (cal3.get(Calendar.HOUR_OF_DAY) > 9) {
                            slaCode = 102;
                        }
                    }
                    else {
                        if (cal3.get(Calendar.HOUR_OF_DAY) <= 8 && cal3.get(Calendar.MINUTE) < 45) {
                            slaCodeAdmin = 2;

                        } else if (cal3.get(Calendar.HOUR_OF_DAY) >= 8 && cal3.get(Calendar.MINUTE) >= 45) {
                            slaCodeAdmin = 102;
                        }
                    }
                }
                else if (timeAssigned.compareTo(todayDate0815Am) >= 0 && timeAssigned.compareTo(todayDate08Pm) < 0) {
                    if(flag == 0) {{
                        if (assign45Min.compareTo(cal3.getTime()) <= 0) {
                            slaCode = 101;
                        } else if (assign45Min.compareTo(cal3.getTime()) > 0) {
                            slaCode = 1;
                        }
                    }
                        if (assign45Min.compareTo(cal3.getTime()) <= 0) {
                            slaCode = 101;
                        } else if (assign45Min.compareTo(cal3.getTime()) > 0) {
                            slaCode = 1;
                        }
                    }else {
                        if (assign30Min.compareTo(cal3.getTime()) <= 0) {
                            slaCodeAdmin = 101;
                        } else if (assign30Min.compareTo(cal3.getTime()) > 0) {
                            slaCodeAdmin = 1;
                        }
                    }
                } else if (timeAssigned.compareTo(todayDate08Pm) >= 0) {
                    slaCode = 1;
                    slaCodeAdmin = 1;
                } else {
                    slaCode = 100;
                    slaCodeAdmin = 100;
                }
                if(timeAssigned.compareTo(today)>0){
                    slaCode=1;
                    slaCodeAdmin = 1;
                }
            } else if(status.equalsIgnoreCase("Confirmed")){
                if(shippingType.equalsIgnoreCase("4")){ // Fixed Date
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(currentDateNoTimeStamp);
                    cal2.add(Calendar.DATE,-1);
                    cal2.add(Calendar.HOUR,20);
                    Date yesterdayDateTimeStampwith20Pm=cal2.getTime();
                    cal2.setTime(currentDateWithTimeStamp);
                    if( deliveryDateFormat.compareTo(currentDateNoTimeStamp)==0 && timeAssigned.compareTo(yesterdayDateTimeStampwith20Pm)<=0 ){
                        if(flag == 0){
                            if(cal2.get(Calendar.HOUR_OF_DAY)<=14 ){
                                if(cal2.get(Calendar.HOUR_OF_DAY)==14 && (cal2.get(Calendar.MINUTE)>0) && (cal2.get(Calendar.SECOND)>0)){
                                    slaCode=201;
                                }
                                else {
                                    slaCode=21;
                                }
                            }else {
                                slaCode=201;
                            }
                        }else {
                            if(cal2.get(Calendar.HOUR_OF_DAY)<=13 && cal2.get(Calendar.MINUTE)<=45){
                                slaCodeAdmin = 21;
                            }else {
                                slaCodeAdmin =  201;
                            }
                        }
                    }else if(deliveryDateFormat.compareTo(currentDateNoTimeStamp)==0 && timeAssigned.compareTo(yesterdayDateTimeStampwith20Pm)>0 ) {
                        if(flag == 0){
                            if(cal2.get(Calendar.HOUR)<=19 ){
                                if(cal2.get(Calendar.HOUR_OF_DAY) == 19 && (cal2.get(Calendar.MINUTE)>0) && (cal2.get(Calendar.SECOND)>0)){
                                    slaCode=202;
                                }
                                else{
                                    slaCode=22;
                                }
                            } else {
                                slaCode=202;
                            }
                        }else {
                            if(cal2.get(Calendar.HOUR_OF_DAY)<=18 && cal2.get(Calendar.MINUTE)<=45){
                                slaCodeAdmin = 22;
                            }else {
                                slaCodeAdmin =  202;
                            }
                        }
                    }else if(deliveryDateFormat.compareTo(currentDateNoTimeStamp)>0){
                        slaCode=0;
                        slaCodeAdmin=0;
                    } else {
                        slaCode=100;
                        slaCodeAdmin=100;
                    }
                }else if(shippingType.equalsIgnoreCase("2")){ //Fixed Time
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(currentDateWithTimeStamp);
                    String slotBiginningTime=deliveryTime.split(":")[0];
                    int slotBiginningTimeInt=Integer.parseInt(slotBiginningTime);
                    int currentHour=cal2.get(Calendar.HOUR_OF_DAY);
                    int currentMin=cal2.get(Calendar.MINUTE);
                    int currentSec=cal2.get(Calendar.SECOND);
                    if(deliveryDateFormat.compareTo(currentDateNoTimeStamp) ==0 && slotBiginningTimeInt+1>=currentHour ){
                        if(flag == 0){
                            if(slotBiginningTimeInt+1==currentHour && (currentMin>0) && (currentSec>0) ) {
                                slaCode=203;
                            }
                            else {
                                slaCode=23;
                            }
                        }else {
                            if(slotBiginningTimeInt+1==currentHour) {
                                slaCodeAdmin=203;
                            }
                            else if( slotBiginningTimeInt==currentHour && (currentMin>=45)){
                                slaCodeAdmin=203;
                            }
                            else {
                                slaCodeAdmin=23;
                            }
                        }

                    }else if(deliveryDateFormat.compareTo(currentDateNoTimeStamp) > 0){
                        slaCode=0;
                        slaCodeAdmin=0;
                    }else {
                        slaCode=203;
                        slaCodeAdmin=203;
                    }
                } else if(shippingType.equalsIgnoreCase("3")){ // MID Night
                    Calendar cal2 = Calendar.getInstance();
                    Calendar cal3=Calendar.getInstance();
                    cal2.setTime(currentDateWithTimeStamp);
                    cal3.setTime(currentDateNoTimeStamp);
                    int currentHour=cal2.get(Calendar.HOUR_OF_DAY);
                    int currentMin=cal2.get(Calendar.MINUTE);
                    cal3.add(Calendar.DATE,-1);
                    cal2.setTime(currentDateNoTimeStamp);
                    if(deliveryDateFormat.compareTo(cal2.getTime())>0){
                        slaCode=0;
                        slaCodeAdmin=0;
                    }else if( ( deliveryDateFormat.compareTo(cal3.getTime())==0 && currentHour <=9  ) || ( deliveryDateFormat.compareTo(cal2.getTime())==0  ) ){
                        if(flag == 0) {
                            if (currentHour == 9 && (currentMin > 0) && deliveryDateFormat.compareTo(cal2.getTime()) != 0) {
                                slaCode = 204;
                            } else {
                                slaCode = 24;
                            }
                        }else {
                            if (currentHour < 9 && deliveryDateFormat.compareTo(cal2.getTime()) != 0){
                                slaCodeAdmin = 24;
                            }else if(currentHour == 9 && (currentMin <= 45) && deliveryDateFormat.compareTo(cal2.getTime()) != 0){
                                slaCodeAdmin = 24;
                            }
                            else {
                                slaCodeAdmin = 204;
                            }
                        }

                    } else {
                        slaCode=204;
                        slaCodeAdmin = 204;
                    }

                }
                if(timeAssigned.compareTo(today)>0) {
                    slaCode = 21;
                    slaCodeAdmin = 21;
                }
            } else  if(status.equalsIgnoreCase("Shipped") && deliveryStatus==0){
                if(shippingType.equalsIgnoreCase("4")) {  // Fixed Date
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(currentDateNoTimeStamp);
                    cal2.add(Calendar.DATE,-1);
                    cal2.add(Calendar.HOUR_OF_DAY,20);
                    Date yesterdayDateTimeStampwith20Pm=cal2.getTime();
                    Calendar cal3 = Calendar.getInstance();
                    cal3.setTime(currentDateWithTimeStamp);
                    if( deliveryDateFormat.compareTo(currentDateNoTimeStamp)==0 && timeAssigned.compareTo(yesterdayDateTimeStampwith20Pm)<=0 ){
                        if(flag == 0) {
                            if (cal3.get(Calendar.HOUR_OF_DAY) <= 14 && cal3.get(Calendar.MINUTE) <= 45) {
                                slaCode = 41;
                            } else {
                                slaCode = 401;
                            }
                        }else{
                            if (cal3.get(Calendar.HOUR_OF_DAY) <= 14 && cal3.get(Calendar.MINUTE) <= 30) {
                                slaCodeAdmin = 41;
                            } else {
                                slaCodeAdmin = 401;
                            }
                        }
                    }else if(deliveryDateFormat.compareTo(currentDateNoTimeStamp)==0 && timeAssigned.compareTo(yesterdayDateTimeStampwith20Pm)>0) {
                        if(flag == 0){
                            if(cal3.get(Calendar.HOUR_OF_DAY)<=19 && cal3.get(Calendar.MINUTE)<=45 ) {
                                slaCode=42;
                            } else {
                                slaCode=402;
                            }
                        }else{
                            if (cal3.get(Calendar.HOUR_OF_DAY) <= 19 && cal3.get(Calendar.MINUTE) <= 15) {
                                slaCodeAdmin = 42;
                            } else {
                                slaCodeAdmin = 402;
                            }
                        }
                    }else if(deliveryDateFormat.compareTo(currentDateNoTimeStamp)>0){
                        slaCode=0;
                        slaCodeAdmin=0;
                    }else {
                        slaCode=100;
                        slaCodeAdmin=100;
                    }
                } else if(shippingType.equalsIgnoreCase("2")){  //Fixed Time
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(currentDateWithTimeStamp);
                    String slotBiginningTime=deliveryTime.split(":")[0];
                    int slotBiginningTimeInt=Integer.parseInt(slotBiginningTime);
                    int currentHour=cal2.get(Calendar.HOUR_OF_DAY);
                    int currentMin=cal2.get(Calendar.MINUTE);
                    int currentSec=cal2.get(Calendar.SECOND);
                    if(deliveryDateFormat.compareTo(currentDateNoTimeStamp) == 0 && slotBiginningTimeInt+2>=currentHour ){
                        if (flag == 0) {
                            if (slotBiginningTimeInt + 2 == currentHour && (currentMin > 0) && (currentSec > 0)) {
                                slaCode = 403;
                            } else {
                                slaCode = 43;
                            }
                        }else {
                            if(slotBiginningTimeInt+2==currentHour) {
                                slaCodeAdmin=403;
                            }
                            else if( slotBiginningTimeInt==currentHour && (currentMin>=45)){
                                slaCodeAdmin=403;
                            }
                            else {
                                slaCodeAdmin=43;
                            }
                        }
                    }else if(deliveryDateFormat.compareTo(currentDateNoTimeStamp) > 0){
                        slaCode=0;
                        slaCodeAdmin=0;
                    }else {
                        slaCode=403;
                        slaCodeAdmin=403;
                    }
                } else if(shippingType.equalsIgnoreCase("3")){  // MID Night
                    Calendar cal2 = Calendar.getInstance();
                    Calendar cal3=Calendar.getInstance();
                    cal2.setTime(currentDateWithTimeStamp);
                    cal3.setTime(currentDateNoTimeStamp);
                    int currentHour=cal2.get(Calendar.HOUR_OF_DAY);
                    int currentMin=cal2.get(Calendar.MINUTE);
                    cal3.add(Calendar.DATE,-1);
                    cal2.setTime(currentDateNoTimeStamp);
                    //          deliveryDateFormat=formatter1.parse(deliveryDate);
                    if(deliveryDateFormat.compareTo(cal2.getTime())>0){
                        slaCode=0;
                        slaCodeAdmin=0;
                    }else if(deliveryDateFormat.compareTo(cal3.getTime())==0 && currentHour <=9 || ( deliveryDateFormat.compareTo(cal2.getTime())==0  ) ) {
                        if (flag == 0) {
                            if (currentHour == 9 && (currentMin > 0) && deliveryDateFormat.compareTo(cal2.getTime()) != 0) {
                                slaCode = 404;
                            } else {
                                slaCode = 44;
                            }
                        } else {
                            if (currentHour < 9 && deliveryDateFormat.compareTo(cal2.getTime()) != 0) {
                                slaCodeAdmin = 44;
                            } else if (currentHour == 9 && (currentMin <= 45) && deliveryDateFormat.compareTo(cal2.getTime()) != 0) {
                                slaCodeAdmin = 44;
                            } else {
                                slaCodeAdmin = 404;
                            }
                        }
                    }else {
                        slaCode=404;
                        slaCodeAdmin=404;
                    }
                }
                if(timeAssigned.compareTo(today)>0){
                    slaCode=41;
                    slaCodeAdmin=41;
                }
            }
            if(flag==0) {
                return slaCode;
            }
            else {
                return slaCodeAdmin;
            }
        }catch (Exception exception){

        }
        // just fall back i.e to just remove compile time error so no use
        return slaCode;
    }
}
