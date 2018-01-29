package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by suditi on 11/1/18.
 */
public class MarketPlaceTempOrderModel {
    @JsonProperty("custid")
    private String customerId;

    @JsonProperty("cid")
    private String idHash;

    @JsonProperty("usrc")
    private String uniqSrc;

    @JsonProperty("adrsid")
    private Integer addressBookId;

    @JsonProperty("dsc")
    private BigDecimal discount;

    @JsonProperty("dscusd")
    private BigDecimal discountUSD;

    @JsonProperty("scharge")
    private BigDecimal shippingCharges;

    @JsonProperty("schargeusd")
    private BigDecimal shippingChargesUSD;

    @JsonProperty("cval")
    private BigDecimal cartValue;

    @JsonProperty("cvalusd")
    private BigDecimal cartValueUSD;

    @JsonProperty("fkasid")
    private Integer associateId;

    @JsonProperty("vchr")
    private String voucher;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("rel")
    private String relation;

    @JsonProperty("dinstr")
    private String deliveryInstr;

    @JsonProperty("eval")
    private String extraValue;

    @JsonProperty("sadrs")
    private AddressModel shippingAddressModel;

    @JsonProperty("delmsg")
    private DeliveryMessageModel deliveryMessageModel;

    @JsonProperty("tempoid")
    private Integer tempOrderId;

    @JsonProperty("occasionid")
    private int occasionId;

    private String deliveryDate;

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUniqSrc() {
        return uniqSrc;
    }

    public void setUniqSrc(String uniqSrc) {
        this.uniqSrc = uniqSrc;
    }

    public Integer getAddressBookId() {
        return addressBookId;
    }

    public void setAddressBookId(Integer addressBookId) {
        this.addressBookId = addressBookId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountUSD() {
        return discountUSD;
    }

    public void setDiscountUSD(BigDecimal discountUSD) {
        this.discountUSD = discountUSD;
    }

    public BigDecimal getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(BigDecimal shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public BigDecimal getShippingChargesUSD() {
        return shippingChargesUSD;
    }

    public void setShippingChargesUSD(BigDecimal shippingChargesUSD) {
        this.shippingChargesUSD = shippingChargesUSD;
    }

    public BigDecimal getCartValue() {
        return cartValue;
    }

    public void setCartValue(BigDecimal cartValue) {
        this.cartValue = cartValue;
    }

    public BigDecimal getCartValueUSD() {
        return cartValueUSD;
    }

    public void setCartValueUSD(BigDecimal cartValueUSD) {
        this.cartValueUSD = cartValueUSD;
    }

    public Integer getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Integer associateId) {
        this.associateId = associateId;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDeliveryInstr() {
        return deliveryInstr;
    }

    public void setDeliveryInstr(String deliveryInstr) {
        this.deliveryInstr = deliveryInstr;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }

    public AddressModel getShippingAddressModel() {
        return shippingAddressModel;
    }

    public void setShippingAddressModel(AddressModel shippingAddressModel) {
        this.shippingAddressModel = shippingAddressModel;
    }

    public DeliveryMessageModel getDeliveryMessageModel() {
        return deliveryMessageModel;
    }

    public void setDeliveryMessageModel(DeliveryMessageModel deliveryMessageModel) {
        this.deliveryMessageModel = deliveryMessageModel;
    }

    public Integer getTempOrderId() {
        return tempOrderId;
    }

    public void setTempOrderId(Integer tempOrderId) {
        this.tempOrderId = tempOrderId;
    }

    public int getOccasionId() {
        return occasionId;
    }

    public void setOccasionId(int occasionId) {
        this.occasionId = occasionId;
    }

    public String getIdHash() {
        return idHash;
    }

    public void setIdHash(String idHash) {
        this.idHash = idHash;
    }
}
