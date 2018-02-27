package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by shanky on 27/2/18.
 */
public class OrderProductUploadFileModel {
    @JsonProperty("Order_No")
    private String orderNo;

    @JsonProperty("Date")
    private String date;

    @JsonProperty("Vendor_Name")
    private String vendorName;

    @JsonProperty("Delivery_Date")
    private String deliveryDate;

    @JsonProperty("Out_Of_Delivery")
    private List<String> productPhotosOutOfDelivery;

    @JsonProperty("Proof_Of_Delivery")
    private List<String> productPhotosDelivered;

    @JsonProperty("Product_Photo")
    private String productActualPhoto;

    public String getProductActualPhoto()
    {
        return productActualPhoto;
    }

    public void setProductActualPhoto(String productActualPhoto)
    {
        this.productActualPhoto = productActualPhoto;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
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

    public List<String> getProductPhotosOutOfDelivery()
    {
        return productPhotosOutOfDelivery;
    }

    public void setProductPhotosOutOfDelivery(List<String> productPhotosOutOfDelivery)
    {
        this.productPhotosOutOfDelivery = productPhotosOutOfDelivery;
    }

    public List<String> getProductPhotosDelivered()
    {
        return productPhotosDelivered;
    }

    public void setProductPhotosDelivered(List<String> productPhotosDelivered)
    {
        this.productPhotosDelivered = productPhotosDelivered;
    }
}
