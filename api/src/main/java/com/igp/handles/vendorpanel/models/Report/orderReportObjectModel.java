package com.igp.handles.vendorpanel.models.Report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shal on 22/9/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class orderReportObjectModel {


    @JsonProperty("Order_No")
    private String orderNo;


    @JsonProperty("Date")
    private String date;

    @JsonProperty("Vendor_Id")
    private String vendorId;

    @JsonProperty("Vendor_Name")
    private String vendorName;

    @JsonProperty("Occasion")
    private String occasion;

    @JsonProperty("City")
    private String city;

    @JsonProperty("Pincode")
    private int  pincode;

    @JsonProperty("Delivery_Date")
    private String delivery_Date;

    @JsonProperty("Delivery_Type")
    private String deliveryType;

    @JsonProperty("Recipient_Name")
    private String recipienName;

    @JsonProperty("Phone")
    private String phoneNumber;

    @JsonProperty("Amount")
    private double  price;


    @JsonProperty("Status")
    private String orderProductStatus;










    @JsonIgnore
    private Integer status;



    @JsonIgnore
    private Double totalAmountSummary;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getDelivery_Date() {
        return delivery_Date;
    }

    public void setDelivery_Date(String delivery_Date) {
        this.delivery_Date = delivery_Date;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getRecipienName() {
        return recipienName;
    }

    public void setRecipienName(String recipienName) {
        this.recipienName = recipienName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getTotalAmountSummary() {
        return totalAmountSummary;
    }

    public void setTotalAmountSummary(Double totalAmountSummary) {
        this.totalAmountSummary = totalAmountSummary;
    }

    public String getOrderProductStatus() {
        return orderProductStatus;
    }

    public void setOrderProductStatus(String orderProductStatus) {
        this.orderProductStatus = orderProductStatus;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
