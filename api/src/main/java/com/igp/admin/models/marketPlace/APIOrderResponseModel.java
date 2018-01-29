package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.igp.admin.models.marketPlace.PaymentOptionModel;

/**
 * Created by suditi on 18/1/18.
 */
@JsonDeserialize
@JsonSerialize
public class APIOrderResponseModel {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)

    private Integer orderId;
    private Boolean error;
    private String message;
    private PaymentOptionModel poption;
    private String sbihash;
    private String pspRefNo; // temp_order_Id
    private String custRefNo; //customer refrence number provided by sbi UPI
    private String transactionStatus;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentOptionModel getPoption() {
        return poption;
    }

    public void setPoption(PaymentOptionModel poption) {
        this.poption = poption;
    }

    public String getSbihash() {
        return sbihash;
    }

    public void setSbihash(String sbihash) {
        this.sbihash = sbihash;
    }

    public String getPspRefNo() {
        return pspRefNo;
    }

    public void setPspRefNo(String pspRefNo) {
        this.pspRefNo = pspRefNo;
    }

    public String getCustRefNo() {
        return custRefNo;
    }

    public void setCustRefNo(String custRefNo) {
        this.custRefNo = custRefNo;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
