package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by suditi on 18/1/18.
 */
@JsonDeserialize
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)

public class MarketPlaceOrderModel {

    @JsonProperty("cid")
    private String idHash;

    @JsonProperty("usrc")
    private String uniqSrc;

    @JsonProperty("tid")
    private Integer orderTempId;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("theme")
    private Integer theme;

    @JsonProperty("sinstr")
    private String specialInstructions;

    @JsonProperty("doption")
    private Integer deliveryOption;

    @JsonProperty("date")
    private String date; // inCase the delivery option is Select tentative delivery date.

    @JsonProperty("poption")
    private String paymentOption;

    @JsonProperty("oid")
    private Integer optionId;

    @JsonProperty("ops")
    private String orderPaySite;

    @JsonProperty("pstatus")
    private Boolean paymentStatus;

    @JsonProperty("btxnid")
    private String bankTransactionId;

    @JsonProperty("bauthcode")
    private String bankAuthorizationCode;

    @JsonProperty("hash")
    private String hash;

    @JsonProperty("sbi")
    private String sbiHash;

    @JsonProperty("upi")
    private String upiHash;

    @JsonProperty("callurl")
    private String callBackUrl;

    @JsonProperty("backurl")
    private String backUrl;

    @JsonProperty("fkasid")
    private Integer associateId;

    @JsonProperty("upiobject")
    private String upiObject;

    @JsonProperty("marketplace")
    private ExtraInfoModel extraInfoModel;

    public String getIdHash() {
        return idHash;
    }

    public void setIdHash(String idHash) {
        this.idHash = idHash;
    }

    public String getUniqSrc() {
        return uniqSrc;
    }

    public void setUniqSrc(String uniqSrc) {
        this.uniqSrc = uniqSrc;
    }

    public Integer getOrderTempId() {
        return orderTempId;
    }

    public void setOrderTempId(Integer orderTempId) {
        this.orderTempId = orderTempId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Integer getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(Integer deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOrderPaySite() {
        return orderPaySite;
    }

    public void setOrderPaySite(String orderPaySite) {
        this.orderPaySite = orderPaySite;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        this.bankTransactionId = bankTransactionId;
    }

    public String getBankAuthorizationCode() {
        return bankAuthorizationCode;
    }

    public void setBankAuthorizationCode(String bankAuthorizationCode) {
        this.bankAuthorizationCode = bankAuthorizationCode;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSbiHash() {
        return sbiHash;
    }

    public void setSbiHash(String sbiHash) {
        this.sbiHash = sbiHash;
    }

    public String getUpiHash() {
        return upiHash;
    }

    public void setUpiHash(String upiHash) {
        this.upiHash = upiHash;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public Integer getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Integer associateId) {
        this.associateId = associateId;
    }

    public String getUpiObject() {
        return upiObject;
    }

    public void setUpiObject(String upiObject) {
        this.upiObject = upiObject;
    }

    public ExtraInfoModel getExtraInfoModel() {
        return extraInfoModel;
    }

    public void setExtraInfoModel(ExtraInfoModel extraInfoModel) {
        this.extraInfoModel = extraInfoModel;
    }
}
