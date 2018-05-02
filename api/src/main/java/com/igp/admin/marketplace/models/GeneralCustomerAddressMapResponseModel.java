package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igp.admin.response.Status;

import java.util.Map;

/**
 * Created by suditi on 23/1/18.
 */
@JsonDeserialize
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GeneralCustomerAddressMapResponseModel {
    @JsonProperty("status")
    private Status status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Map<String,UserAddressModel> data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String,UserAddressModel> getData() {
        return data;
    }

    public void setData(Map<String,UserAddressModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
