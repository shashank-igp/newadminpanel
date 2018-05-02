package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igp.admin.response.Status;

import java.util.Map;

/**
 * Created by suditi on 23/1/18.
 */
@JsonDeserialize
public class GeneralOrderResponseModel {
    @JsonProperty("status")
    private Status status;

    @JsonProperty("data")
    private Map<String,APIOrderResponseModel> data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, APIOrderResponseModel> getData() {
        return data;
    }

    public void setData(Map<String, APIOrderResponseModel> data) {
        this.data = data;
    }
}
