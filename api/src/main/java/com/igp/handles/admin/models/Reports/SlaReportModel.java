package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanky on 11/4/18.
 */
public class SlaReportModel {
    @JsonProperty("Order_No")
    private String orderNo;

    @JsonProperty("Assign_Date")
    private String assignDate;

    @JsonProperty("Vendor_Name")
    private String vendorName;

    @JsonProperty("Delivery_Date")
    private String deliveryDate;

    @JsonProperty("Delivery_Type")
    private String deliveryType;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Confirm_Time")
    private String confirmTime;

    @JsonProperty("Sla1")
    private String sla1;

    @JsonProperty("OFD_Time")
    private String outForDeliveryTime;

    @JsonProperty("Sla2")
    private String sla2;

    @JsonProperty("Delivered_Time")
    private String deliveredTime;

    @JsonProperty("Sla3")
    private String sla3;

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getAssignDate()
    {
        return assignDate;
    }

    public void setAssignDate(String assignDate)
    {
        this.assignDate = assignDate;
    }

    public String getVendorName()
    {
        return vendorName;
    }

    public void setVendorName(String vendorName)
    {
        this.vendorName = vendorName;
    }

    public String getDeliveryDate()
    {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate)
    {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryType()
    {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType)
    {
        this.deliveryType = deliveryType;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getConfirmTime()
    {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime)
    {
        this.confirmTime = confirmTime;
    }

    public String getSla1()
    {
        return sla1;
    }

    public void setSla1(String sla1)
    {
        this.sla1 = sla1;
    }

    public String getOutForDeliveryTime()
    {
        return outForDeliveryTime;
    }

    public void setOutForDeliveryTime(String outForDeliveryTime)
    {
        this.outForDeliveryTime = outForDeliveryTime;
    }

    public String getSla2()
    {
        return sla2;
    }

    public void setSla2(String sla2)
    {
        this.sla2 = sla2;
    }

    public String getDeliveredTime()
    {
        return deliveredTime;
    }

    public void setDeliveredTime(String deliveredTime)
    {
        this.deliveredTime = deliveredTime;
    }

    public String getSla3()
    {
        return sla3;
    }

    public void setSla3(String sla3)
    {
        this.sla3 = sla3;
    }
}
