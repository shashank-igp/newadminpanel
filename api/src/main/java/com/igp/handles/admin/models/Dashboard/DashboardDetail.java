package com.igp.handles.admin.models.Dashboard;

import java.util.*;

/**
 * Created by shanky on 16/1/18.
 */
public class DashboardDetail {
    private Map<String, Map<String,Map<String, Map<String, String>>>> dateStatusCountAllMap      = new HashMap<>();
    private Map<String, Map<String,Map<String, Map<String, String>>>> dateStatusCountNoBreachMap = new HashMap<>();
    private Map<String, Map<String,Map<String, Map<String, String>>>> dateStatusCountAlertMap    = new HashMap<>();

    private Set<Map<String, String>> outOfDeliveryOrderIds      = new HashSet<>();
    private Set<Map<String, String>> slaOutOfDeliveryOrderIds   = new HashSet<>();
    private Set<Map<String, String>> alertOutOfDeliveryOrderIds = new HashSet<>();
    private int                      deliveredTodayOrderCount   = 0;
    private String                     festivalDate               = null;

    private int orderTotalWhole=0;
    private int notAssignedOrdersTotalWhole=0;
    private int notConfirmedOrdersTotalWhole=0;

    private Map<String,String> notShippedTotalOrderCount=new HashMap<>();
    private Map<String,String> notShippedPendingOrderCount=new HashMap<>();
    private Map<String,String> notDeliveredTotalOrderCount=new HashMap<>();
    private Map<String,String> notDeliveredPendingOrderCount=new HashMap<>();
    Map<String,String> attemptedDeliveryOrders=new HashMap<>();



    //for specific to mobile view

    private Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdAllMap      = new HashMap<>();
    private Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdNoBreachMap = new HashMap<>();
    private Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdAlertMap    = new HashMap<>();
    private Map<String,Map<String,Map<String,String>>> leftOutOrderToOrderProductIdMap2=new HashMap<>();

    public Map<String, String> getAttemptedDeliveryOrders()
    {
        return attemptedDeliveryOrders;
    }

    public void setAttemptedDeliveryOrders(Map<String, String> attemptedDeliveryOrders)
    {
        this.attemptedDeliveryOrders = attemptedDeliveryOrders;
    }

    public Map<String, Map<String, Map<String, String>>> getLeftOutOrderToOrderProductIdMap2()
    {
        return leftOutOrderToOrderProductIdMap2;
    }

    public void setLeftOutOrderToOrderProductIdMap2(
        Map<String, Map<String, Map<String, String>>> leftOutOrderToOrderProductIdMap2)
    {
        this.leftOutOrderToOrderProductIdMap2 = leftOutOrderToOrderProductIdMap2;
    }

    public Map<String, Map<String, Map<String, Set<Map<String, String>>>>> getDateStatusOrderIdAllMap()
    {
        return dateStatusOrderIdAllMap;
    }

    public void setDateStatusOrderIdAllMap(
        Map<String, Map<String, Map<String, Set<Map<String, String>>>>> dateStatusOrderIdAllMap)
    {
        this.dateStatusOrderIdAllMap = dateStatusOrderIdAllMap;
    }

    public Map<String, Map<String, Map<String, Set<Map<String, String>>>>> getDateStatusOrderIdNoBreachMap()
    {
        return dateStatusOrderIdNoBreachMap;
    }

    public Map<String, String> getNotShippedTotalOrderCount()
    {
        return notShippedTotalOrderCount;
    }

    public void setNotShippedTotalOrderCount(Map<String, String> notShippedTotalOrderCount)
    {
        this.notShippedTotalOrderCount = notShippedTotalOrderCount;
    }

    public Map<String, String> getNotShippedPendingOrderCount()
    {
        return notShippedPendingOrderCount;
    }

    public void setNotShippedPendingOrderCount(Map<String, String> notShippedPendingOrderCount)
    {
        this.notShippedPendingOrderCount = notShippedPendingOrderCount;
    }

    public Map<String, String> getNotDeliveredTotalOrderCount()
    {
        return notDeliveredTotalOrderCount;
    }

    public void setNotDeliveredTotalOrderCount(Map<String, String> notDeliveredTotalOrderCount)
    {
        this.notDeliveredTotalOrderCount = notDeliveredTotalOrderCount;
    }

    public Map<String, String> getNotDeliveredPendingOrderCount()
    {
        return notDeliveredPendingOrderCount;
    }

    public void setNotDeliveredPendingOrderCount(Map<String, String> notDeliveredPendingOrderCount)
    {
        this.notDeliveredPendingOrderCount = notDeliveredPendingOrderCount;
    }

    public void setDateStatusOrderIdNoBreachMap(
        Map<String, Map<String, Map<String, Set<Map<String, String>>>>> dateStatusOrderIdNoBreachMap)
    {
        this.dateStatusOrderIdNoBreachMap = dateStatusOrderIdNoBreachMap;
    }

    public Map<String, Map<String, Map<String, Set<Map<String, String>>>>> getDateStatusOrderIdAlertMap()
    {
        return dateStatusOrderIdAlertMap;
    }

    public void setDateStatusOrderIdAlertMap(
        Map<String, Map<String, Map<String, Set<Map<String, String>>>>> dateStatusOrderIdAlertMap)
    {
        this.dateStatusOrderIdAlertMap = dateStatusOrderIdAlertMap;
    }

    public int getOrderTotalWhole()
    {
        return orderTotalWhole;
    }

    public void setOrderTotalWhole(int orderTotalWhole)
    {
        this.orderTotalWhole = orderTotalWhole;
    }

    public int getNotAssignedOrdersTotalWhole()
    {
        return notAssignedOrdersTotalWhole;
    }

    public void setNotAssignedOrdersTotalWhole(int notAssignedOrdersTotalWhole)
    {
        this.notAssignedOrdersTotalWhole = notAssignedOrdersTotalWhole;
    }

    public int getNotConfirmedOrdersTotalWhole()
    {
        return notConfirmedOrdersTotalWhole;
    }

    public void setNotConfirmedOrdersTotalWhole(int notConfirmedOrdersTotalWhole)
    {
        this.notConfirmedOrdersTotalWhole = notConfirmedOrdersTotalWhole;
    }

    /**
     * @return the dateStatusCountAllMap
     */
    public Map<String, Map<String, Map<String, Map<String, String>>>> getDateStatusCountAllMap()
    {
        return dateStatusCountAllMap;
    }

    public void setDateStatusCountAllMap(
        Map<String, Map<String, Map<String, Map<String, String>>>> dateStatusCountAllMap)
    {
        this.dateStatusCountAllMap = dateStatusCountAllMap;
    }

    public Map<String, Map<String, Map<String, Map<String, String>>>> getDateStatusCountNoBreachMap()
    {
        return dateStatusCountNoBreachMap;
    }

    public void setDateStatusCountNoBreachMap(
        Map<String, Map<String, Map<String, Map<String, String>>>> dateStatusCountNoBreachMap)
    {
        this.dateStatusCountNoBreachMap = dateStatusCountNoBreachMap;
    }

    public Map<String, Map<String, Map<String, Map<String, String>>>> getDateStatusCountAlertMap()
    {
        return dateStatusCountAlertMap;
    }

    public void setDateStatusCountAlertMap(
        Map<String, Map<String, Map<String, Map<String, String>>>> dateStatusCountAlertMap)
    {
        this.dateStatusCountAlertMap = dateStatusCountAlertMap;
    }

    /**
     * @return the outOfDeliveryOrderIds
     */
    public Set<Map<String, String>> getOutOfDeliveryOrderIds()
    {
        return outOfDeliveryOrderIds;
    }

    /**
     * @param outOfDeliveryOrderIds
     *            the outOfDeliveryOrderIds to set
     */
    public void setOutOfDeliveryOrderIds(Set<Map<String, String>> outOfDeliveryOrderIds)
    {
        this.outOfDeliveryOrderIds = outOfDeliveryOrderIds;
    }

    /**
     * @return the deliveredTodayOrderCount
     */
    public int getDeliveredTodayOrderCount()
    {
        return deliveredTodayOrderCount;
    }

    /**
     * @param deliveredTodayOrderCount
     *            the deliveredTodayOrderCount to set
     */
    public void setDeliveredTodayOrderCount(int deliveredTodayOrderCount)
    {
        this.deliveredTodayOrderCount = deliveredTodayOrderCount;
    }

    /**
     * @return the festivalDate
     */
    public String getFestivalDate()
    {
        return festivalDate;
    }

    public void setFestivalDate(String festivalDate)
    {
        this.festivalDate = festivalDate;
    }

    /**
     * @return the slaOutOfDeliveryOrderIds
     */
    public Set<Map<String, String>> getSlaOutOfDeliveryOrderIds()
    {
        return slaOutOfDeliveryOrderIds;
    }

    /**
     * @param slaOutOfDeliveryOrderIds
     *            the slaOutOfDeliveryOrderIds to set
     */
    public void setSlaOutOfDeliveryOrderIds(Set<Map<String, String>> slaOutOfDeliveryOrderIds)
    {
        this.slaOutOfDeliveryOrderIds = slaOutOfDeliveryOrderIds;
    }

    /**
     * @return the alertOutOfDeliveryOrderIds
     */
    public Set<Map<String, String>> getAlertOutOfDeliveryOrderIds()
    {
        return alertOutOfDeliveryOrderIds;
    }

    /**
     * @param alertOutOfDeliveryOrderIds
     *            the alertOutOfDeliveryOrderIds to set
     */
    public void setAlertOutOfDeliveryOrderIds(Set<Map<String, String>> alertOutOfDeliveryOrderIds)
    {
        this.alertOutOfDeliveryOrderIds = alertOutOfDeliveryOrderIds;
    }
}
