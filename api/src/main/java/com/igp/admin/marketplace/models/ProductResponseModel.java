package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by suditi on 11/1/18.
 */
public class ProductResponseModel {
    @JsonProperty("pid")
    private Integer id;

    @JsonProperty("otempid")
    private Integer orderTempBasketId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private String image;

    @JsonProperty("error")
    private Boolean error;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("priceusd")
    private BigDecimal priceUSD;

    @JsonProperty("stotal")
    private BigDecimal stotal;

    @JsonProperty("stotalusd")
    private BigDecimal stotalUSD;

    @JsonProperty("qty")
    private Integer quantity;

    @JsonProperty("scharge")
    private BigDecimal serviceCharge;

    @JsonProperty("schargeusd")
    private BigDecimal serviceChargeUSD;

    @JsonProperty("dsc")
    private Boolean discountApplied;

    @JsonProperty("dscval")
    private Double discount;

    @JsonProperty("dscvalusd")
    private BigDecimal discountUsd;

    @JsonProperty("gbox")
    private Integer giftBox;

    @JsonProperty("vid")
    private Integer vendorId;

    @JsonProperty("vscharge")
    private Integer vendorShipCharge;

    @JsonProperty("vschargeusd")
    private BigDecimal vendorShipChargeUsd;

    @JsonProperty("pers")
    private Boolean personalizable;

    @JsonProperty("attr")
    private Map<String, String> displayAttrList;

    @JsonProperty("stype")
    private String serviceType;

    @JsonProperty("stime")
    private String serviceTime;

    @JsonProperty("gbxposs")
    private Boolean boxPossible;

    @JsonProperty("sdate")
    private String serviceDate;

    @JsonProperty("stypeid")
    private Integer serviceTypeId;

    @JsonProperty("ppheral")
    private BigDecimal peripheralPrice;

    @JsonProperty("ppheralusd")
    private BigDecimal peripheralPriceUsd;

    @JsonProperty("sku")
    private String skuId;

    @JsonProperty("oprice")
    private BigDecimal perProductPriceWithoutRoundOff;

    @JsonProperty("opriceusd")
    private BigDecimal perProductPriceWithoutRoundOffUsd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderTempBasketId() {
        return orderTempBasketId;
    }

    public void setOrderTempBasketId(Integer orderTempBasketId) {
        this.orderTempBasketId = orderTempBasketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(BigDecimal priceUSD) {
        this.priceUSD = priceUSD;
    }

    public BigDecimal getStotal() {
        return stotal;
    }

    public void setStotal(BigDecimal stotal) {
        this.stotal = stotal;
    }

    public BigDecimal getStotalUSD() {
        return stotalUSD;
    }

    public void setStotalUSD(BigDecimal stotalUSD) {
        this.stotalUSD = stotalUSD;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public Boolean getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(Boolean discountApplied) {
        this.discountApplied = discountApplied;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountUsd() {
        return discountUsd;
    }

    public void setDiscountUsd(BigDecimal discountUsd) {
        this.discountUsd = discountUsd;
    }

    public Integer getGiftBox() {
        return giftBox;
    }

    public void setGiftBox(Integer giftBox) {
        this.giftBox = giftBox;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getVendorShipCharge() {
        return vendorShipCharge;
    }

    public void setVendorShipCharge(Integer vendorShipCharge) {
        this.vendorShipCharge = vendorShipCharge;
    }

    public BigDecimal getVendorShipChargeUsd() {
        return vendorShipChargeUsd;
    }

    public void setVendorShipChargeUsd(BigDecimal vendorShipChargeUsd) {
        this.vendorShipChargeUsd = vendorShipChargeUsd;
    }

    public Boolean getPersonalizable() {
        return personalizable;
    }

    public void setPersonalizable(Boolean personalizable) {
        this.personalizable = personalizable;
    }

    public Map<String, String> getDisplayAttrList() {
        return displayAttrList;
    }

    public void setDisplayAttrList(Map<String, String> displayAttrList) {
        this.displayAttrList = displayAttrList;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Boolean getBoxPossible() {
        return boxPossible;
    }

    public void setBoxPossible(Boolean boxPossible) {
        this.boxPossible = boxPossible;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public BigDecimal getPeripheralPrice() {
        return peripheralPrice;
    }

    public void setPeripheralPrice(BigDecimal peripheralPrice) {
        this.peripheralPrice = peripheralPrice;
    }

    public BigDecimal getPeripheralPriceUsd() {
        return peripheralPriceUsd;
    }

    public void setPeripheralPriceUsd(BigDecimal peripheralPriceUsd) {
        this.peripheralPriceUsd = peripheralPriceUsd;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getPerProductPriceWithoutRoundOff() {
        return perProductPriceWithoutRoundOff;
    }

    public void setPerProductPriceWithoutRoundOff(BigDecimal perProductPriceWithoutRoundOff) {
        this.perProductPriceWithoutRoundOff = perProductPriceWithoutRoundOff;
    }

    public BigDecimal getPerProductPriceWithoutRoundOffUsd() {
        return perProductPriceWithoutRoundOffUsd;
    }

    public void setPerProductPriceWithoutRoundOffUsd(BigDecimal perProductPriceWithoutRoundOffUsd) {
        this.perProductPriceWithoutRoundOffUsd = perProductPriceWithoutRoundOffUsd;
    }
}
