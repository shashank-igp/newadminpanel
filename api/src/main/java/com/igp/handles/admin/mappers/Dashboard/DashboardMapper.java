package com.igp.handles.admin.mappers.Dashboard;

import com.igp.admin.marketplace.mappers.Constants;
import com.igp.handles.admin.models.Dashboard.DashboardDetail;
import com.igp.handles.admin.utils.Dashboard.DashboardUtil;
import com.igp.handles.admin.utils.Order.SlaCompliant;
import com.igp.handles.vendorpanel.mappers.Vendor.HandlesVendorMapper;
import com.igp.handles.vendorpanel.models.Vendor.OrderDetailsPerOrderProduct;
import com.igp.handles.vendorpanel.utils.Order.OrderUtil;
import com.igp.handles.vendorpanel.utils.Vendor.VendorUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shanky on 16/1/18.
 */
public class DashboardMapper {
    private static final Logger logger = LoggerFactory.getLogger(DashboardUtil.class);

    public DashboardDetail getDashboardOrderCountDetail(Date specificDate)
    {
        DashboardDetail dashboardDetail = new DashboardDetail();
        HandlesVendorMapper hp=new HandlesVendorMapper();
        VendorUtil handelVendorUtil=new VendorUtil();
        DashboardUtil dashboardUtil=new DashboardUtil();
        SlaCompliant slaCompliant=new SlaCompliant();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date deliveryDate = null;

        Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdAllMap      = dashboardDetail.getDateStatusOrderIdAllMap();
        Date todayDate = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date tomorrowDate = DateUtils.addDays(todayDate, 1);
        Date futureDate = DateUtils.addDays(todayDate, 2);
        Date pastDate = DateUtils.addDays(todayDate, -7);

        Map<String,Set<String>> uniqueUnitsMap=new HashMap<>();


        // For mobile view we need hashSet of orderIds and orderProductIds ....
        Map<String,Map<String,  Set<Map<String, String>>>> statusCountOrderIdMap00 = new HashMap<>();
        Map<String,Set<Map<String,String>>>  statusCountOrderIdMap0=new HashMap<>();
        statusCountOrderIdMap0.put("notAlloted",new HashSet<>());
        statusCountOrderIdMap0.put("processing",new HashSet<>());
        statusCountOrderIdMap00.put("unAssigned",statusCountOrderIdMap0);
        Map<String,Set<Map<String,String>>>  statusCountOrderIdMap1=new HashMap<>();
        statusCountOrderIdMap1.put("pending",new HashSet<>());
        statusCountOrderIdMap1.put("total",new HashSet<>());
        statusCountOrderIdMap00.put("notConfirmed",statusCountOrderIdMap1);
        dateStatusOrderIdAllMap.put("past",statusCountOrderIdMap00);



        Map<String,Map<String,  Set<Map<String, String>>>> statusCountOrderIdMap01 = new HashMap<>();
        Map<String,Set<Map<String,String>>>  statusCountOrderIdMap2=new HashMap<>();
        statusCountOrderIdMap2.put("notAlloted",new HashSet<>());
        statusCountOrderIdMap2.put("processing",new HashSet<>());
        statusCountOrderIdMap01.put("unAssigned",statusCountOrderIdMap2);
        Map<String,Set<Map<String,String>>>  statusCountOrderIdMap3=new HashMap<>();
        statusCountOrderIdMap3.put("pending",new HashSet<>());
        statusCountOrderIdMap3.put("total",new HashSet<>());
        statusCountOrderIdMap01.put("notConfirmed",statusCountOrderIdMap3);
        dateStatusOrderIdAllMap.put("today",statusCountOrderIdMap01);


        Map<String,Map<String,  Set<Map<String, String>>>> statusCountOrderIdMap02 = new HashMap<>();
        Map<String,Set<Map<String,String>>>  statusCountOrderIdMap4=new HashMap<>();
        statusCountOrderIdMap4.put("notAlloted",new HashSet<>());
        statusCountOrderIdMap4.put("processing",new HashSet<>());
        statusCountOrderIdMap02.put("unAssigned",statusCountOrderIdMap4);
        Map<String,Set<Map<String,String>>>  statusCountOrderIdMap5=new HashMap<>();
        statusCountOrderIdMap5.put("pending",new HashSet<>());
        statusCountOrderIdMap5.put("total",new HashSet<>());
        statusCountOrderIdMap02.put("notConfirmed",statusCountOrderIdMap5);
        dateStatusOrderIdAllMap.put("tomorrow",statusCountOrderIdMap02);

        Map<String,Map<String,  Set<Map<String, String>>>> statusCountOrderIdMap03 = new HashMap<>();
        Map<String,Set<Map<String,String>>>  statusCountOrderIdMap6=new HashMap<>();
        statusCountOrderIdMap6.put("notAlloted",new HashSet<>());
        statusCountOrderIdMap6.put("processing",new HashSet<>());
        statusCountOrderIdMap03.put("unAssigned",statusCountOrderIdMap6);
        Map<String,Set<Map<String,String>>>  statusCountOrderIdMap7=new HashMap<>();
        statusCountOrderIdMap7.put("pending",new HashSet<>());
        statusCountOrderIdMap7.put("total",new HashSet<>());
        statusCountOrderIdMap03.put("notConfirmed",statusCountOrderIdMap7);
        dateStatusOrderIdAllMap.put("future",statusCountOrderIdMap03);


        // some more things to do for mobile view ..>>>> will be done later


        //actual countMaps stated here

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("count", "" + 0);
        dataMap.put("sla", false + "");
        dataMap.put("alert", false + "");

        Map<String,Map<String, Map<String, Map<String, String>>>> dateStatusCountAllMap = dashboardDetail
            .getDateStatusCountAllMap();


        Map<String, Map<String,Map<String, String>>> statusCountMap0 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap00 = new HashMap<>();
        statusCountMap00.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap00.put("processing",hp.copyMap(dataMap));
        statusCountMap0.put("unAssigned",statusCountMap00);
        Map<String,Map<String, String>> statusCountMap01 = new HashMap<>();
        statusCountMap01.put("pending",hp.copyMap(dataMap));
        statusCountMap01.put("total",hp.copyMap(dataMap));
        statusCountMap0.put("notConfirmed",statusCountMap01);
        dateStatusCountAllMap.put("past",statusCountMap0);

        Map<String, Map<String,Map<String, String>>> statusCountMap1 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap02 = new HashMap<>();
        statusCountMap02.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap02.put("processing",hp.copyMap(dataMap));
        statusCountMap1.put("unAssigned",statusCountMap02);
        Map<String,Map<String, String>> statusCountMap03 = new HashMap<>();
        statusCountMap03.put("pending",hp.copyMap(dataMap));
        statusCountMap03.put("total",hp.copyMap(dataMap));
        statusCountMap1.put("notConfirmed",statusCountMap03);
        dateStatusCountAllMap.put("today",statusCountMap1);

        Map<String, Map<String,Map<String, String>>> statusCountMap2 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap04 = new HashMap<>();
        statusCountMap04.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap04.put("processing",hp.copyMap(dataMap));
        statusCountMap2.put("unAssigned",statusCountMap04);
        Map<String,Map<String, String>> statusCountMap05 = new HashMap<>();
        statusCountMap05.put("pending",hp.copyMap(dataMap));
        statusCountMap05.put("total",hp.copyMap(dataMap));
        statusCountMap2.put("notConfirmed",statusCountMap05);
        dateStatusCountAllMap.put("tomorrow",statusCountMap2);

        Map<String, Map<String,Map<String, String>>> statusCountMap3 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap06 = new HashMap<>();
        statusCountMap06.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap06.put("processing",hp.copyMap(dataMap));
        statusCountMap3.put("unAssigned",statusCountMap06);
        Map<String,Map<String, String>> statusCountMap07 = new HashMap<>();
        statusCountMap07.put("pending",hp.copyMap(dataMap));
        statusCountMap07.put("total",hp.copyMap(dataMap));
        statusCountMap3.put("notConfirmed",statusCountMap07);
        dateStatusCountAllMap.put("future",statusCountMap3);

        Map<String,String> notShippedTotalOrderCount = hp.copyMap(dataMap);
        Map<String,String> notShippedPendingOrderCount = hp.copyMap(dataMap);
        Map<String,String> notDeliveredTotalOrderCount = hp.copyMap(dataMap);
        Map<String,String> notDeliveredPendingOrderCount = hp.copyMap(dataMap);
        Map<String,String> attemptedDeliveryOrders=hp.copyMap(dataMap);

        int orderTotalWhole=0;
        int notAssignedOrdersTotalWhole=0;
        int notConfirmedOrdersTotalWhole=0;

        try{
            List<OrderDetailsPerOrderProduct> listOfHandelsOrderId=dashboardUtil.getHandelsOrderList(pastDate,0);
            if (specificDate == null)
            {
                specificDate = handelVendorUtil.getFestivalDate(todayDate);
            }
            dashboardDetail.setFestivalDate(dateFormat.format(specificDate));

            getHandelOrderCountDetailForDate(dashboardDetail);

            for (OrderDetailsPerOrderProduct orderDetailsPerOrderProduct : listOfHandelsOrderId){
                try {
                    Long orderId = orderDetailsPerOrderProduct.getOrdersId();
                    Long orderProductId= orderDetailsPerOrderProduct.getOrdersProductsId();
                    // unique order Ids

                    if (orderDetailsPerOrderProduct.getDeliveryDate() != null)
                        deliveryDate = orderDetailsPerOrderProduct.getDeliveryDate();

                    String status = orderDetailsPerOrderProduct.getOrderProductStatus();
                    String deliveryTime = orderDetailsPerOrderProduct.getDeliveryTime();
                    String shippingType = Constants.getDeliveryType(orderDetailsPerOrderProduct.getShippingType());
                    int vendorId=orderDetailsPerOrderProduct.getVendorId();
                    boolean deliverystatus = (boolean) orderDetailsPerOrderProduct.getDeliveryStatus();
                    boolean flagForUniqueness=false;
                    int slaCode= slaCompliant.generateSlacodeForAll(orderDetailsPerOrderProduct,1);
                    String key=orderId + "," + deliveryDate + "," + shippingType;
                    int deliveryAttemptFlag=orderDetailsPerOrderProduct.getDeliveryAttemptFlag();
                    flagForUniqueness=hp.checkUniqueUnit(orderId,deliveryDate,shippingType,deliveryTime,uniqueUnitsMap,vendorId,status);

                    //    logger.debug("slacode  in handels panel Dashboard  "+slaCode+" with orderId "+orderDetailsPerOrderProduct.getOrdersId()+" and orderProductId "+orderDetailsPerOrderProduct.getOrdersProductsId());


                    if (flagForUniqueness){

                        if(status.equals("Processed") && vendorId==72){ // unassigned -> not alloted
                            //past
                            orderTotalWhole++;
                            notAssignedOrdersTotalWhole++;
                            if (deliveryDate.getTime() < todayDate.getTime()){
                                int count=Integer.parseInt(statusCountMap0.get("unAssigned").get("notAlloted").get("count"));
                                count++;
                                statusCountMap0.get("unAssigned").get("notAlloted").put("count", String.valueOf(count));
                                if (OrderUtil.isSLASatisfied(slaCode)){
                                    if(!statusCountMap0.get("unAssigned").get("notAlloted").get("alert").equals("true")){
                                        statusCountMap0.get("unAssigned").get("notAlloted").put("sla",String.valueOf(true));
                                    }
                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    statusCountMap0.get("unAssigned").get("notAlloted").put("alert",String.valueOf(true));
                                    statusCountMap0.get("unAssigned").get("notAlloted").put("sla",String.valueOf(false));
                                }
                            }
                            else if (deliveryDate.getTime() == todayDate.getTime()){ // today
                                int count=Integer.parseInt(statusCountMap1.get("unAssigned").get("notAlloted").get("count"));
                                count++;
                                statusCountMap1.get("unAssigned").get("notAlloted").put("count", String.valueOf(count));
                                if (OrderUtil.isSLASatisfied(slaCode)){
                                    if(!statusCountMap1.get("unAssigned").get("notAlloted").get("alert").equals("true")){
                                        statusCountMap1.get("unAssigned").get("notAlloted").put("sla",String.valueOf(true));
                                    }
                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    statusCountMap1.get("unAssigned").get("notAlloted").put("alert",String.valueOf(true));
                                    statusCountMap1.get("unAssigned").get("notAlloted").put("sla",String.valueOf(false));
                                }
                            }
                            else if(deliveryDate.getTime() == tomorrowDate.getTime()){ // tommorow
                                int count=Integer.parseInt(statusCountMap2.get("unAssigned").get("notAlloted").get("count"));
                                count++;
                                statusCountMap2.get("unAssigned").get("notAlloted").put("count", String.valueOf(count));
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    if(!statusCountMap2.get("unAssigned").get("notAlloted").get("alert").equals("true")){
                                        statusCountMap2.get("unAssigned").get("notAlloted").put("sla",String.valueOf(true));
                                    }

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    statusCountMap2.get("unAssigned").get("notAlloted").put("alert",String.valueOf(true));
                                    statusCountMap2.get("unAssigned").get("notAlloted").put("sla",String.valueOf(false));
                                }
                            }
                            else if (deliveryDate.getTime() >= futureDate.getTime()){ // future
                                int count=Integer.parseInt(statusCountMap3.get("unAssigned").get("notAlloted").get("count"));
                                count++;
                                statusCountMap3.get("unAssigned").get("notAlloted").put("count", String.valueOf(count));
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    if(!statusCountMap3.get("unAssigned").get("notAlloted").get("alert").equals("true")){
                                        statusCountMap3.get("unAssigned").get("notAlloted").put("sla",String.valueOf(true));
                                    }

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {
                                    statusCountMap3.get("unAssigned").get("notAlloted").put("alert",String.valueOf(true));
                                    statusCountMap3.get("unAssigned").get("notAlloted").put("sla",String.valueOf(false));

                                }
                            }
                        }
                        else if(status.equals("Processing") && vendorId==72){ // unassigned -> Processing
                            //past
                            orderTotalWhole++;
                            notAssignedOrdersTotalWhole++;
                            if (deliveryDate.getTime() < todayDate.getTime()){
                                int count=Integer.parseInt(statusCountMap0.get("unAssigned").get("processing").get("count"));
                                count++;
                                statusCountMap0.get("unAssigned").get("processing").put("count", String.valueOf(count));

                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    if(!statusCountMap0.get("unAssigned").get("processing").get("alert").equals("true")){
                                        statusCountMap0.get("unAssigned").get("processing").put("sla",String.valueOf(true));
                                    }
                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    statusCountMap0.get("unAssigned").get("processing").put("alert",String.valueOf(true));
                                    statusCountMap0.get("unAssigned").get("processing").put("sla",String.valueOf(false));
                                }
                            }
                            else if (deliveryDate.getTime() == todayDate.getTime()){ // today
                                int count=Integer.parseInt(statusCountMap1.get("unAssigned").get("processing").get("count"));
                                count++;
                                statusCountMap1.get("unAssigned").get("processing").put("count", String.valueOf(count));
                                if (OrderUtil.isSLASatisfied(slaCode)){
                                    if(!statusCountMap1.get("unAssigned").get("processing").get("alert").equals("true")){
                                        statusCountMap1.get("unAssigned").get("processing").put("sla",String.valueOf(true));
                                    }

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    statusCountMap1.get("unAssigned").get("processing").put("alert",String.valueOf(true));
                                    statusCountMap1.get("unAssigned").get("processing").put("sla",String.valueOf(false));

                                }
                            }
                            else if(deliveryDate.getTime() == tomorrowDate.getTime()){ // tommorow
                                int count=Integer.parseInt(statusCountMap2.get("unAssigned").get("processing").get("count"));
                                count++;
                                statusCountMap2.get("unAssigned").get("processing").put("count", String.valueOf(count));

                                if (OrderUtil.isSLASatisfied(slaCode)){
                                    if(!statusCountMap2.get("unAssigned").get("processing").get("alert").equals("true")){
                                        statusCountMap2.get("unAssigned").get("processing").put("sla",String.valueOf(true));
                                    }

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {
                                    statusCountMap2.get("unAssigned").get("processing").put("alert",String.valueOf(true));
                                    statusCountMap2.get("unAssigned").get("processing").put("sla",String.valueOf(false));

                                }
                            }
                            else if (deliveryDate.getTime() >= futureDate.getTime()){ // future
                                int count=Integer.parseInt(statusCountMap3.get("unAssigned").get("processing").get("count"));
                                count++;
                                statusCountMap3.get("unAssigned").get("processing").put("count", String.valueOf(count));

                                if (OrderUtil.isSLASatisfied(slaCode)){
                                    if(!statusCountMap3.get("unAssigned").get("processing").get("alert").equals("true")){
                                        statusCountMap3.get("unAssigned").get("processing").put("sla",String.valueOf(true));
                                    }

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {
                                    statusCountMap3.get("unAssigned").get("processing").put("alert",String.valueOf(true));
                                    statusCountMap3.get("unAssigned").get("processing").put("sla",String.valueOf(false));

                                }
                            }
                        }else if (status.equals("Processed") && vendorId != 72){
                            // (-ve sla of Processed to confirm) not Confirmed Yet -> Pending
                            // all orders which are in Processed state of that vendor ,  not Confirmed Yet -> Total

                            //past
                            orderTotalWhole++;
                            notConfirmedOrdersTotalWhole++;
                            if (deliveryDate.getTime() < todayDate.getTime()){
                                int count=Integer.parseInt(statusCountMap0.get("notConfirmed").get("total").get("count"));
                                count++;
                                statusCountMap0.get("notConfirmed").get("total").put("count", String.valueOf(count));

                                if (OrderUtil.isSLASatisfied(slaCode)){
                                    if(statusCountMap0.get("notConfirmed").get("pending").get("alert").equals("true")){
                                        statusCountMap0.get("notConfirmed").get("total").put("alert",String.valueOf(true));
                                    }else {
                                        statusCountMap0.get("notConfirmed").get("total").put("sla",String.valueOf(true));
                                    }
                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap0.get("notConfirmed").get("pending").get("count"));
                                    alertCount++;
                                    statusCountMap0.get("notConfirmed").get("pending").put("count", String.valueOf(alertCount));
                                    statusCountMap0.get("notConfirmed").get("pending").put("alert",String.valueOf(true));

                                }
                            }
                            else if (deliveryDate.getTime() == todayDate.getTime()){ // today
                                int count=Integer.parseInt(statusCountMap1.get("notConfirmed").get("total").get("count"));
                                count++;
                                statusCountMap1.get("notConfirmed").get("total").put("count", String.valueOf(count));
                                if (OrderUtil.isSLASatisfied(slaCode)){
                                    if(statusCountMap1.get("notConfirmed").get("pending").get("alert").equals("true")){
                                        statusCountMap1.get("notConfirmed").get("total").put("alert",String.valueOf(true));
                                    }else {
                                        statusCountMap1.get("notConfirmed").get("total").put("sla",String.valueOf(true));
                                    }
                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap1.get("notConfirmed").get("pending").get("count"));
                                    alertCount++;
                                    statusCountMap1.get("notConfirmed").get("pending").put("count", String.valueOf(alertCount));
                                    statusCountMap1.get("notConfirmed").get("pending").put("alert",String.valueOf(true));

                                }
                            }
                            else if(deliveryDate.getTime() == tomorrowDate.getTime()){ // tommorow
                                int count=Integer.parseInt(statusCountMap2.get("notConfirmed").get("total").get("count"));
                                count++;
                                statusCountMap2.get("notConfirmed").get("total").put("count", String.valueOf(count));
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    if(statusCountMap2.get("notConfirmed").get("pending").get("alert").equals("true")){
                                        statusCountMap2.get("notConfirmed").get("total").put("alert",String.valueOf(true));
                                    }else {
                                        statusCountMap2.get("notConfirmed").get("total").put("sla",String.valueOf(true));
                                    }

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap2.get("notConfirmed").get("pending").get("count"));
                                    alertCount++;
                                    statusCountMap2.get("notConfirmed").get("pending").put("count", String.valueOf(alertCount));
                                    statusCountMap2.get("notConfirmed").get("pending").put("alert",String.valueOf(true));

                                }
                            }
                            else if (deliveryDate.getTime() >= futureDate.getTime()){ // future
                                int count=Integer.parseInt(statusCountMap3.get("notConfirmed").get("total").get("count"));
                                count++;
                                statusCountMap3.get("notConfirmed").get("total").put("count", String.valueOf(count));
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    if(statusCountMap3.get("notConfirmed").get("pending").get("alert").equals("true")){
                                        statusCountMap3.get("notConfirmed").get("total").put("alert",String.valueOf(true));
                                    }else {
                                        statusCountMap3.get("notConfirmed").get("total").put("sla",String.valueOf(true));
                                    }

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap3.get("notConfirmed").get("pending").get("count"));
                                    alertCount++;
                                    statusCountMap3.get("notConfirmed").get("pending").put("count", String.valueOf(alertCount));
                                    statusCountMap3.get("notConfirmed").get("pending").put("alert",String.valueOf(true));

                                }
                            }


                        }else if(status.equals("Confirmed") && vendorId != 72){
                            // (-ve sla of Confirmed to OutOfDelivery i.e *Shipped* ) not Shipped Yet -> Pending
                            // all orders which are in Confirmed state of that vendor ,  not Shipped Yet -> Total


                            if (deliveryDate.getTime() <= todayDate.getTime()){
                                orderTotalWhole++;
                                int count=Integer.parseInt(notShippedTotalOrderCount.get("count"));
                                count++;
                                notShippedTotalOrderCount.put("count",String.valueOf(count));
                                notShippedTotalOrderCount.put("sla",String.valueOf(true));
                                if(OrderUtil.isHighAlertActionRequired(slaCode)){
                                    orderTotalWhole++;
                                    int alertCount=Integer.parseInt(notShippedPendingOrderCount.get("count"));
                                    alertCount++;
                                    notShippedPendingOrderCount.put("count",String.valueOf(alertCount));
                                    notShippedPendingOrderCount.put("alert", String.valueOf(true));
                                }
                            }

                        }else if(status.equals("Shipped") && vendorId != 72 && deliverystatus == false){
                            // (-ve sla of OutOfDelivery to Delivered  i.e * [ Shipped & deliveryStatus=0 ] to
                            //  [Shipped & deliveryStatus= 1 ]* ) not Delivered Yet -> Pending
                            // all orders which are in Shipped & deliveryStatus=0 state of that vendor ,  not Delivered Yet -> Total

                            if (deliveryDate.getTime() <= todayDate.getTime()){
                                orderTotalWhole++;
                                int count=Integer.parseInt(notDeliveredTotalOrderCount.get("count"));
                                count++;
                                notDeliveredTotalOrderCount.put("count",String.valueOf(count));
                                notDeliveredTotalOrderCount.put("sla",String.valueOf(true));
                                if(OrderUtil.isHighAlertActionRequired(slaCode)){
                                    orderTotalWhole++;
                                    int alertCount=Integer.parseInt(notDeliveredPendingOrderCount.get("count"));
                                    alertCount++;
                                    notDeliveredPendingOrderCount.put("count",String.valueOf(alertCount));
                                    notDeliveredPendingOrderCount.put("alert", String.valueOf(true));
                                }
                            }
                        }
                        else if(status.equals("Shipped") && deliverystatus == true && deliveryAttemptFlag == 1){
                            //attempted delivery but somehow could'nt
                            //deliveryAttemptFlag=0 no hassle , deliveryAttemptFlag=1 raised ,
                            // deliveryAttemptFlag = 2 approve from handels team to deliver again ,
                            // deliveryAttemptFlag = 3 finally re-delivery
                            int count=Integer.parseInt(attemptedDeliveryOrders.get("count"));
                            count++;
                            attemptedDeliveryOrders.put("count",String.valueOf(count));

                        }
                    }
                    dashboardDetail.setNotShippedTotalOrderCount(notShippedTotalOrderCount);
                    dashboardDetail.setNotShippedPendingOrderCount(notShippedPendingOrderCount);
                    dashboardDetail.setNotDeliveredTotalOrderCount(notDeliveredTotalOrderCount);
                    dashboardDetail.setNotDeliveredPendingOrderCount(notDeliveredPendingOrderCount);
                    dashboardDetail.setOrderTotalWhole(orderTotalWhole);
                    dashboardDetail.setNotAssignedOrdersTotalWhole(notAssignedOrdersTotalWhole);
                    dashboardDetail.setNotConfirmedOrdersTotalWhole(notConfirmedOrdersTotalWhole);
                    dashboardDetail.setAttemptedDeliveryOrders(attemptedDeliveryOrders);
                }catch (Exception exception){
                    logger.error("Exception occured ",exception);
                }
            }

        }catch (Exception exception){
            logger.error("Exception while preparing orders for dashboard vendor panel");
        }

        return dashboardDetail;
    }
    public void getHandelOrderCountDetailForDate(DashboardDetail dashboardDetail){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("count", "" + 0);
        dataMap.put("sla", false + "");
        dataMap.put("alert", false + "");
        Date deliveryDate = null;

        Map<String,Set<String>> uniqueUnitsMap=new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DashboardUtil dashboardUtil=new DashboardUtil();
        HandlesVendorMapper hp=new HandlesVendorMapper();
        SlaCompliant slaCompliant=new SlaCompliant();

        try{
            Date specificDate=dateFormat.parse(dashboardDetail.getFestivalDate());

            Map<String,Map<String, Map<String, Map<String, String>>>> dateStatusCountAllMap = dashboardDetail
                .getDateStatusCountAllMap();

            Map<String, Map<String,Map<String, String>>> statusCountMap0 = new HashMap<>();
            Map<String,Map<String, String>> statusCountMap00 = new HashMap<>();
            statusCountMap00.put("notAlloted",hp.copyMap(dataMap));
            statusCountMap00.put("processing",hp.copyMap(dataMap));
            statusCountMap0.put("unAssigned",statusCountMap00);
            Map<String,Map<String, String>> statusCountMap01 = new HashMap<>();
            statusCountMap01.put("pending",hp.copyMap(dataMap));
            statusCountMap01.put("total",hp.copyMap(dataMap));
            statusCountMap0.put("notConfirmed",statusCountMap01);
            dateStatusCountAllMap.put("festivalDate",statusCountMap0);

            Map<String,Map<String, Map<String, Map<String, String>>>> noBreachStatusCountAllMap =dashboardDetail
                .getDateStatusCountNoBreachMap();

            Map<String, Map<String,Map<String, String>>> statusCountMap4 = new HashMap<>();
            Map<String,Map<String, String>> statusCountMap08 = new HashMap<>();
            statusCountMap08.put("notAlloted",hp.copyMap(dataMap));
            statusCountMap08.put("processing",hp.copyMap(dataMap));
            statusCountMap4.put("unAssigned",statusCountMap08);
            Map<String,Map<String, String>> statusCountMap09 = new HashMap<>();
            statusCountMap09.put("pending",hp.copyMap(dataMap));
            statusCountMap09.put("total",hp.copyMap(dataMap));
            statusCountMap4.put("notConfirmed",statusCountMap09);
            noBreachStatusCountAllMap.put("festivalDate",statusCountMap4);


            Map<String,Map<String, Map<String, Map<String, String>>>> alertStatusCountAllMap =dashboardDetail
                .getDateStatusCountAlertMap();

            Map<String, Map<String,Map<String, String>>> statusCountMap8 = new HashMap<>();
            Map<String,Map<String, String>> statusCountMap016 = new HashMap<>();
            statusCountMap016.put("notAlloted",hp.copyMap(dataMap));
            statusCountMap016.put("processing",hp.copyMap(dataMap));
            statusCountMap8.put("unAssigned",statusCountMap016);
            Map<String,Map<String, String>> statusCountMap017 = new HashMap<>();
            statusCountMap017.put("pending",hp.copyMap(dataMap));
            statusCountMap017.put("total",hp.copyMap(dataMap));
            statusCountMap8.put("notConfirmed",statusCountMap017);
            alertStatusCountAllMap.put("festivalDate",statusCountMap8);

            specificDate = DateUtils.truncate(specificDate, Calendar.DAY_OF_MONTH);
            List<OrderDetailsPerOrderProduct> listOfHandelsOrderId=dashboardUtil.getHandelsOrderList(specificDate,1);


            for (OrderDetailsPerOrderProduct orderDetailsPerOrderProduct : listOfHandelsOrderId){
                try{
                    Long orderId = orderDetailsPerOrderProduct.getOrdersId();
                    Long orderProductId= orderDetailsPerOrderProduct.getOrdersProductsId();
                    // unique order Ids

                    if (orderDetailsPerOrderProduct.getDeliveryDate() != null)
                        deliveryDate = orderDetailsPerOrderProduct.getDeliveryDate();

                    String status = orderDetailsPerOrderProduct.getOrderProductStatus();
                    String deliveryTime = orderDetailsPerOrderProduct.getDeliveryTime();
                    String shippingType = Constants.getDeliveryType(orderDetailsPerOrderProduct.getShippingType());
                    int vendorId=orderDetailsPerOrderProduct.getVendorId();
                    boolean deliverystatus = (boolean) orderDetailsPerOrderProduct.getDeliveryStatus();
                    boolean flagForUniqueness=false;
                    int slaCode= orderDetailsPerOrderProduct.getSlaCode();

                    String key=orderId + "," + deliveryDate + "," + shippingType;
                    flagForUniqueness=hp.checkUniqueUnit(orderId,deliveryDate,shippingType,deliveryTime,uniqueUnitsMap,vendorId,status);

                    if (flagForUniqueness && deliveryDate.getTime() == specificDate.getTime()){
                        if(status.equals("Processed") && vendorId==72){ // unassigned -> not alloted
                            int count=Integer.parseInt(statusCountMap0.get("unAssigned").get("notAlloted").get("count"));
                            count++;
                            statusCountMap0.get("unAssigned").get("notAlloted").put("count", String.valueOf(count));

                            if (OrderUtil.isSLASatisfied(slaCode)){
                                statusCountMap0.get("unAssigned").get("notAlloted").put("sla",String.valueOf(true));

                            }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                statusCountMap0.get("unAssigned").get("notAlloted").put("alert",String.valueOf(true));

                            }
                        }
                        else if(status.equals("Processing") && vendorId==72){ // unassigned -> Processing
                            int count=Integer.parseInt(statusCountMap0.get("unAssigned").get("processing").get("count"));
                            count++;
                            statusCountMap0.get("unAssigned").get("processing").put("count", String.valueOf(count));

                            if (OrderUtil.isSLASatisfied(slaCode)){
                                statusCountMap0.get("unAssigned").get("processing").put("sla",String.valueOf(true));

                            }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {
                                statusCountMap0.get("unAssigned").get("processing").put("alert",String.valueOf(true));

                            }
                        }
                        else if (status.equals("Processed") && vendorId != 72) {
                            // (-ve sla of Processed to confirm) not Confirmed Yet -> Pending
                            // all orders which are in Processed state of that vendor ,  not Confirmed Yet -> Total

                            int count=Integer.parseInt(statusCountMap0.get("notConfirmed").get("total").get("count"));
                            count++;
                            statusCountMap0.get("notConfirmed").get("total").put("count", String.valueOf(count));

                            if (OrderUtil.isSLASatisfied(slaCode)){
                                statusCountMap0.get("notConfirmed").get("total").put("sla",String.valueOf(true));

                            }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                int alertCount = Integer.parseInt(statusCountMap0.get("notConfirmed").get("pending").get("count"));
                                alertCount++;
                                statusCountMap0.get("notConfirmed").get("pending").put("count", String.valueOf(alertCount));
                                statusCountMap0.get("notConfirmed").get("pending").put("alert",String.valueOf(true));

                            }

                        }
                    }


                }catch (Exception exception){
                    logger.error("Error in preparing order for specific date in handel panel dashboard",exception);
                }
            }


        }catch (Exception exception){
            logger.error("Error in preparing order for specific date in handel panel dashboard",exception);
        }

    }
}
