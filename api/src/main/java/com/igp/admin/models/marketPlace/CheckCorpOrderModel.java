package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by suditi on 25/1/18.
 */
@JsonSerialize
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckCorpOrderModel {
    @JsonProperty("rel")
    private String relId;

    @JsonProperty("fkid")
    private Integer fkId;

    @JsonProperty("oid")
    private Integer orderId;

    @JsonProperty("flag")
    private Boolean flag;
    // true if order exists

    @JsonProperty("error")
    private Boolean error;
    // true if order exists


    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public Integer getFkId() {
        return fkId;
    }

    public void setFkId(Integer fkId) {
        this.fkId = fkId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
