package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igp.admin.response.Status;

/**
 * Created by suditi on 25/1/18.
 */
@JsonDeserialize
public class GenerateCheckCorpOrderResponseModel {
    @JsonProperty("status")
    private Status status;

    @JsonProperty("data")
    private CheckCorpOrderModel data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public CheckCorpOrderModel getData() {
        return data;
    }

    public void setData(CheckCorpOrderModel data) {
        this.data = data;
    }
}
