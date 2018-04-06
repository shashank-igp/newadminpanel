package com.igp.handles.admin.models.Order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shanky on 5/4/18.
 */
public class BulkOrderAssignModel {

    @JsonProperty("fkAssociateId")
    private int vendorId;

    @JsonProperty("orderIdMap")
    private Map<Integer,String> orderIdToProductIdMap=new HashMap<>();

    public Map<Integer, String> getOrderIdToProductIdMap()
    {
        return orderIdToProductIdMap;
    }

    public void setOrderIdToProductIdMap(Map<Integer, String> orderIdToProductIdMap)
    {
        this.orderIdToProductIdMap = orderIdToProductIdMap;
    }

    public int getVendorId()
    {
        return vendorId;
    }

    public void setVendorId(int vendorId)
    {
        this.vendorId = vendorId;
    }
}
