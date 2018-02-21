package com.igp.handles.admin.mappers.Dashboard;

import com.igp.admin.mappers.marketPlace.Constants;
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
        Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdNoBreachMap = dashboardDetail.getDateStatusOrderIdNoBreachMap();
        Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdAlertMap    = dashboardDetail.getDateStatusOrderIdAlertMap();

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
        noBreachStatusCountAllMap.put("past",statusCountMap4);

        Map<String, Map<String,Map<String, String>>> statusCountMap5 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap010 = new HashMap<>();
        statusCountMap010.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap010.put("processing",hp.copyMap(dataMap));
        statusCountMap5.put("unAssigned",statusCountMap010);
        Map<String,Map<String, String>> statusCountMap011 = new HashMap<>();
        statusCountMap011.put("pending",hp.copyMap(dataMap));
        statusCountMap011.put("total",hp.copyMap(dataMap));
        statusCountMap5.put("notConfirmed",statusCountMap011);
        noBreachStatusCountAllMap.put("today",statusCountMap5);

        Map<String, Map<String,Map<String, String>>> statusCountMap6 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap012 = new HashMap<>();
        statusCountMap012.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap012.put("processing",hp.copyMap(dataMap));
        statusCountMap6.put("unAssigned",statusCountMap012);
        Map<String,Map<String, String>> statusCountMap013 = new HashMap<>();
        statusCountMap013.put("pending",hp.copyMap(dataMap));
        statusCountMap013.put("total",hp.copyMap(dataMap));
        statusCountMap6.put("notConfirmed",statusCountMap013);
        noBreachStatusCountAllMap.put("tomorrow",statusCountMap6);

        Map<String, Map<String,Map<String, String>>> statusCountMap7 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap014 = new HashMap<>();
        statusCountMap014.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap014.put("processing",hp.copyMap(dataMap));
        statusCountMap7.put("unAssigned",statusCountMap014);
        Map<String,Map<String, String>> statusCountMap015 = new HashMap<>();
        statusCountMap015.put("pending",hp.copyMap(dataMap));
        statusCountMap015.put("total",hp.copyMap(dataMap));
        statusCountMap7.put("notConfirmed",statusCountMap015);
        noBreachStatusCountAllMap.put("future",statusCountMap7);


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
        alertStatusCountAllMap.put("past",statusCountMap8);

        Map<String, Map<String,Map<String, String>>> statusCountMap9 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap018 = new HashMap<>();
        statusCountMap018.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap018.put("processing",hp.copyMap(dataMap));
        statusCountMap9.put("unAssigned",statusCountMap018);
        Map<String,Map<String, String>> statusCountMap019 = new HashMap<>();
        statusCountMap019.put("pending",hp.copyMap(dataMap));
        statusCountMap019.put("total",hp.copyMap(dataMap));
        statusCountMap9.put("notConfirmed",statusCountMap019);
        alertStatusCountAllMap.put("today",statusCountMap9);

        Map<String, Map<String,Map<String, String>>> statusCountMap10 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap020 = new HashMap<>();
        statusCountMap020.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap020.put("processing",hp.copyMap(dataMap));
        statusCountMap10.put("unAssigned",statusCountMap020);
        Map<String,Map<String, String>> statusCountMap021 = new HashMap<>();
        statusCountMap021.put("pending",hp.copyMap(dataMap));
        statusCountMap021.put("total",hp.copyMap(dataMap));
        statusCountMap10.put("notConfirmed",statusCountMap021);
        alertStatusCountAllMap.put("tomorrow",statusCountMap10);

        Map<String, Map<String,Map<String, String>>> statusCountMap11 = new HashMap<>();
        Map<String,Map<String, String>> statusCountMap022 = new HashMap<>();
        statusCountMap022.put("notAlloted",hp.copyMap(dataMap));
        statusCountMap022.put("processing",hp.copyMap(dataMap));
        statusCountMap11.put("unAssigned",statusCountMap022);
        Map<String,Map<String, String>> statusCountMap023 = new HashMap<>();
        statusCountMap023.put("pending",hp.copyMap(dataMap));
        statusCountMap023.put("total",hp.copyMap(dataMap));
        statusCountMap11.put("notConfirmed",statusCountMap023);
        alertStatusCountAllMap.put("future",statusCountMap11);


        Map<String,String> notShippedTotalOrderCount = hp.copyMap(dataMap);
        Map<String,String> notShippedPendingOrderCount = hp.copyMap(dataMap);
        Map<String,String> notDeliveredTotalOrderCount = hp.copyMap(dataMap);
        Map<String,String> notDeliveredPendingOrderCount = hp.copyMap(dataMap);

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
                    flagForUniqueness=hp.checkUniqueUnit(orderId,deliveryDate,shippingType,deliveryTime,uniqueUnitsMap,vendorId);


                    if (flagForUniqueness){

                        if(status.equals("Processed") && vendorId==72){ // unassigned -> not alloted
                            //past
                            orderTotalWhole++;
                            notAssignedOrdersTotalWhole++;
                            if (deliveryDate.getTime() < todayDate.getTime()){
                                int count=Integer.parseInt(statusCountMap0.get("unAssigned").get("notAlloted").get("count"));
                                count++;
                                statusCountMap0.get("unAssigned").get("notAlloted").put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap4.get("unAssigned").get("notAlloted").get("count"));
                                    noBreachCount++;
                                    statusCountMap4.get("unAssigned").get("notAlloted").put("count", noBreachCount + "");
                                    statusCountMap4.get("unAssigned").get("notAlloted").put("sla", true + "");
                                    statusCountMap0.get("unAssigned").get("notAlloted").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap8.get("unAssigned").get("notAlloted").get("count"));
                                    alertCount++;
                                    statusCountMap8.get("unAssigned").get("notAlloted").put("count", alertCount + "");
                                    statusCountMap8.get("unAssigned").get("notAlloted").put("sla", true + "");
                                    statusCountMap0.get("unAssigned").get("notAlloted").put("sla",true+"");

                                }
                            }
                            else if (deliveryDate.getTime() == todayDate.getTime()){ // today
                                int count=Integer.parseInt(statusCountMap1.get("unAssigned").get("notAlloted").get("count"));
                                count++;
                                statusCountMap1.get("unAssigned").get("notAlloted").put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap5.get("unAssigned").get("notAlloted").get("count"));
                                    noBreachCount++;
                                    statusCountMap5.get("unAssigned").get("notAlloted").put("count", noBreachCount + "");
                                    statusCountMap5.get("unAssigned").get("notAlloted").put("sla", true + "");
                                    statusCountMap1.get("unAssigned").get("notAlloted").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap9.get("unAssigned").get("notAlloted").get("count"));
                                    alertCount++;
                                    statusCountMap9.get("unAssigned").get("notAlloted").put("count", alertCount + "");
                                    statusCountMap9.get("unAssigned").get("notAlloted").put("sla", true + "");
                                    statusCountMap1.get("unAssigned").get("notAlloted").put("sla",true+"");

                                }
                            }
                            else if(deliveryDate.getTime() == tomorrowDate.getTime()){ // tommorow
                                int count=Integer.parseInt(statusCountMap2.get("unAssigned").get("notAlloted").get("count"));
                                count++;
                                statusCountMap2.get("unAssigned").get("notAlloted").put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap6.get("unAssigned").get("notAlloted").get("count"));
                                    noBreachCount++;
                                    statusCountMap6.get("unAssigned").get("notAlloted").put("count", noBreachCount + "");
                                    statusCountMap6.get("unAssigned").get("notAlloted").put("sla", true + "");
                                    statusCountMap2.get("unAssigned").get("notAlloted").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap10.get("unAssigned").get("notAlloted").get("count"));
                                    alertCount++;
                                    statusCountMap10.get("unAssigned").get("notAlloted").put("count", alertCount + "");
                                    statusCountMap10.get("unAssigned").get("notAlloted").put("sla", true + "");
                                    statusCountMap2.get("unAssigned").get("notAlloted").put("sla",true+"");

                                }
                            }
                            else if (deliveryDate.getTime() >= futureDate.getTime()){ // future
                                int count=Integer.parseInt(statusCountMap3.get("unAssigned").get("notAlloted").get("count"));
                                count++;
                                statusCountMap3.get("unAssigned").get("notAlloted").put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap7.get("unAssigned").get("notAlloted").get("count"));
                                    noBreachCount++;
                                    statusCountMap7.get("unAssigned").get("notAlloted").put("count", noBreachCount + "");
                                    statusCountMap7.get("unAssigned").get("notAlloted").put("sla", true + "");
                                    statusCountMap3.get("unAssigned").get("notAlloted").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap11.get("unAssigned").get("notAlloted").get("count"));
                                    alertCount++;
                                    statusCountMap11.get("unAssigned").get("notAlloted").put("count", alertCount + "");
                                    statusCountMap11.get("unAssigned").get("notAlloted").put("sla", true + "");
                                    statusCountMap3.get("unAssigned").get("notAlloted").put("sla",true+"");

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
                                statusCountMap0.get("unAssigned").get("processing").put("count", count + "");

                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap4.get("unAssigned").get("processing").get("count"));
                                    noBreachCount++;
                                    statusCountMap4.get("unAssigned").get("processing").put("count", noBreachCount + "");
                                    statusCountMap4.get("unAssigned").get("processing").put("sla", true + "");
                                    statusCountMap0.get("unAssigned").get("processing").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap8.get("unAssigned").get("processing").get("count"));
                                    alertCount++;
                                    statusCountMap8.get("unAssigned").get("processing").put("count", alertCount + "");
                                    statusCountMap8.get("unAssigned").get("processing").put("sla", true + "");
                                    statusCountMap0.get("unAssigned").get("processing").put("sla",true+"");

                                }
                            }
                            else if (deliveryDate.getTime() == todayDate.getTime()){ // today
                                int count=Integer.parseInt(statusCountMap1.get("unAssigned").get("processing").get("count"));
                                count++;
                                statusCountMap1.get("unAssigned").get("processing").put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap5.get("unAssigned").get("processing").get("count"));
                                    noBreachCount++;
                                    statusCountMap5.get("unAssigned").get("processing").put("count", noBreachCount + "");
                                    statusCountMap5.get("unAssigned").get("processing").put("sla", true + "");
                                    statusCountMap1.get("unAssigned").get("processing").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap9.get("unAssigned").get("processing").get("count"));
                                    alertCount++;
                                    statusCountMap9.get("unAssigned").get("processing").put("count", alertCount + "");
                                    statusCountMap9.get("unAssigned").get("processing").put("sla", true + "");
                                    statusCountMap1.get("unAssigned").get("processing").put("sla",true+"");

                                }
                            }
                            else if(deliveryDate.getTime() == tomorrowDate.getTime()){ // tommorow
                                int count=Integer.parseInt(statusCountMap2.get("unAssigned").get("processing").get("count"));
                                count++;
                                statusCountMap2.get("unAssigned").get("processing").put("count", count + "");



                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap6.get("unAssigned").get("processing").get("count"));
                                    noBreachCount++;
                                    statusCountMap6.get("unAssigned").get("processing").put("count", noBreachCount + "");
                                    statusCountMap6.get("unAssigned").get("processing").put("sla", true + "");
                                    statusCountMap2.get("unAssigned").get("processing").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap10.get("unAssigned").get("processing").get("count"));
                                    alertCount++;
                                    statusCountMap10.get("unAssigned").get("processing").put("count", alertCount + "");
                                    statusCountMap10.get("unAssigned").get("processing").put("sla", true + "");
                                    statusCountMap2.get("unAssigned").get("processing").put("sla",true+"");

                                }
                            }
                            else if (deliveryDate.getTime() >= futureDate.getTime()){ // future
                                int count=Integer.parseInt(statusCountMap3.get("unAssigned").get("processing").get("count"));
                                count++;
                                statusCountMap3.get("unAssigned").get("processing").put("count", count + "");

                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap7.get("unAssigned").get("processing").get("count"));
                                    noBreachCount++;
                                    statusCountMap7.get("unAssigned").get("processing").put("count", noBreachCount + "");
                                    statusCountMap7.get("unAssigned").get("processing").put("sla", true + "");
                                    statusCountMap3.get("unAssigned").get("processing").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap11.get("unAssigned").get("processing").get("count"));
                                    alertCount++;
                                    statusCountMap11.get("unAssigned").get("processing").put("count", alertCount + "");
                                    statusCountMap11.get("unAssigned").get("processing").put("sla", true + "");
                                    statusCountMap3.get("unAssigned").get("processing").put("sla",true+"");

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
                                statusCountMap0.get("notConfirmed").get("total").put("count", count + "");

                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap4.get("notConfirmed").get("total").get("count"));
                                    noBreachCount++;
                                    statusCountMap4.get("notConfirmed").get("total").put("count", noBreachCount + "");
                                    statusCountMap4.get("notConfirmed").get("total").put("sla", true + "");
                                    statusCountMap0.get("notConfirmed").get("total").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap8.get("notConfirmed").get("pending").get("count"));
                                    alertCount++;
                                    statusCountMap8.get("notConfirmed").get("pending").put("count", alertCount + "");
                                    statusCountMap8.get("notConfirmed").get("pending").put("sla", true + "");
                                    statusCountMap0.get("notConfirmed").get("pending").put("sla",true+"");

                                }
                            }
                            else if (deliveryDate.getTime() == todayDate.getTime()){ // today
                                int count=Integer.parseInt(statusCountMap1.get("notConfirmed").get("total").get("count"));
                                count++;
                                statusCountMap1.get("notConfirmed").get("total").put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap5.get("notConfirmed").get("total").get("count"));
                                    noBreachCount++;
                                    statusCountMap5.get("notConfirmed").get("total").put("count", noBreachCount + "");
                                    statusCountMap5.get("notConfirmed").get("total").put("sla", true + "");
                                    statusCountMap1.get("notConfirmed").get("total").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap9.get("notConfirmed").get("pending").get("count"));
                                    alertCount++;
                                    statusCountMap9.get("notConfirmed").get("pending").put("count", alertCount + "");
                                    statusCountMap9.get("notConfirmed").get("pending").put("sla", true + "");
                                    statusCountMap1.get("notConfirmed").get("pending").put("sla",true+"");

                                }
                            }
                            else if(deliveryDate.getTime() == tomorrowDate.getTime()){ // tommorow
                                int count=Integer.parseInt(statusCountMap2.get("notConfirmed").get("total").get("count"));
                                count++;
                                statusCountMap2.get("notConfirmed").get("total").put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap6.get("notConfirmed").get("total").get("count"));
                                    noBreachCount++;
                                    statusCountMap6.get("notConfirmed").get("total").put("count", noBreachCount + "");
                                    statusCountMap6.get("notConfirmed").get("total").put("sla", true + "");
                                    statusCountMap2.get("notConfirmed").get("total").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap10.get("notConfirmed").get("pending").get("count"));
                                    alertCount++;
                                    statusCountMap10.get("notConfirmed").get("pending").put("count", alertCount + "");
                                    statusCountMap10.get("notConfirmed").get("pending").put("sla", true + "");
                                    statusCountMap2.get("notConfirmed").get("pending").put("sla",true+"");

                                }
                            }
                            else if (deliveryDate.getTime() >= futureDate.getTime()){ // future
                                int count=Integer.parseInt(statusCountMap3.get("notConfirmed").get("total").get("count"));
                                count++;
                                statusCountMap3.get("notConfirmed").get("total").put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode)){

                                    int noBreachCount=Integer.parseInt(statusCountMap7.get("notConfirmed").get("total").get("count"));
                                    noBreachCount++;
                                    statusCountMap7.get("notConfirmed").get("total").put("count", noBreachCount + "");
                                    statusCountMap7.get("notConfirmed").get("total").put("sla", true + "");
                                    statusCountMap3.get("notConfirmed").get("total").put("sla",true+"");

                                }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                    int alertCount = Integer.parseInt(statusCountMap11.get("notConfirmed").get("pending").get("count"));
                                    alertCount++;
                                    statusCountMap11.get("notConfirmed").get("pending").put("count", alertCount + "");
                                    statusCountMap11.get("notConfirmed").get("pending").put("sla", true + "");
                                    statusCountMap3.get("notConfirmed").get("pending").put("sla",true+"");

                                }
                            }


                        }else if(status.equals("Confirmed") && vendorId != 72){
                            // (-ve sla of Confirmed to OutOfDelivery i.e *Shipped* ) not Shipped Yet -> Pending
                            // all orders which are in Confirmed state of that vendor ,  not Shipped Yet -> Total


                            if (deliveryDate.getTime() <= todayDate.getTime()){
                                orderTotalWhole++;
                                int count=Integer.parseInt(notShippedTotalOrderCount.get("count"));
                                count++;
                                notShippedTotalOrderCount.put("count",count+"");
                                notShippedTotalOrderCount.put("sla",true+"");
                                if(OrderUtil.isHighAlertActionRequired(slaCode)){
                                    orderTotalWhole++;
                                    int alertCount=Integer.parseInt(notShippedPendingOrderCount.get("count"));
                                    alertCount++;
                                    notShippedPendingOrderCount.put("count",alertCount+"");
                                    notShippedPendingOrderCount.put("alert", true + "");
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
                                notDeliveredTotalOrderCount.put("count",count+"");
                                notDeliveredTotalOrderCount.put("sla",true+"");
                                if(OrderUtil.isHighAlertActionRequired(slaCode)){
                                    orderTotalWhole++;
                                    int alertCount=Integer.parseInt(notDeliveredPendingOrderCount.get("count"));
                                    alertCount++;
                                    notDeliveredPendingOrderCount.put("count",alertCount+"");
                                    notDeliveredPendingOrderCount.put("alert", true + "");
                                }
                            }


                        }
                    }
                    dashboardDetail.setNotShippedTotalOrderCount(notShippedTotalOrderCount);
                    dashboardDetail.setNotShippedPendingOrderCount(notShippedPendingOrderCount);
                    dashboardDetail.setNotDeliveredTotalOrderCount(notDeliveredTotalOrderCount);
                    dashboardDetail.setNotDeliveredPendingOrderCount(notDeliveredPendingOrderCount);
                    dashboardDetail.setOrderTotalWhole(orderTotalWhole);
                    dashboardDetail.setNotAssignedOrdersTotalWhole(notAssignedOrdersTotalWhole);
                    dashboardDetail.setNotConfirmedOrdersTotalWhole(notConfirmedOrdersTotalWhole);
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
                    flagForUniqueness=hp.checkUniqueUnit(orderId,deliveryDate,shippingType,deliveryTime,uniqueUnitsMap,vendorId);

                    if (flagForUniqueness && deliveryDate.getTime() == specificDate.getTime()){
                        if(status.equals("Processed") && vendorId==72){ // unassigned -> not alloted
                            int count=Integer.parseInt(statusCountMap0.get("unAssigned").get("notAlloted").get("count"));
                            count++;
                            statusCountMap0.get("unAssigned").get("notAlloted").put("count", count + "");

                            if (OrderUtil.isSLASatisfied(slaCode)){

                                int noBreachCount=Integer.parseInt(statusCountMap4.get("unAssigned").get("notAlloted").get("count"));
                                noBreachCount++;
                                statusCountMap4.get("unAssigned").get("notAlloted").put("count", noBreachCount + "");
                                statusCountMap4.get("unAssigned").get("notAlloted").put("sla", true + "");
                                statusCountMap0.get("unAssigned").get("notAlloted").put("sla",true+"");

                            }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                int alertCount = Integer.parseInt(statusCountMap8.get("unAssigned").get("notAlloted").get("count"));
                                alertCount++;
                                statusCountMap8.get("unAssigned").get("notAlloted").put("count", alertCount + "");
                                statusCountMap8.get("unAssigned").get("notAlloted").put("sla", true + "");
                                statusCountMap0.get("unAssigned").get("notAlloted").put("sla",true+"");

                            }
                        }
                        else if(status.equals("Processing") && vendorId==72){ // unassigned -> Processing
                            int count=Integer.parseInt(statusCountMap0.get("unAssigned").get("processing").get("count"));
                            count++;
                            statusCountMap0.get("unAssigned").get("processing").put("count", count + "");

                            if (OrderUtil.isSLASatisfied(slaCode)){

                                int noBreachCount=Integer.parseInt(statusCountMap4.get("unAssigned").get("processing").get("count"));
                                noBreachCount++;
                                statusCountMap4.get("unAssigned").get("processing").put("count", noBreachCount + "");
                                statusCountMap4.get("unAssigned").get("processing").put("sla", true + "");
                                statusCountMap0.get("unAssigned").get("processing").put("sla",true+"");

                            }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                int alertCount = Integer.parseInt(statusCountMap8.get("unAssigned").get("notAlloted").get("count"));
                                alertCount++;
                                statusCountMap8.get("unAssigned").get("processing").put("count", alertCount + "");
                                statusCountMap8.get("unAssigned").get("processing").put("sla", true + "");
                                statusCountMap0.get("unAssigned").get("processing").put("sla",true+"");

                            }
                        }
                        else if (status.equals("Processed") && vendorId != 72) {
                            // (-ve sla of Processed to confirm) not Confirmed Yet -> Pending
                            // all orders which are in Processed state of that vendor ,  not Confirmed Yet -> Total

                            int count=Integer.parseInt(statusCountMap0.get("notConfirmed").get("total").get("count"));
                            count++;
                            statusCountMap0.get("notConfirmed").get("total").put("count", count + "");

                            if (OrderUtil.isSLASatisfied(slaCode)){

                                int noBreachCount=Integer.parseInt(statusCountMap4.get("notConfirmed").get("total").get("count"));
                                noBreachCount++;
                                statusCountMap4.get("notConfirmed").get("total").put("count", noBreachCount + "");
                                statusCountMap4.get("notConfirmed").get("total").put("sla", true + "");
                                statusCountMap0.get("notConfirmed").get("total").put("sla",true+"");

                            }else if (OrderUtil.isHighAlertActionRequired(slaCode)) {

                                int alertCount = Integer.parseInt(statusCountMap8.get("notConfirmed").get("pending").get("count"));
                                alertCount++;
                                statusCountMap8.get("notConfirmed").get("pending").put("count", alertCount + "");
                                statusCountMap8.get("notConfirmed").get("pending").put("sla", true + "");
                                statusCountMap0.get("notConfirmed").get("pending").put("sla",true+"");

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
