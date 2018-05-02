package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by suditi on 11/1/18.
 */
public class MarketPlaceTempResponseModel {
    @JsonProperty("tempoid")
    private Integer tempOrderId;

    @JsonProperty("dsc")
    private Boolean discountApplied;

    @JsonProperty("totaldsc")
    private BigDecimal totalDiscount;

    @JsonProperty("totaldscusd")
    private BigDecimal totalDiscountUSD;

    @JsonProperty("shipcharge")
    private BigDecimal shippingCharge;

    @JsonProperty("country")
    private Integer country;

    @JsonProperty("shipchargeusd")
    private BigDecimal shippingChargeUSD;

    @JsonProperty("scharge")
    private BigDecimal serviceCharge;

    @JsonProperty("schargeusd")
    private BigDecimal serviceChargeUSD;

    @JsonProperty("stotal")
    private BigDecimal subTotal;

    @JsonProperty("stotalusd")
    private BigDecimal subTotalUSD;

    @JsonProperty("total")
    private BigDecimal total;

    @JsonProperty("totalusd")
    private BigDecimal totalUSD;

    @JsonProperty("error")
    private Boolean error;

    @JsonProperty("reqmsg")
    private Boolean messageNeeded;

    @JsonProperty("msg")
    private String errorMessage;

    @JsonProperty("pin")
    private String pincode;

    @JsonProperty("vchr")
    private String discountVoucher;

    @JsonProperty("adrs")
    private AddressModel addressModel;

    @JsonProperty("aid")
    private Integer addressId;

    @JsonProperty("plist")
    private List<ProductResponseModel> ProductResponseList;

    @JsonProperty("cstatus")
    private Boolean checkoutStatus;

    @JsonProperty("flag")
    private String flags;

    @JsonProperty("delmsg")
    private DeliveryMessageModel deliveryMessageModel;

    public Integer getTempOrderId() {
        return tempOrderId;
    }

    public void setTempOrderId(Integer tempOrderId) {
        this.tempOrderId = tempOrderId;
    }

    public Boolean getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(Boolean discountApplied) {
        this.discountApplied = discountApplied;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getTotalDiscountUSD() {
        return totalDiscountUSD;
    }

    public void setTotalDiscountUSD(BigDecimal totalDiscountUSD) {
        this.totalDiscountUSD = totalDiscountUSD;
    }

    public BigDecimal getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(BigDecimal shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public BigDecimal getShippingChargeUSD() {
        return shippingChargeUSD;
    }

    public void setShippingChargeUSD(BigDecimal shippingChargeUSD) {
        this.shippingChargeUSD = shippingChargeUSD;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getServiceChargeUSD() {
        return serviceChargeUSD;
    }

    public void setServiceChargeUSD(BigDecimal serviceChargeUSD) {
        this.serviceChargeUSD = serviceChargeUSD;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getSubTotalUSD() {
        return subTotalUSD;
    }

    public void setSubTotalUSD(BigDecimal subTotalUSD) {
        this.subTotalUSD = subTotalUSD;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalUSD() {
        return totalUSD;
    }

    public void setTotalUSD(BigDecimal totalUSD) {
        this.totalUSD = totalUSD;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Boolean getMessageNeeded() {
        return messageNeeded;
    }

    public void setMessageNeeded(Boolean messageNeeded) {
        this.messageNeeded = messageNeeded;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDiscountVoucher() {
        return discountVoucher;
    }

    public void setDiscountVoucher(String discountVoucher) {
        this.discountVoucher = discountVoucher;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public List<ProductResponseModel> getProductResponseList() {
        return ProductResponseList;
    }

    public void setProductResponseList(List<ProductResponseModel> productResponseList) {
        ProductResponseList = productResponseList;
    }

    public Boolean getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(Boolean checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public DeliveryMessageModel getDeliveryMessageModel() {
        return deliveryMessageModel;
    }

    public void setDeliveryMessageModel(DeliveryMessageModel deliveryMessageModel) {
        this.deliveryMessageModel = deliveryMessageModel;
    }
}
