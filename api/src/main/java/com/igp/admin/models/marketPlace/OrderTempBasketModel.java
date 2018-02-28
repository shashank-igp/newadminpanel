package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by suditi on 18/1/18.
 */
public class OrderTempBasketModel {

    @JsonProperty("cid")
    private Integer customerId;

    @JsonProperty("pid")
    private Integer productId;

    @JsonProperty("qty")
    private Integer quantity;

    @JsonProperty("otempid")
    private Integer orderTempId;

    @JsonProperty("oid")
    private Integer orderId;

    @JsonProperty("vid")
    private Integer vendorId;

    @JsonProperty("bcurr")
    private Integer baseCurrency;

    @JsonProperty("bcurrval")
    private Integer baseCurrencyValue;

    @JsonProperty("bcurrvalinr")
    private Integer baseCurrencyValueInr;

    @JsonProperty("scharge")
    private BigDecimal serviceCharges;

    @JsonProperty("stype")
    private String serviceType;

    @JsonProperty("attr")
    private String productAttribute;

    @JsonProperty("gbox")
    private Integer giftBox;

    @JsonProperty("stype")
    private Integer serviceTypeId;

    @JsonProperty("stime")
    private String serviceTime;

    @JsonProperty("sdate")
    private String serviceDate;

    @JsonIgnore
    private BigDecimal productSellingPrice;

    public BigDecimal getProductSellingPrice()
    {
        return productSellingPrice;
    }

    public void setProductSellingPrice(BigDecimal productSellingPrice)
    {
        this.productSellingPrice = productSellingPrice;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOrderTempId() {
        return orderTempId;
    }

    public void setOrderTempId(Integer orderTempId) {
        this.orderTempId = orderTempId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Integer baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Integer getBaseCurrencyValue() {
        return baseCurrencyValue;
    }

    public void setBaseCurrencyValue(Integer baseCurrencyValue) {
        this.baseCurrencyValue = baseCurrencyValue;
    }

    public Integer getBaseCurrencyValueInr() {
        return baseCurrencyValueInr;
    }

    public void setBaseCurrencyValueInr(Integer baseCurrencyValueInr) {
        this.baseCurrencyValueInr = baseCurrencyValueInr;
    }

    public BigDecimal getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(BigDecimal serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(String productAttribute) {
        this.productAttribute = productAttribute;
    }

    public Integer getGiftBox() {
        return giftBox;
    }

    public void setGiftBox(Integer giftBox) {
        this.giftBox = giftBox;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }
}
