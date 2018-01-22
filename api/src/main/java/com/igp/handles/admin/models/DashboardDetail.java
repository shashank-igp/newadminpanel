package com.igp.handles.admin.models;

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
    private Date                     festivalDate               = null;

    private int orderTotalWhole=0;
    private int newOrderTotalWhole=0;
    private int confirmOrderTotalWhole=0;
    private int outOfDeliveryOrderTotalWhole=0;
    private int orderTotalActionRequired=0;
    private int newOrderTotalActionRequired=0;
    private int confirmOrderTotalWholeActionRequired=0;
    private int outOfDeliveryOrderTotalActionRequired=0;
    private int orderTotalHighAlert=0;
    private int newOrderTotalHighAlert=0;
    private int confirmOrderTotalWholeHighAlert=0;
    private int outOfDeliveryOrderTotalHighAlert=0;

    private Map<String,String> notShippedTotalOrderCount=new HashMap<>();
    private Map<String,String> notShippedPendingOrderCount=new HashMap<>();
    private Map<String,String> notDeliveredTotalOrderCount=new HashMap<>();
    private Map<String,String> notDeliveredPendingOrderCount=new HashMap<>();



    //for specific to mobile view

    private Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdAllMap      = new HashMap<>();
    private Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdNoBreachMap = new HashMap<>();
    private Map<String, Map<String,Map<String, Set<Map<String, String>>>>> dateStatusOrderIdAlertMap    = new HashMap<>();
    private Map<String,Map<String,Map<String,String>>> leftOutOrderToOrderProductIdMap2=new HashMap<>();

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

    public int getNewOrderTotalWhole()
    {
        return newOrderTotalWhole;
    }

    public void setNewOrderTotalWhole(int newOrderTotalWhole)
    {
        this.newOrderTotalWhole = newOrderTotalWhole;
    }

    public int getConfirmOrderTotalWhole()
    {
        return confirmOrderTotalWhole;
    }

    public void setConfirmOrderTotalWhole(int confirmOrderTotalWhole)
    {
        this.confirmOrderTotalWhole = confirmOrderTotalWhole;
    }

    public int getOutOfDeliveryOrderTotalWhole()
    {
        return outOfDeliveryOrderTotalWhole;
    }

    public void setOutOfDeliveryOrderTotalWhole(int outOfDeliveryOrderTotalWhole)
    {
        this.outOfDeliveryOrderTotalWhole = outOfDeliveryOrderTotalWhole;
    }

    public int getOrderTotalActionRequired()
    {
        return orderTotalActionRequired;
    }

    public void setOrderTotalActionRequired(int orderTotalActionRequired)
    {
        this.orderTotalActionRequired = orderTotalActionRequired;
    }

    public int getNewOrderTotalActionRequired()
    {
        return newOrderTotalActionRequired;
    }

    public void setNewOrderTotalActionRequired(int newOrderTotalActionRequired)
    {
        this.newOrderTotalActionRequired = newOrderTotalActionRequired;
    }

    public int getConfirmOrderTotalWholeActionRequired()
    {
        return confirmOrderTotalWholeActionRequired;
    }

    public void setConfirmOrderTotalWholeActionRequired(int confirmOrderTotalWholeActionRequired)
    {
        this.confirmOrderTotalWholeActionRequired = confirmOrderTotalWholeActionRequired;
    }

    public int getOutOfDeliveryOrderTotalActionRequired()
    {
        return outOfDeliveryOrderTotalActionRequired;
    }

    public void setOutOfDeliveryOrderTotalActionRequired(int outOfDeliveryOrderTotalActionRequired)
    {
        this.outOfDeliveryOrderTotalActionRequired = outOfDeliveryOrderTotalActionRequired;
    }

    public int getOrderTotalHighAlert()
    {
        return orderTotalHighAlert;
    }

    public void setOrderTotalHighAlert(int orderTotalHighAlert)
    {
        this.orderTotalHighAlert = orderTotalHighAlert;
    }

    public int getNewOrderTotalHighAlert()
    {
        return newOrderTotalHighAlert;
    }

    public void setNewOrderTotalHighAlert(int newOrderTotalHighAlert)
    {
        this.newOrderTotalHighAlert = newOrderTotalHighAlert;
    }

    public int getConfirmOrderTotalWholeHighAlert()
    {
        return confirmOrderTotalWholeHighAlert;
    }

    public void setConfirmOrderTotalWholeHighAlert(int confirmOrderTotalWholeHighAlert)
    {
        this.confirmOrderTotalWholeHighAlert = confirmOrderTotalWholeHighAlert;
    }

    public int getOutOfDeliveryOrderTotalHighAlert()
    {
        return outOfDeliveryOrderTotalHighAlert;
    }

    public void setOutOfDeliveryOrderTotalHighAlert(int outOfDeliveryOrderTotalHighAlert)
    {
        this.outOfDeliveryOrderTotalHighAlert = outOfDeliveryOrderTotalHighAlert;
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
    public Date getFestivalDate()
    {
        return festivalDate;
    }

    public void setFestivalDate(Date festivalDate)
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
