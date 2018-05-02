package com.igp.handles.vendorpanel.mappers.Vendor;

import com.igp.admin.marketplace.mappers.Constants;
import com.igp.handles.admin.utils.Order.SlaCompliant;
import com.igp.handles.vendorpanel.models.Vendor.OrderDetailsPerOrderProduct;
import com.igp.handles.vendorpanel.models.Vendor.VendorCountDetail;
import com.igp.handles.vendorpanel.utils.Order.OrderUtil;
import com.igp.handles.vendorpanel.utils.Vendor.VendorUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shanky on 8/7/17.
 */
public class HandlesVendorMapper {
    private static final Logger logger = LoggerFactory.getLogger(HandlesVendorMapper.class);

    public static int cnt=0;
    public VendorCountDetail getVendorCountDetail(String scopeId, String fkAssociateId, Date specificDate){
        VendorCountDetail vendorCountDetail = new VendorCountDetail();
        Set<Map<String, String>> outForDeliveryOrderIds = vendorCountDetail.getOutOfDeliveryOrderIds();
        Set<Map<String, String>> slaOutForDeliveryOrderIds = vendorCountDetail.getSlaOutOfDeliveryOrderIds();
        Set<Map<String, String>> alertOutForDeliveryOrderIds = vendorCountDetail.getAlertOutOfDeliveryOrderIds();
        Map<String, Map<String, Set<Map<String, String>>>> dateStatusOrderIdAllMap      = vendorCountDetail.getDateStatusOrderIdAllMap();
        Map<String, Map<String, Set<Map<String, String>>>> dateStatusOrderIdNoBreachMap = vendorCountDetail.getDateStatusOrderIdNoBreachMap();
        Map<String, Map<String, Set<Map<String, String>>>> dateStatusOrderIdAlertMap    = vendorCountDetail.getDateStatusOrderIdAlertMap();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SlaCompliant slaCompliant=new SlaCompliant();
        OrderUtil orderUtil=new OrderUtil();
        try{
            Set<String> uniqueCombinations = new HashSet<>();
            Date todayDate = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
//            Date todayDate = DateUtils.truncate(dateFormat.parse("2017-01-01"), Calendar.DAY_OF_MONTH);
            Date tomorrowDate = DateUtils.addDays(todayDate, 1);
            Date futureDate = DateUtils.addDays(todayDate, 2);
            Date pastDate = DateUtils.addDays(todayDate, -7);
            VendorUtil handleVendorUtil=new VendorUtil();
            if (specificDate == null)
            {
                specificDate = handleVendorUtil.getFestivalDate(todayDate);
            }
            vendorCountDetail.setFestivalDate(dateFormat.format(specificDate));

            Map<String,Set<String>> uniqueUnitsMap=new HashMap<>();

            List<OrderDetailsPerOrderProduct> listOfOrderIdAsPerVendor=handleVendorUtil.getOrderListForVendor( fkAssociateId,  pastDate,0);

//            Set<Map<String, String>> dataOrderIdMap = new HashMap<>();
//            dataOrderIdMap.put("all",new HashSet<>());
//            dataOrderIdMap.put("sla",new HashSet<>());
//            dataOrderIdMap.put("alert",new HashSet<>());

            // ------ Second tab i.e all order tab related -------
            Map<String,  Set<Map<String, String>>> statusCountOrderIdMap0 = new HashMap<>();
            statusCountOrderIdMap0.put("Processed",new HashSet<>());
            statusCountOrderIdMap0.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAllMap.put("past",statusCountOrderIdMap0);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap1 = new HashMap<>();
            statusCountOrderIdMap1.put("Processed",new HashSet<>());
            statusCountOrderIdMap1.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAllMap.put("today",statusCountOrderIdMap1);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap2 = new HashMap<>();
            statusCountOrderIdMap2.put("Processed",new HashSet<>());
            statusCountOrderIdMap2.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAllMap.put("tomorrow",statusCountOrderIdMap2);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap3 = new HashMap<>();
            statusCountOrderIdMap3.put("Processed",new HashSet<>());
            statusCountOrderIdMap3.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAllMap.put("future",statusCountOrderIdMap3);

            // ------ Second tab i.e sla tab related -------

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap40 = new HashMap<>();
            statusCountOrderIdMap40.put("Processed",new HashSet<>());
            statusCountOrderIdMap40.put("Confirmed",new HashSet<>());
            dateStatusOrderIdNoBreachMap.put("past",statusCountOrderIdMap40);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap4 = new HashMap<>();
            statusCountOrderIdMap4.put("Processed",new HashSet<>());
            statusCountOrderIdMap4.put("Confirmed",new HashSet<>());
            dateStatusOrderIdNoBreachMap.put("today",statusCountOrderIdMap4);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap5 = new HashMap<>();
            statusCountOrderIdMap5.put("Processed",new HashSet<>());
            statusCountOrderIdMap5.put("Confirmed",new HashSet<>());
            dateStatusOrderIdNoBreachMap.put("tomorrow",statusCountOrderIdMap5);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap6 = new HashMap<>();
            statusCountOrderIdMap6.put("Processed",new HashSet<>());
            statusCountOrderIdMap6.put("Confirmed",new HashSet<>());
            dateStatusOrderIdNoBreachMap.put("future",statusCountOrderIdMap6);

            // ------ third tab i.e alert tab related -------

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap70 = new HashMap<>();
            statusCountOrderIdMap70.put("Processed",new HashSet<>());
            statusCountOrderIdMap70.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAlertMap.put("past",statusCountOrderIdMap70);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap7 = new HashMap<>();
            statusCountOrderIdMap7.put("Processed",new HashSet<>());
            statusCountOrderIdMap7.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAlertMap.put("today",statusCountOrderIdMap7);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap8 = new HashMap<>();
            statusCountOrderIdMap8.put("Processed",new HashSet<>());
            statusCountOrderIdMap8.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAlertMap.put("tomorrow",statusCountOrderIdMap8);

            Map<String, Set<Map<String, String>>> statusCountOrderIdMap9 = new HashMap<>();
            statusCountOrderIdMap9.put("Processed",new HashSet<>());
            statusCountOrderIdMap9.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAlertMap.put("future",statusCountOrderIdMap9);


            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("count", "" + 0);
            dataMap.put("sla", false + "");
            dataMap.put("alert", false + "");

            Map<String, Map<String, Map<String, String>>> dateStatusCountAllMap = vendorCountDetail
                .getDateStatusCountAllMap();

            Map<String, Map<String, String>> statusCountMap0 = new HashMap<>();
            statusCountMap0.put("Processed", copyMap(dataMap));
            statusCountMap0.put("Confirmed", copyMap(dataMap));
            dateStatusCountAllMap.put("past", statusCountMap0);

            Map<String, Map<String, String>> statusCountMap1 = new HashMap<>();
            statusCountMap1.put("Processed", copyMap(dataMap));
            statusCountMap1.put("Confirmed", copyMap(dataMap));
            dateStatusCountAllMap.put("today", statusCountMap1);

            Map<String, Map<String, String>> statusCountMap2 = new HashMap<>();
            statusCountMap2.put("Processed", copyMap(dataMap));
            statusCountMap2.put("Confirmed", copyMap(dataMap));
            dateStatusCountAllMap.put("tomorrow", statusCountMap2);

            Map<String, Map<String, String>> statusCountMap3 = new HashMap<>();
            statusCountMap3.put("Processed", copyMap(dataMap));
            statusCountMap3.put("Confirmed", copyMap(dataMap));
            dateStatusCountAllMap.put("future", statusCountMap3);

            // populate vendorCountDetail for specific Date
            getVendorCountDetailForDate(scopeId, fkAssociateId, vendorCountDetail);

            Map<String, Map<String, Map<String, String>>> noBreachStatusCountAllMap = vendorCountDetail
                .getDateStatusCountNoBreachMap();

            Map<String, Map<String, String>> statusCountMap40 = new HashMap<>();
            statusCountMap40.put("Processed", copyMap(dataMap));
            statusCountMap40.put("Confirmed", copyMap(dataMap));
            noBreachStatusCountAllMap.put("past", statusCountMap40);

            Map<String, Map<String, String>> statusCountMap4 = new HashMap<>();
            statusCountMap4.put("Processed", copyMap(dataMap));
            statusCountMap4.put("Confirmed", copyMap(dataMap));
            noBreachStatusCountAllMap.put("today", statusCountMap4);

            Map<String, Map<String, String>> statusCountMap5 = new HashMap<>();
            statusCountMap5.put("Processed", copyMap(dataMap));
            statusCountMap5.put("Confirmed", copyMap(dataMap));
            noBreachStatusCountAllMap.put("tomorrow", statusCountMap5);

            Map<String, Map<String, String>> statusCountMap6 = new HashMap<>();
            statusCountMap6.put("Processed", copyMap(dataMap));
            statusCountMap6.put("Confirmed", copyMap(dataMap));
            noBreachStatusCountAllMap.put("future", statusCountMap6);

            Map<String, Map<String, Map<String, String>>> alertStatusCountAllMap = vendorCountDetail
                .getDateStatusCountAlertMap();
            Map<String, Map<String, String>> statusCountMap70 = new HashMap<>();
            statusCountMap70.put("Processed", copyMap(dataMap));
            statusCountMap70.put("Confirmed", copyMap(dataMap));
            alertStatusCountAllMap.put("past", statusCountMap70);

            Map<String, Map<String, String>> statusCountMap7 = new HashMap<>();
            statusCountMap7.put("Processed", copyMap(dataMap));
            statusCountMap7.put("Confirmed", copyMap(dataMap));
            alertStatusCountAllMap.put("today", statusCountMap7);

            Map<String, Map<String, String>> statusCountMap8 = new HashMap<>();
            statusCountMap8.put("Processed", copyMap(dataMap));
            statusCountMap8.put("Confirmed", copyMap(dataMap));
            alertStatusCountAllMap.put("tomorrow", statusCountMap8);

            Map<String, Map<String, String>> statusCountMap9 = new HashMap<>();
            statusCountMap9.put("Processed", copyMap(dataMap));
            statusCountMap9.put("Confirmed", copyMap(dataMap));
            alertStatusCountAllMap.put("future", statusCountMap9);

            // orders_id | delivered_date | deliveryDate | orders_product_status
            // delivery_status | delivery_time | shipping_type
            // | sla code | outforDeliveryDate
            Date deliveredDate = null;
            Date deliveryDate = null;
            Date outForDeliveryDate = null;
            int orderTotalWhole=0,newOrderTotalWhole=0,confirmOrderTotalWhole=0,outOfDeliveryOrderTotalWhole=0;
            int orderTotalActionRequired=0,newOrderTotalActionRequired=0,confirmOrderTotalWholeActionRequired=0
                ,outOfDeliveryOrderTotalActionRequired=0;
            int orderTotalHighAlert=0,newOrderTotalHighAlert=0,confirmOrderTotalWholeHighAlert=0
                ,outOfDeliveryOrderTotalHighAlert=0;

            Map<String,Map<String,Map<String,String>>> leftOutOrderToOrderProductIdMap2=vendorCountDetail.getLeftOutOrderToOrderProductIdMap2();
            Map<String, Map<String, String>> leftOutOrderIdMap1 = new HashMap<>();
            leftOutOrderIdMap1.put("Processed", new HashMap<>());
            leftOutOrderIdMap1.put("Confirmed", new HashMap<>());
            leftOutOrderToOrderProductIdMap2.put("past",leftOutOrderIdMap1);

            Map<String, Map<String, String>> leftOutOrderIdMap2 = new HashMap<>();
            leftOutOrderIdMap2.put("Processed", new HashMap<>());
            leftOutOrderIdMap2.put("Confirmed", new HashMap<>());
            leftOutOrderToOrderProductIdMap2.put("today",leftOutOrderIdMap2);

            Map<String, Map<String, String>> leftOutOrderIdMap3 = new HashMap<>();
            leftOutOrderIdMap3.put("Processed", new HashMap<>());
            leftOutOrderIdMap3.put("Confirmed", new HashMap<>());
            leftOutOrderToOrderProductIdMap2.put("tomorrow",leftOutOrderIdMap3);

            Map<String, Map<String, String>> leftOutOrderIdMap4 = new HashMap<>();
            leftOutOrderIdMap4.put("Processed", new HashMap<>());
            leftOutOrderIdMap4.put("Confirmed", new HashMap<>());
            leftOutOrderToOrderProductIdMap2.put("future",leftOutOrderIdMap4);

            for (OrderDetailsPerOrderProduct orderDetailsPerOrderProduct : listOfOrderIdAsPerVendor){
                try
                {
                    Long orderId = orderDetailsPerOrderProduct.getOrdersId();
                    Long orderProductId= orderDetailsPerOrderProduct.getOrdersProductsId();
                    // unique order Ids

                    if (orderDetailsPerOrderProduct.getDeliveredDate() != null)
                        deliveredDate = orderDetailsPerOrderProduct.getDeliveredDate();
                    if (orderDetailsPerOrderProduct.getDeliveryDate() != null)
                        deliveryDate = orderDetailsPerOrderProduct.getDeliveryDate();
                    if (orderDetailsPerOrderProduct.getOutForDeliveryDate() != null)
                    {
                        outForDeliveryDate = orderDetailsPerOrderProduct.getOutForDeliveryDate();
                    }

                    String status = orderDetailsPerOrderProduct.getOrderProductStatus();
                    String deliveryTime = orderDetailsPerOrderProduct.getDeliveryTime();
                    String shippingType = Constants.getDeliveryType(orderDetailsPerOrderProduct.getShippingType());
                    boolean deliverystatus = (boolean) orderDetailsPerOrderProduct.getDeliveryStatus();

                    boolean flagForUniqueness=false;
                    Map<String, String> orderIdData = new HashMap<>();
//                    int slaCode= orderDetailsPerOrderProduct.getSlaCode();
                    int slaCode=slaCompliant.generateSlacodeForAll(orderDetailsPerOrderProduct,0);
                    orderUtil.saveSlaCodes(orderId.intValue(),orderProductId.intValue(),status,slaCode,deliverystatus);
                    int deliveryAttemptFlag=orderDetailsPerOrderProduct.getDeliveryAttemptFlag();
                    if (status.equals("Processed") && deliverystatus == false)
                    {
                        orderIdData.put("orderId", orderId + "");
                        orderIdData.put("orderProductId", orderProductId + "");
                        orderIdData.put("sla", false + "");
                        orderIdData.put("alert", false + "");

                        String key=orderId + "," + deliveryDate + "," + shippingType;
                        flagForUniqueness=checkUniqueUnit(orderId,deliveryDate,shippingType,deliveryTime,uniqueUnitsMap,Integer.parseInt(fkAssociateId),status);

                        logger.debug("Key : status" + status + "-" + key);
                        if (flagForUniqueness)
                        {
                            orderTotalWhole++;
                            newOrderTotalWhole++;
                            if (deliveryDate.getTime() < todayDate.getTime())
                            {
                                int count = Integer.parseInt(statusCountMap0.get(status).get("count"));
                                count++;
                                statusCountMap0.get(status).put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode))
                                {
                                    orderTotalActionRequired++;
                                    newOrderTotalActionRequired++;
                                    int noBreachCount = Integer.parseInt(statusCountMap40.get(status).get("count"));
                                    noBreachCount++;
                                    statusCountMap40.get(status).put("count", noBreachCount + "");
                                    statusCountMap40.get(status).put("sla", true + "");
                                    statusCountMap0.get(status).put("sla", true + "");

                                    orderIdData.put("sla", true + "");
                                    orderIdData.put("alert", false + "");
                                    statusCountOrderIdMap0.get(status).add(orderIdData);
                                    statusCountOrderIdMap40.get(status).add(copyMap(orderIdData));
                                }
                                else if (OrderUtil.isHighAlertActionRequired(slaCode))
                                {
                                    orderTotalHighAlert++;
                                    newOrderTotalHighAlert++;
                                    // put in high alert action map
                                    int alertCount = Integer.parseInt(statusCountMap70.get(status).get("count"));
                                    alertCount++;
                                    statusCountMap70.get(status).put("count", alertCount + "");
                                    statusCountMap70.get(status).put("alert", true + "");
                                    statusCountMap0.get(status).put("alert", true + "");

                                    orderIdData.put("sla", false + "");
                                    orderIdData.put("alert", true + "");
                                    statusCountOrderIdMap0.get(status).add(orderIdData);
                                    statusCountOrderIdMap70.get(status).add(copyMap(orderIdData));
                                }
                                statusCountOrderIdMap0.get(status).add(copyMap(orderIdData));
                            }
                            else if (deliveryDate.getTime() == todayDate.getTime())
                            {
                                int count = Integer.parseInt(statusCountMap1.get(status).get("count"));
                                count++;
                                statusCountMap1.get(status).put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode))
                                {
                                    orderTotalActionRequired++;
                                    newOrderTotalActionRequired++;
                                    int noBreachCount = Integer.parseInt(statusCountMap4.get(status).get("count"));
                                    noBreachCount++;
                                    statusCountMap4.get(status).put("count", noBreachCount + "");
                                    statusCountMap4.get(status).put("sla", true + "");
                                    statusCountMap1.get(status).put("sla", true + "");

                                    orderIdData.put("sla", true + "");
                                    orderIdData.put("alert", false + "");
                                    statusCountOrderIdMap1.get(status).add(orderIdData);
                                    statusCountOrderIdMap4.get(status).add(copyMap(orderIdData));
                                }
                                else if (OrderUtil.isHighAlertActionRequired(slaCode))
                                {
                                    orderTotalHighAlert++;
                                    newOrderTotalHighAlert++;
                                    // put in high alert action map
                                    int alertCount = Integer.parseInt(statusCountMap7.get(status).get("count"));
                                    alertCount++;
                                    statusCountMap7.get(status).put("count", alertCount + "");
                                    statusCountMap7.get(status).put("alert", true + "");
                                    statusCountMap1.get(status).put("alert", true + "");

                                    orderIdData.put("sla", false + "");
                                    orderIdData.put("alert", true + "");
                                    statusCountOrderIdMap1.get(status).add(orderIdData);
                                    statusCountOrderIdMap7.get(status).add(copyMap(orderIdData));
                                }
                                statusCountOrderIdMap1.get(status).add(copyMap(orderIdData));
                            }
                            else if (deliveryDate.getTime() == tomorrowDate.getTime())
                            {
                                int count = Integer.parseInt(statusCountMap2.get(status).get("count"));
                                count++;
                                statusCountMap2.get(status).put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode))
                                {
                                    orderTotalActionRequired++;
                                    newOrderTotalActionRequired++;
                                    int noBreachCount = Integer.parseInt(statusCountMap5.get(status).get("count"));
                                    noBreachCount++;
                                    statusCountMap5.get(status).put("count", noBreachCount + "");
                                    statusCountMap5.get(status).put("sla", true + "");
                                    statusCountMap2.get(status).put("sla", true + "");

                                    orderIdData.put("sla", true + "");
                                    orderIdData.put("alert", false + "");
                                    statusCountOrderIdMap2.get(status).add(orderIdData);
                                    statusCountOrderIdMap5.get(status).add(copyMap(orderIdData));
                                }
                                else if (OrderUtil.isHighAlertActionRequired(slaCode))
                                {
                                    orderTotalHighAlert++;
                                    newOrderTotalHighAlert++;
                                    // put in high alert action map
                                    int alertCount = Integer.parseInt(statusCountMap8.get(status).get("count"));
                                    alertCount++;
                                    statusCountMap8.get(status).put("count", alertCount + "");
                                    statusCountMap8.get(status).put("alert", true + "");
                                    statusCountMap2.get(status).put("alert", true + "");

                                    orderIdData.put("sla", false + "");
                                    orderIdData.put("alert", true + "");
                                    statusCountOrderIdMap2.get(status).add(orderIdData);
                                    statusCountOrderIdMap8.get(status).add(copyMap(orderIdData));
                                }
                                statusCountOrderIdMap2.get(status).add(copyMap(orderIdData));
                            }
                            else if (deliveryDate.getTime() >= futureDate.getTime())
                            {
                                int count = Integer.parseInt(statusCountMap3.get(status).get("count"));
                                count++;
                                statusCountMap3.get(status).put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode))
                                {
                                    orderTotalActionRequired++;
                                    newOrderTotalActionRequired++;
                                    int noBreachCount = Integer.parseInt(statusCountMap6.get(status).get("count"));
                                    noBreachCount++;
                                    statusCountMap6.get(status).put("count", noBreachCount + "");
                                    statusCountMap6.get(status).put("sla", true + "");
                                    statusCountMap3.get(status).put("sla", true + "");

                                    orderIdData.put("sla", true + "");
                                    orderIdData.put("alert", false + "");
                                    statusCountOrderIdMap3.get(status).add(orderIdData);
                                    statusCountOrderIdMap6.get(status).add(copyMap(orderIdData));
                                }
                                else if (OrderUtil.isHighAlertActionRequired(slaCode))
                                {
                                    orderTotalHighAlert++;
                                    newOrderTotalHighAlert++;
                                    // put in high alert action map
                                    int alertCount = Integer.parseInt(statusCountMap9.get(status).get("count"));
                                    alertCount++;
                                    statusCountMap9.get(status).put("count", alertCount + "");
                                    statusCountMap9.get(status).put("alert", true + "");
                                    statusCountMap3.get(status).put("alert", true + "");

                                    orderIdData.put("sla", false + "");
                                    orderIdData.put("alert", true + "");
                                    statusCountOrderIdMap3.get(status).add(orderIdData);
                                    statusCountOrderIdMap9.get(status).add(copyMap(orderIdData));
                                }
                                statusCountOrderIdMap3.get(status).add(copyMap(orderIdData));
                            }
                        }else {
                            if (deliveryDate.getTime() < todayDate.getTime()){
                                fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("past").get(status),orderId,orderProductId);
                            }else if (deliveryDate.getTime() == todayDate.getTime()){
                                fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("today").get(status),orderId,orderProductId);
                            }else if (deliveryDate.getTime() == tomorrowDate.getTime()){
                                fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("tomorrow").get(status),orderId,orderProductId);
                            }else if (deliveryDate.getTime() >= futureDate.getTime()){
                                fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("future").get(status),orderId,orderProductId);
                            }
                        }
                    }
                    else if (status.equals("Confirmed") && deliverystatus == false)
                    {
                        orderIdData.put("orderId", orderId + "");
                        orderIdData.put("orderProductId", orderProductId + "");
                        orderIdData.put("sla", false + "");
                        orderIdData.put("alert", false + "");

                        String key=orderId + "," + deliveryDate + "," + shippingType;
                        flagForUniqueness=checkUniqueUnit(orderId,deliveryDate,shippingType,deliveryTime,uniqueUnitsMap,Integer.parseInt(fkAssociateId),status);
                        logger.debug("Key : status" + status + "-" + key);
                        if (flagForUniqueness)
                        {
                            orderTotalWhole++;
                            confirmOrderTotalWhole++;
                            if (deliveryDate.getTime() < todayDate.getTime())
                            {
                                int count = Integer.parseInt(statusCountMap0.get(status).get("count"));
                                count++;
                                statusCountMap0.get(status).put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode))
                                {
                                    orderTotalActionRequired++;
                                    confirmOrderTotalWholeActionRequired++;
                                    int noBreachCount = Integer.parseInt(statusCountMap40.get(status).get("count"));
                                    noBreachCount++;
                                    statusCountMap40.get(status).put("count", noBreachCount + "");
                                    statusCountMap40.get(status).put("sla", true + "");
                                    statusCountMap0.get(status).put("sla", true + "");

                                    orderIdData.put("sla", true + "");
                                    orderIdData.put("alert", false + "");
                                    statusCountOrderIdMap0.get(status).add(orderIdData);
                                    statusCountOrderIdMap40.get(status).add(copyMap(orderIdData));
                                }
                                else if (OrderUtil.isHighAlertActionRequired(slaCode))
                                {
                                    orderTotalHighAlert++;
                                    confirmOrderTotalWholeHighAlert++;
                                    int alertCount = Integer.parseInt(statusCountMap70.get(status).get("count"));
                                    alertCount++;
                                    statusCountMap70.get(status).put("count", alertCount + "");
                                    statusCountMap70.get(status).put("alert", true + "");
                                    statusCountMap0.get(status).put("alert", true + "");

                                    orderIdData.put("sla", false + "");
                                    orderIdData.put("alert", true + "");
                                    statusCountOrderIdMap0.get(status).add(orderIdData);
                                    statusCountOrderIdMap70.get(status).add(copyMap(orderIdData));
                                }
                                statusCountOrderIdMap0.get(status).add(copyMap(orderIdData));

                            }
                            else if (deliveryDate.getTime() == todayDate.getTime())
                            {
                                int count = Integer.parseInt(statusCountMap1.get(status).get("count"));
                                count++;
                                statusCountMap1.get(status).put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode))
                                {
                                    orderTotalActionRequired++;
                                    confirmOrderTotalWholeActionRequired++;
                                    int noBreachCount = Integer.parseInt(statusCountMap4.get(status).get("count"));
                                    noBreachCount++;
                                    statusCountMap4.get(status).put("count", noBreachCount + "");
                                    statusCountMap4.get(status).put("sla", true + "");
                                    statusCountMap1.get(status).put("sla", true + "");

                                    orderIdData.put("sla", true + "");
                                    orderIdData.put("alert", false + "");
                                    statusCountOrderIdMap1.get(status).add(orderIdData);
                                    statusCountOrderIdMap4.get(status).add(copyMap(orderIdData));
                                }
                                else if (OrderUtil.isHighAlertActionRequired(slaCode))
                                {
                                    orderTotalHighAlert++;
                                    confirmOrderTotalWholeHighAlert++;
                                    // put in high alert action map
                                    int alertCount = Integer.parseInt(statusCountMap7.get(status).get("count"));
                                    alertCount++;
                                    statusCountMap7.get(status).put("count", alertCount + "");
                                    statusCountMap7.get(status).put("alert", true + "");
                                    statusCountMap1.get(status).put("alert", true + "");

                                    orderIdData.put("sla", false + "");
                                    orderIdData.put("alert", true + "");
                                    statusCountOrderIdMap1.get(status).add(orderIdData);
                                    statusCountOrderIdMap7.get(status).add(copyMap(orderIdData));
                                }
                                statusCountOrderIdMap1.get(status).add(copyMap(orderIdData));
                            }
                            else if (deliveryDate.getTime() == tomorrowDate.getTime())
                            {
                                int count = Integer.parseInt(statusCountMap2.get(status).get("count"));
                                count++;
                                statusCountMap2.get(status).put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode))
                                {
                                    orderTotalActionRequired++;
                                    confirmOrderTotalWholeActionRequired++;
                                    int noBreachCount = Integer.parseInt(statusCountMap5.get(status).get("count"));
                                    noBreachCount++;
                                    statusCountMap5.get(status).put("count", noBreachCount + "");
                                    statusCountMap5.get(status).put("sla", true + "");
                                    statusCountMap2.get(status).put("sla", true + "");

                                    orderIdData.put("sla", true + "");
                                    orderIdData.put("alert", false + "");
                                    statusCountOrderIdMap2.get(status).add(orderIdData);
                                    statusCountOrderIdMap5.get(status).add(copyMap(orderIdData));
                                }
                                else if (OrderUtil.isHighAlertActionRequired(slaCode))
                                {
                                    orderTotalHighAlert++;
                                    confirmOrderTotalWholeHighAlert++;
                                    // put in high alert action map
                                    int alertCount = Integer.parseInt(statusCountMap8.get(status).get("count"));
                                    alertCount++;
                                    statusCountMap8.get(status).put("count", alertCount + "");
                                    statusCountMap8.get(status).put("alert", true + "");
                                    statusCountMap2.get(status).put("alert", true + "");

                                    orderIdData.put("sla", false + "");
                                    orderIdData.put("alert", true + "");
                                    statusCountOrderIdMap2.get(status).add(orderIdData);
                                    statusCountOrderIdMap8.get(status).add(copyMap(orderIdData));
                                }
                                statusCountOrderIdMap2.get(status).add(copyMap(orderIdData));
                            }
                            else if (deliveryDate.getTime() >= futureDate.getTime())
                            {
                                int count = Integer.parseInt(statusCountMap3.get(status).get("count"));
                                count++;
                                statusCountMap3.get(status).put("count", count + "");
                                if (OrderUtil.isSLASatisfied(slaCode))
                                {
                                    orderTotalActionRequired++;
                                    confirmOrderTotalWholeActionRequired++;
                                    int noBreachCount = Integer.parseInt(statusCountMap6.get(status).get("count"));
                                    noBreachCount++;
                                    statusCountMap6.get(status).put("count", noBreachCount + "");
                                    statusCountMap6.get(status).put("sla", true + "");
                                    statusCountMap3.get(status).put("sla", true + "");

                                    orderIdData.put("sla", true + "");
                                    orderIdData.put("alert", false + "");
                                    statusCountOrderIdMap3.get(status).add(orderIdData);
                                    statusCountOrderIdMap6.get(status).add(copyMap(orderIdData));
                                }
                                else if (OrderUtil.isHighAlertActionRequired(slaCode))
                                {
                                    orderTotalHighAlert++;
                                    confirmOrderTotalWholeHighAlert++;
                                    // put in high alert action map
                                    int alertCount = Integer.parseInt(statusCountMap9.get(status).get("count"));
                                    alertCount++;
                                    statusCountMap9.get(status).put("count", alertCount + "");
                                    statusCountMap9.get(status).put("alert", true + "");
                                    statusCountMap3.get(status).put("alert", true + "");

                                    orderIdData.put("sla", false + "");
                                    orderIdData.put("alert", true + "");
                                    statusCountOrderIdMap3.get(status).add(orderIdData);
                                    statusCountOrderIdMap9.get(status).add(copyMap(orderIdData));
                                }
                                statusCountOrderIdMap3.get(status).add(copyMap(orderIdData));
                            }
                        }else {
                            if (deliveryDate.getTime() < todayDate.getTime()){
                                fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("past").get(status),orderId,orderProductId);
                            }else if (deliveryDate.getTime() == todayDate.getTime()){
                                fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("today").get(status),orderId,orderProductId);
                            }else if (deliveryDate.getTime() == tomorrowDate.getTime()){
                                fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("tomorrow").get(status),orderId,orderProductId);
                            }else if (deliveryDate.getTime() >= futureDate.getTime()){
                                fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("future").get(status),orderId,orderProductId);
                            }
                        }
                    }
                    else if (status.equals("Shipped") && deliverystatus == true
                        && deliveredDate.getTime() == todayDate.getTime())
                    {
                        String key=orderId + "," + deliveryDate + "," + shippingType;
                        flagForUniqueness=checkUniqueUnit(orderId,deliveryDate,shippingType,deliveryTime,uniqueUnitsMap,
                                                            Integer.parseInt(fkAssociateId),status);
                        if (flagForUniqueness )
                        {
//                            orderTotalWhole++;
                            if(deliveryAttemptFlag == 0 || deliveryAttemptFlag==3){ // 0 means regular flow product delivered
                                vendorCountDetail                                   // 3 means vendor reattempt delivery and finally did it
                                    .setDeliveredTodayOrderCount(vendorCountDetail.getDeliveredTodayOrderCount() + 1);
                            }else if(deliveryAttemptFlag==1) {
                                //raised issue for delivery attempt
                                vendorCountDetail.setDeliveryAttemptRaiseRequestCount(vendorCountDetail.getDeliveryAttemptApproveCount()+1);
                            }else if(deliveryAttemptFlag==2){
                                //approve by handles team to go ahead for re-delivery
                                vendorCountDetail.setDeliveryAttemptApproveCount(vendorCountDetail.getDeliveryAttemptApproveCount()+1);
                            }

                        }
                    }
                    else if (status.equals("Shipped") && deliverystatus == false
                        && outForDeliveryDate.getTime() <= todayDate.getTime())
                    {
                        orderTotalWhole++;
                        outOfDeliveryOrderTotalWhole++;
                        Map<String, String> data = new HashMap<>();
                        data.put("orderId", orderId + "");
                        if (OrderUtil.isSLASatisfied(slaCode))
                        {
                            orderTotalActionRequired++;
                            outOfDeliveryOrderTotalActionRequired++;
                            data.put("sla", true + "");
                            data.put("alert", false + "");
                        }
                        else if (OrderUtil.isHighAlertActionRequired(slaCode))
                        {
                            orderTotalHighAlert++;
                            outOfDeliveryOrderTotalHighAlert++;
                            data.put("sla", false + "");
                            data.put("alert", true + "");
                        }
                        outForDeliveryOrderIds.add(data);
                        if (data.get("sla").equals("true"))
                        {
                            slaOutForDeliveryOrderIds.add(data);
                        }
                        if (data.get("alert").equals("true"))
                        {
                            alertOutForDeliveryOrderIds.add(data);
                        }
                    }
                }
                catch (Exception exception)
                {
                    logger.error("Error in preparing VendorCountDetail", exception);
                }
            }
            vendorCountDetail.setOrderTotalWhole(vendorCountDetail.getOrderTotalWhole()+orderTotalWhole);
            vendorCountDetail.setNewOrderTotalWhole(vendorCountDetail.getNewOrderTotalWhole()+newOrderTotalWhole);
            vendorCountDetail.setConfirmOrderTotalWhole(vendorCountDetail.getConfirmOrderTotalWhole()+confirmOrderTotalWhole);
            vendorCountDetail.setOutOfDeliveryOrderTotalWhole(vendorCountDetail.getOutOfDeliveryOrderTotalWhole()+outOfDeliveryOrderTotalWhole);
            vendorCountDetail.setOrderTotalActionRequired(vendorCountDetail.getOrderTotalActionRequired()+orderTotalActionRequired);
            vendorCountDetail.setNewOrderTotalActionRequired(vendorCountDetail.getNewOrderTotalActionRequired()+newOrderTotalActionRequired);
            vendorCountDetail.setConfirmOrderTotalWholeActionRequired(vendorCountDetail.getConfirmOrderTotalWholeActionRequired()+confirmOrderTotalWholeActionRequired);
            vendorCountDetail.setOutOfDeliveryOrderTotalActionRequired(vendorCountDetail.getOutOfDeliveryOrderTotalHighAlert()+outOfDeliveryOrderTotalActionRequired);
            vendorCountDetail.setOrderTotalHighAlert(vendorCountDetail.getOrderTotalHighAlert()+orderTotalHighAlert);
            vendorCountDetail.setNewOrderTotalHighAlert(vendorCountDetail.getNewOrderTotalHighAlert()+newOrderTotalHighAlert);
            vendorCountDetail.setConfirmOrderTotalWholeHighAlert(vendorCountDetail.getConfirmOrderTotalWholeHighAlert()+confirmOrderTotalWholeHighAlert);
            vendorCountDetail.setOutOfDeliveryOrderTotalHighAlert(vendorCountDetail.getOutOfDeliveryOrderTotalHighAlert()+outOfDeliveryOrderTotalHighAlert);
            if(leftOutOrderToOrderProductIdMap2!=null){
                clubOrderProductIds(dateStatusOrderIdAllMap,leftOutOrderToOrderProductIdMap2);
                clubOrderProductIds(dateStatusOrderIdNoBreachMap,leftOutOrderToOrderProductIdMap2);
                clubOrderProductIds(dateStatusOrderIdAlertMap,leftOutOrderToOrderProductIdMap2);
            }
        } catch (Exception exception)
        {
            logger.error("Error in preparing VendorCountDetail", exception);
        }
        return vendorCountDetail;
    }

    public Map<String, Map<String, String>> getVendorCountDetailForDate(String scopeId, String fkAssociateId,
        VendorCountDetail vendorCountDetail){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("count", "" + 0);
        dataMap.put("sla", false + "");
        dataMap.put("alert", false + "");

        Map<String,Set<String>> uniqueUnitsMap=new HashMap<>();

        Map<String, Map<String, String>> statusCountMap = new HashMap<>();
        statusCountMap.put("Processed", copyMap(dataMap));
        statusCountMap.put("Confirmed", copyMap(dataMap));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date deliveredDate = null;
        Date deliveryDate = null;
        VendorUtil handleVendorUtil=new VendorUtil();
        OrderUtil orderUtil=new OrderUtil();
        SlaCompliant slaCompliant=new SlaCompliant();
        try
        {
            Date specificDate = dateFormat.parse(vendorCountDetail.getFestivalDate());


            Map<String, Map<String, Set<Map<String, String>>>> dateStatusOrderIdAllMap      = vendorCountDetail.getDateStatusOrderIdAllMap();
            Map<String, Map<String, Set<Map<String, String>>>> dateStatusOrderIdNoBreachMap = vendorCountDetail.getDateStatusOrderIdNoBreachMap();
            Map<String, Map<String, Set<Map<String, String>>>> dateStatusOrderIdAlertMap    = vendorCountDetail.getDateStatusOrderIdAlertMap();


            Map<String,  Set<Map<String, String>>> statusCountOrderIdMap1 = new HashMap<>();
            statusCountOrderIdMap1.put("Processed",new HashSet<>());
            statusCountOrderIdMap1.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAllMap.put("festivalDate",statusCountOrderIdMap1);

            Map<String,  Set<Map<String, String>>> statusCountOrderIdMap2 = new HashMap<>();
            statusCountOrderIdMap2.put("Processed",new HashSet<>());
            statusCountOrderIdMap2.put("Confirmed",new HashSet<>());
            dateStatusOrderIdNoBreachMap.put("festivalDate",statusCountOrderIdMap2);

            Map<String,  Set<Map<String, String>>> statusCountOrderIdMap3 = new HashMap<>();
            statusCountOrderIdMap3.put("Processed",new HashSet<>());
            statusCountOrderIdMap3.put("Confirmed",new HashSet<>());
            dateStatusOrderIdAlertMap.put("festivalDate",statusCountOrderIdMap3);


            Map<String, Map<String, Map<String, String>>> dateStatusCountAllMap = vendorCountDetail
                .getDateStatusCountAllMap();

            Map<String, Map<String, String>> statusCountMap1 = new HashMap<>();
            statusCountMap1.put("Processed", copyMap(dataMap));
            statusCountMap1.put("Confirmed", copyMap(dataMap));
            dateStatusCountAllMap.put("festivalDate", statusCountMap1);

            Map<String, Map<String, Map<String, String>>> noBreachStatusCountAllMap = vendorCountDetail
                .getDateStatusCountNoBreachMap();

            Map<String, Map<String, String>> statusCountMap2 = new HashMap<>();
            statusCountMap2.put("Processed", copyMap(dataMap));
            statusCountMap2.put("Confirmed", copyMap(dataMap));
            noBreachStatusCountAllMap.put("festivalDate", statusCountMap2);

            Map<String, Map<String, Map<String, String>>> alertStatusCountAllMap = vendorCountDetail
                .getDateStatusCountAlertMap();

            Map<String, Map<String, String>> statusCountMap3 = new HashMap<>();
            statusCountMap3.put("Processed", copyMap(dataMap));
            statusCountMap3.put("Confirmed", copyMap(dataMap));
            alertStatusCountAllMap.put("festivalDate", statusCountMap3);

            Map<String,Map<String,Map<String,String>>> leftOutOrderToOrderProductIdMap2=new HashMap<>();
            Map<String, Map<String, String>> leftOutOrderIdMap1 = new HashMap<>();
            leftOutOrderIdMap1.put("Processed", new HashMap<>());
            leftOutOrderIdMap1.put("Confirmed", new HashMap<>());
            leftOutOrderToOrderProductIdMap2.put("festivalDate",leftOutOrderIdMap1);

            Set<String> uniqueCombinations = new HashSet<>();
            specificDate = DateUtils.truncate(specificDate, Calendar.DAY_OF_MONTH);

            List<OrderDetailsPerOrderProduct> listOfOrderIdAsPerVendor=handleVendorUtil.getOrderListForVendor( fkAssociateId,  specificDate,1);

            // orders_id | deliveredDate | deliveryDate | orders_product_status
            // |
            // delivery_status | delivery_time | shipping_type
            // | sla code
            for (OrderDetailsPerOrderProduct orderDetailsPerOrderProduct : listOfOrderIdAsPerVendor)
            {
                Long orderId = orderDetailsPerOrderProduct.getOrdersId();
                Long orderProductId= orderDetailsPerOrderProduct.getOrdersProductsId();
                if (orderDetailsPerOrderProduct.getDeliveredDate() != null)
                    deliveredDate = orderDetailsPerOrderProduct.getDeliveredDate();
                if (orderDetailsPerOrderProduct.getDeliveryDate() != null)
                    deliveryDate = orderDetailsPerOrderProduct.getDeliveryDate();
                String status = orderDetailsPerOrderProduct.getOrderProductStatus();
                String deliveryTime = orderDetailsPerOrderProduct.getDeliveryTime();
                String shippingType = Constants.getDeliveryType(orderDetailsPerOrderProduct.getShippingType());
                boolean flagForUniqueness=false;

                boolean deliverystatus = (boolean) orderDetailsPerOrderProduct.getDeliveryStatus();
                //                int slaCode = orderDetailsPerOrderProduct.getSlaCode();
                int slaCode=slaCompliant.generateSlacodeForAll(orderDetailsPerOrderProduct,0);
                orderUtil.saveSlaCodes(orderId.intValue(),orderProductId.intValue(),status,slaCode,deliverystatus);
                Map<String, String> orderIdData = new HashMap<>();
                // boolean deliverystatus = (boolean) orderArray[4];
                orderIdData.put("orderId", orderId + "");
                orderIdData.put("orderProductId", orderProductId + "");
                orderIdData.put("sla", false + "");
                orderIdData.put("alert", false + "");
                if (status.equals("Processed") || status.equals("Confirmed"))
                {
                    String key = orderId + "," + deliveryDate + "," + shippingType + "," + deliveryTime + "," + status;

                    flagForUniqueness=checkUniqueUnit(orderId,deliveryDate,shippingType,deliveryTime,uniqueUnitsMap,
                                                    Integer.parseInt(fkAssociateId),status);

                    if (flagForUniqueness)
                    {
                        if (deliveryDate.getTime() == specificDate.getTime())
                        {
                            int count = Integer.parseInt(statusCountMap1.get(status).get("count"));
                            count++;
                            statusCountMap1.get(status).put("count", count + "");
                            if (OrderUtil.isSLASatisfied(slaCode))
                            {
                                int noBreachCount = Integer.parseInt(statusCountMap2.get(status).get("count"));
                                noBreachCount++;
                                statusCountMap2.get(status).put("count", noBreachCount + "");
                                statusCountMap2.get(status).put("sla", true + "");
                                statusCountMap1.get(status).put("sla", true + "");

                                orderIdData.put("sla", true + "");
                                orderIdData.put("alert", false + "");
                                statusCountOrderIdMap1.get(status).add(orderIdData);
                                statusCountOrderIdMap2.get(status).add(copyMap(orderIdData));
                            }
                            else if (OrderUtil.isHighAlertActionRequired(slaCode))
                            {
                                // put in high alert action map
                                int alertCount = Integer.parseInt(statusCountMap3.get(status).get("count"));
                                alertCount++;
                                statusCountMap3.get(status).put("count", alertCount + "");
                                statusCountMap3.get(status).put("alert", true + "");
                                statusCountMap1.get(status).put("alert", true + "");

                                orderIdData.put("sla", false + "");
                                orderIdData.put("alert", true + "");
                                statusCountOrderIdMap1.get(status).add(orderIdData);
                                statusCountOrderIdMap3.get(status).add(copyMap(orderIdData));
                            }
                        }
                    }
                    else {
                        if (deliveryDate.getTime() == specificDate.getTime()){
                            fillLeftOutOrderIdMap(leftOutOrderToOrderProductIdMap2.get("festivalDate").get(status),orderId,orderProductId);
                        }
                    }
                }
            }
            if(leftOutOrderToOrderProductIdMap2!=null){
                clubOrderProductIds(dateStatusOrderIdAllMap,leftOutOrderToOrderProductIdMap2);
                clubOrderProductIds(dateStatusOrderIdNoBreachMap,leftOutOrderToOrderProductIdMap2);
                clubOrderProductIds(dateStatusOrderIdAlertMap,leftOutOrderToOrderProductIdMap2);
            }
        }
        catch (Exception exception)
        {
            logger.error("Error in preparing VendorCountDetail", exception);
        }


        return statusCountMap;
    }


    public boolean checkUniqueUnit(Long orderId,Date deliveryDate,String shippingType,String deliveryTime,
                            Map<String,Set<String>> uniqueUnitsMap,int vendorId,String status){
        boolean flagForUniqueness=false;


        String key=orderId + "," + deliveryDate + "," + shippingType+ "," + vendorId+ "," + status;

        if(shippingType.equalsIgnoreCase("Any time") ||  shippingType.equalsIgnoreCase("Same Day")){
            if(uniqueUnitsMap.containsKey(key)==false){  // simple key not exist so we have to check if that unit can be clubbed or not
                String key1=orderId + "," + deliveryDate + ",Fix Time"+ "," + vendorId+ "," + status;
                //String key2=orderId + "," + deliveryDate + ",Midnight"+ "," + vendorId+ "," + status;
                if(uniqueUnitsMap.containsKey(key1) ){// || uniqueUnitsMap.containsKey(key2) ){
                    flagForUniqueness=false;
                }
                else {
                    flagForUniqueness=true;
                    Set<String> deliveryTimeSet=new HashSet<>();
                    deliveryTimeSet.add(deliveryTime);
                    uniqueUnitsMap.put(key,deliveryTimeSet);
                }
            }
            else{ // we already have oID,Ddate,AnyTime
                flagForUniqueness=false;
            }
        }
        else if(shippingType.equalsIgnoreCase("Fix Time")) {
            String key1=orderId + "," + deliveryDate + ",Any time"+ "," + vendorId+ "," + status;
            String key2=orderId + "," + deliveryDate + ",Same Day"+ "," + vendorId+ "," + status;

            if(uniqueUnitsMap.containsKey(key)==false){

                if(uniqueUnitsMap.containsKey(key1)|| uniqueUnitsMap.containsKey(key2) ){
                    flagForUniqueness=false;
                }
                else {
                    flagForUniqueness=true;
                    Set<String> deliveryTimeSet=new HashSet<>();
                    deliveryTimeSet.add(deliveryTime);
                    uniqueUnitsMap.put(key,deliveryTimeSet);
                }
            }
            else {
                if(uniqueUnitsMap.containsKey(key)==true){
                    if(uniqueUnitsMap.get(key).add(deliveryTime)){
                        flagForUniqueness=true;
                    }
                    else {
                        flagForUniqueness=false;
                    }
                }
            }
        }else if(shippingType.equalsIgnoreCase("Midnight")){
            if(uniqueUnitsMap.containsKey(key)==false){
                flagForUniqueness=true;
                Set<String> deliveryTimeSet=new HashSet<>();
                deliveryTimeSet.add(deliveryTime);
                uniqueUnitsMap.put(key,deliveryTimeSet);
            }else {
                flagForUniqueness=false;
            }
        }
        if(flagForUniqueness==false){
            cnt++;
        }
        return flagForUniqueness;
    }
    public Map<String, String> copyMap(Map<String, String> map)
    {
        Map<String, String> copyMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            copyMap.put(entry.getKey(), entry.getValue());
        }
        return copyMap;
    }
    public Map<String, Set<Map<String, String>>> copyMapOfSet(Map<String, Set<Map<String, String>>> map)
    {
        Map<String, Set<Map<String, String>>> copyMap = new HashMap<>();
        for (Map.Entry<String, Set<Map<String, String>>> entry : map.entrySet())
        {
            copyMap.put(entry.getKey(), entry.getValue());
        }
        return copyMap;
    }
    private void clubOrderProductIds(Map<String, Map<String, Set<Map<String, String>>>> parentMap,Map<String, Map<String,Map<String, String>>> childMap){
        for(Map.Entry<String, Map<String, Set<Map<String, String>>>> entry : parentMap.entrySet()){
           // entry.getKey() == past , future , today , tomorrow
            String timeWhen=entry.getKey();
            for (Map.Entry<String, Set<Map<String, String>>> entry1:entry.getValue().entrySet()){
                // entry1.getKey() == confirmed , processed
                String status=entry1.getKey();
                for (Map<String,String> map:entry1.getValue()){
                    String orderId=map.get("orderId");
                    Map<String,Map<String, String>> statusWithOrderIdMap=childMap.get(timeWhen);
                    if(statusWithOrderIdMap!=null){
                        Map<String, String> orderIdMap= statusWithOrderIdMap.get(status);
                        if(orderIdMap!=null ){
                            if(orderIdMap.containsKey(orderId)){
                                String orderProductIds=orderIdMap.get(orderId);
                                if(orderProductIds!=null){
                                    orderProductIds=orderProductIds+","+map.get("orderProductId");
                                    map.put("orderProductId",orderProductIds);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private void fillLeftOutOrderIdMap(Map<String,String> leftOutOrderToOrderProductIdMap,Long orderId,Long orderProductId){
        if(leftOutOrderToOrderProductIdMap.containsKey(orderId.toString())){
            String newOdProdId=leftOutOrderToOrderProductIdMap.get(orderId.toString());
            newOdProdId=newOdProdId+","+orderProductId.toString();
            leftOutOrderToOrderProductIdMap.put(orderId.toString(),newOdProdId);
        }else {
            leftOutOrderToOrderProductIdMap.put(orderId.toString(),orderProductId.toString());
        }
    }
}
