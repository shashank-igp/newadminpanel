package com.igp.handles.models.Vendor;

import java.util.Date;

/**
 * Created by shanky on 8/7/17.
 */
public class OrderDetailsPerVendor {
    private Long ordersId;

    private Date deliveredDate;

    private Date deliveryDate;

    private Date outForDeliveryDate;

    private String orderProductStatus;

    private boolean deliveryStatus;

    private String deliveryTime;

    private String shippingType;

    private int slaCode;

    private Long ordersProductsId;

    public Long getOrdersProductsId()
    {
        return ordersProductsId;
    }

    public void setOrdersProductsId(Long ordersProductsId)
    {
        this.ordersProductsId = ordersProductsId;
    }

    public Long getOrdersId()
    {
        return ordersId;
    }

    public void setOrdersId(Long ordersId)
    {
        this.ordersId = ordersId;
    }

    public Date getDeliveredDate()
    {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate)
    {
        this.deliveredDate = deliveredDate;
    }

    public Date getDeliveryDate()
    {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate)
    {
        this.deliveryDate = deliveryDate;
    }

    public Date getOutForDeliveryDate()
    {
        return outForDeliveryDate;
    }

    public void setOutForDeliveryDate(Date outForDeliveryDate)
    {
        this.outForDeliveryDate = outForDeliveryDate;
    }

    public String getOrderProductStatus()
    {
        return orderProductStatus;
    }

    public void setOrderProductStatus(String orderProductStatus)
    {
        this.orderProductStatus = orderProductStatus;
    }

    public boolean getDeliveryStatus()
    {
        return deliveryStatus;
    }

    public void setDeliveryStatus(boolean deliveryStatus)
    {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryTime()
    {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime)
    {
        this.deliveryTime = deliveryTime;
    }

    public String getShippingType()
    {
        return shippingType;
    }

    public void setShippingType(String shippingType)
    {
        this.shippingType = shippingType;
    }

    public int getSlaCode()
    {
        return slaCode;
    }

    public void setSlaCode(int slaCode)
    {
        this.slaCode = slaCode;
    }
}
