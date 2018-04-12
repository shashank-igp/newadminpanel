package com.igp.handles.admin.utils.Order;

import com.igp.handles.vendorpanel.models.Vendor.OrderDetailsPerOrderProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        formatter1.setTimeZone(TimeZone.getTimeZone("IST"));
        status=orderDetailsPerOrderProduct.getOrderProductStatus();
        assignTime=orderDetailsPerOrderProduct.getAssignTime();
        shippingType=orderDetailsPerOrderProduct.getShippingType();
        deliveryDate=formatter1.format(orderDetailsPerOrderProduct.getDeliveryDate());
        deliveryTime=orderDetailsPerOrderProduct.getDeliveryTime();
        purchasedTime=orderDetailsPerOrderProduct.getPurchasedTime();
        deliveryStatus=orderDetailsPerOrderProduct.getDeliveryStatus()==true ? 1 : 0;

        //        logger.debug(" orderDetailsPerOrderProduct with "+orderDetailsPerOrderProduct.toString());


        int slaCode=-2;
        int slaCodeAdmin = -2;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("IST"));
        Date timeAssigned=null;
        Date assignTimeNoTimeStamp=null;
        Date deliveryDateFormat=null;
        Date assign30Min=null;
        Date assign45Min=null;
        Calendar cal = Calendar.getInstance();
        Calendar calForDatePurchased = Calendar.getInstance();
        try{
            if(assignTime!=null){
                timeAssigned=formatter.parse(assignTime);
                assignTimeNoTimeStamp=formatter1.parse(assignTime);
                cal.setTime(timeAssigned);
                cal.add(Calendar.MINUTE,30);
                assign30Min = cal.getTime();
                cal.add(Calendar.MINUTE,15);
                assign45Min = cal.getTime();
            }
            Date currentDateNoTimeStamp=new Date();
            Date currentDateWithTimeStamp=new Date();

            if(deliveryDate!=null){
                deliveryDateFormat=formatter1.parse(deliveryDate);
            }
            currentDateNoTimeStamp=formatter1.parse(formatter1.format(currentDateNoTimeStamp));
            currentDateWithTimeStamp=formatter.parse(formatter.format(currentDateWithTimeStamp));
            Date today=new Date();
            cal.setTime(today);
            Date datePurchased15 = formatter.parse(purchasedTime);
            calForDatePurchased.setTime(datePurchased15);
            calForDatePurchased.add(Calendar.MINUTE,15);
            datePurchased15=calForDatePurchased.getTime();

            if(status.equalsIgnoreCase("Processing") && flag == 1){

                if (currentDateWithTimeStamp.compareTo(datePurchased15) <= 0){
                    slaCodeAdmin = 6;
                }
                else {
                    slaCodeAdmin = 601;
                    // the order is not yet marked processed.
                }
            }
            else if(status.equalsIgnoreCase("Processed")){
                if(orderDetailsPerOrderProduct.getVendorId()==72){
                    if (currentDateWithTimeStamp.compareTo(datePurchased15) <= 0){
                        slaCodeAdmin = 1;
                    }
                    else {
                        slaCodeAdmin = 101;
                        // the order is not yet alloted.
                    }
                }else if (orderDetailsPerOrderProduct.getVendorId()!=72 && assignTime!=null) {
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
                    } else if (timeAssigned.compareTo(yesterdayDateTimeStampwith20Pm) >= 0 && timeAssigned.compareTo(todayDate0815Am) < 0) {

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
                            if (cal3.get(Calendar.HOUR_OF_DAY) <= 8) {
                                if(cal3.get(Calendar.HOUR_OF_DAY) == 8 && cal3.get(Calendar.MINUTE) > 45) {
                                    slaCodeAdmin = 102;
                                }
                                else {
                                    slaCodeAdmin = 2;
                                }
                            } else {
                                slaCodeAdmin = 102;
                            }
                        }
                    }
                    else if (timeAssigned.compareTo(todayDate0815Am) >= 0 && timeAssigned.compareTo(todayDate08Pm) < 0) {
                        if(flag == 0){
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
                            if(cal2.get(Calendar.HOUR_OF_DAY)<=13 ){
                                if(cal2.get(Calendar.HOUR_OF_DAY)==13 && cal2.get(Calendar.MINUTE) > 45){
                                    slaCodeAdmin =  201;
                                }else {
                                    slaCodeAdmin = 21;
                                }
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
                                if(cal2.get(Calendar.HOUR_OF_DAY)==18 && cal2.get(Calendar.MINUTE)>45){
                                    slaCodeAdmin =  202;
                                }else {
                                    slaCodeAdmin = 22;
                                }
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
                            if (currentHour >= 9  && deliveryDateFormat.compareTo(cal2.getTime()) != 0) {
                                //if(currentHour==9 && (currentMin > 0))
                                slaCode = 204;
                            } else {
                                slaCode = 24;
                            }
                        }else {
                            if(currentHour >= 8 && (currentMin >= 45) && deliveryDateFormat.compareTo(cal2.getTime()) != 0){
                                if(currentHour == 8 && (currentMin <= 45)){
                                    slaCodeAdmin = 24;
                                }else {
                                    slaCodeAdmin = 204;
                                }
                            }
                            else {
                                slaCodeAdmin = 24;
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
                            if (cal3.get(Calendar.HOUR_OF_DAY) <= 14) {
                                if(cal3.get(Calendar.HOUR_OF_DAY) == 14 && cal3.get(Calendar.MINUTE) > 45){
                                    slaCode = 401;
                                }else {
                                    slaCode = 41;
                                }
                            } else {
                                slaCode = 401;
                            }
                        }else{
                            if (cal3.get(Calendar.HOUR_OF_DAY) <= 14) {
                                if(cal3.get(Calendar.HOUR_OF_DAY) == 14 && cal3.get(Calendar.MINUTE) > 30){
                                    slaCode = 401;
                                }else {
                                    slaCode = 41;
                                }
                            } else {
                                slaCode = 401;
                            }
                        }
                    }else if(deliveryDateFormat.compareTo(currentDateNoTimeStamp)==0 && timeAssigned.compareTo(yesterdayDateTimeStampwith20Pm)>0) {
                        if(flag == 0){
                            if(cal3.get(Calendar.HOUR_OF_DAY)<=19 ) {
                                if(cal3.get(Calendar.HOUR_OF_DAY)==19 && cal3.get(Calendar.MINUTE) > 45){
                                    slaCode=402;
                                }else {
                                    slaCode=42;
                                }
                            } else {
                                slaCode=402;
                            }
                        }else{
                            if(cal3.get(Calendar.HOUR_OF_DAY)<=19 ) {
                                if(cal3.get(Calendar.HOUR_OF_DAY)==19 && cal3.get(Calendar.MINUTE) > 15){
                                    slaCode=402;
                                }else {
                                    slaCode=42;
                                }
                            } else {
                                slaCode=402;
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
                            if (currentHour >= 9  && deliveryDateFormat.compareTo(cal2.getTime()) != 0) {
                                slaCode = 404;
                            } else {
                                slaCode = 44;
                            }
                        } else {
                            if (currentHour >= 8 && (currentMin <= 45) && deliveryDateFormat.compareTo(cal2.getTime()) != 0) {
                                if(currentHour == 8 && (currentMin <= 45)){
                                    slaCodeAdmin = 44;
                                }else {
                                    slaCodeAdmin = 404;
                                }
                            } else {
                                slaCodeAdmin = 44;
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
            logger.error("Exception happednd while calculating sla code ",exception);
        }
        // just fall back i.e to just remove compile time error so no use
        return slaCode;
    }
/*
   New to Confirmed -
   Orders received before 8PM - 45mins from order time - 1 , 101
   Orders received after 8PM - By 9AM next day -  2 , 102
   Confirmed to Out for Delivery -
   Fixed Date Orders with delivery date as today -  Orders received before 8PM yesterday - 2PM today   - 21  , 201
   Orders received after 8PM yesterday - By 7PM today  - 22,202
   Fixed Time Orders with delivery date as today -By slot beginning time + 1 hour  - 23 , 203
   Mid Night Delivery with delivery date as yesterday -By 9AM today - 24 , 204
   Out for Delivery to Delivered
   Fixed Date Orders with delivery date as today -Orders received before 8PM yesterday - 2:45PM today   - 41 , 401
   Orders received after 8PM yesterday - By 7:45PM today  - 42 , 402
   Fixed Time Orders with delivery date as today -By slot beginning time + 2 hour   - 43 , 403
   Mid Night Delivery with delivery date as yesterday -By 9AM today - 44 , 404
    */

    public String getSlaTime(String assignTime,int slaCode,String actionTime,String purchaseTime,String deliveryDate
                            ,String deliveryTime){

        String result=null;
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("IST"));
        SimpleDateFormat formatterNoTimeStamp=new SimpleDateFormat("yyyy-MM-dd");
        formatterNoTimeStamp.setTimeZone(TimeZone.getTimeZone("IST"));
        Date timeAssigned=null;
        Date assignTimeNoTimeStamp=null;
        Date actionTimeTimeStamp=null;
        Date assign30Min=null;
        Date assign45Min=null;
        Date datePurchased=null,datePurchasedNoTimeStamp=null;
        Date deliveryDateNoTimeStamp=null;
        Calendar cal = Calendar.getInstance();
        try{
            timeAssigned=formatter.parse(assignTime);
            assignTimeNoTimeStamp=formatterNoTimeStamp.parse(assignTime);
            actionTimeTimeStamp=formatter.parse(actionTime);
            cal.setTime(timeAssigned);
            cal.add(Calendar.MINUTE,30);
            assign30Min = cal.getTime();
            cal.add(Calendar.MINUTE,15);
            assign45Min = cal.getTime();
            datePurchased=formatter.parse(purchaseTime);
            datePurchasedNoTimeStamp=formatterNoTimeStamp.parse(purchaseTime);
            deliveryDateNoTimeStamp=formatterNoTimeStamp.parse(deliveryDate);

            if(slaCode==1||slaCode==101){
                result=getDateDifference(actionTimeTimeStamp,assign45Min);
            }else if(slaCode==2||slaCode==102){
                cal.setTime(datePurchasedNoTimeStamp);
                cal.add(Calendar.DATE,1);
                cal.add(Calendar.HOUR_OF_DAY,9);
                Date purchaseTimeToNextDay9Am=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,purchaseTimeToNextDay9Am);
            }else if(slaCode==21||slaCode==201){
                cal.setTime(deliveryDateNoTimeStamp);
                cal.add(Calendar.HOUR_OF_DAY,14);
                Date deliveryDate2Pm=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,deliveryDate2Pm);
            }else if(slaCode==22||slaCode==202){
                cal.setTime(datePurchasedNoTimeStamp);
                Calendar tempCal= Calendar.getInstance();
                tempCal.setTime(datePurchased);
                if(tempCal.get(Calendar.HOUR_OF_DAY)>=8 && tempCal.get(Calendar.HOUR_OF_DAY) <=23 ){
                    cal.add(Calendar.DATE,1);
                }
                cal.add(Calendar.HOUR_OF_DAY,19);
                Date purchaseTimeToNextDay7Pm=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,purchaseTimeToNextDay7Pm);
            }else if(slaCode==23||slaCode==203){
                String slotBiginningTime=deliveryTime.split(":")[0];
                int slotBiginningTimeInt=Integer.parseInt(slotBiginningTime);
                cal.setTime(deliveryDateNoTimeStamp);
                cal.add(Calendar.HOUR_OF_DAY,slotBiginningTimeInt+1);
                Date deliveryDatePlusSlotBigninningPuls1=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,deliveryDatePlusSlotBigninningPuls1);
            }else if(slaCode==24||slaCode==204){
                cal.setTime(deliveryDateNoTimeStamp);
                cal.add(Calendar.DATE,1);
                cal.add(Calendar.HOUR_OF_DAY,9);
                Date deliveryDateToNextDay9Am=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,deliveryDateToNextDay9Am);
            }else if(slaCode==41||slaCode==401){
                cal.setTime(deliveryDateNoTimeStamp);
                cal.add(Calendar.HOUR_OF_DAY,14);
                cal.add(Calendar.MINUTE,45);
                Date deliveryDate245Pm=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,deliveryDate245Pm);
            }else if(slaCode==42|| slaCode==402){
                cal.setTime(datePurchasedNoTimeStamp);
                Calendar tempCal= Calendar.getInstance();
                tempCal.setTime(datePurchased);
                if(tempCal.get(Calendar.HOUR_OF_DAY)>=8 && tempCal.get(Calendar.HOUR_OF_DAY) <=23 ){
                    cal.add(Calendar.DATE,1); // can add one depends upon when the order came i.e yesterday or today
                }
                cal.add(Calendar.HOUR_OF_DAY,19);
                cal.add(Calendar.MINUTE,45);
                Date purchaseTimeToNextDay745Pm=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,purchaseTimeToNextDay745Pm);
            }else if(slaCode==43||slaCode==403){
                String slotBiginningTime=deliveryTime.split(":")[0];
                int slotBiginningTimeInt=Integer.parseInt(slotBiginningTime);
                cal.setTime(deliveryDateNoTimeStamp);
                cal.add(Calendar.HOUR_OF_DAY,slotBiginningTimeInt+2);
                Date deliveryDatePlusSlotBigninningPuls2=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,deliveryDatePlusSlotBigninningPuls2);
            }else if(slaCode==44||slaCode==404){
                cal.setTime(deliveryDateNoTimeStamp);
                cal.add(Calendar.DATE,1);
                cal.add(Calendar.HOUR_OF_DAY,9);
                Date deliveryDateToNextDay9Am=cal.getTime();
                result=getDateDifference(actionTimeTimeStamp,deliveryDateToNextDay9Am);
            }
        }catch (Exception exception) {
            logger.error("Error occured while checking sla time ",exception);
        }
        return result;
    }
    public String getDateDifference(Date date1,Date date2){
        //in milliseconds
        String result="00:00:00";
        try {
            long diff = date1.getTime() - date2.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if(diffDays<0||diffHours<0||diffMinutes<0||diffSeconds<0){
                result="-";
                result+=Math.abs(diffHours)+":"+Math.abs(diffMinutes)+":"+Math.abs(diffSeconds);
            }else {
                result="+";
                result+=Math.abs(diffHours)+":"+Math.abs(diffMinutes)+":"+Math.abs(diffSeconds);
            }
        }catch (Exception exception){
            logger.error("Error occured while calculating difference in dates for sla ",exception);
        }
        return result;
    }
}
