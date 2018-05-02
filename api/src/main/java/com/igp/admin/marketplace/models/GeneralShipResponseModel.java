package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.igp.admin.response.Status;

/**
 * Created by suditi on 25/1/18.
 */
@JsonDeserialize
public class GeneralShipResponseModel {
    @JsonProperty("status")
    private Status status;

    @JsonProperty("data")
    private AddressModel data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AddressModel getData() {
        return data;
    }

    public void setData(AddressModel data) {
        this.data = data;
    }
}
