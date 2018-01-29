package com.igp.admin.utils.marketPlace;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 16/1/18.
 */
public class Currency {

    @JsonProperty("USD")
    private Double usd;
    @JsonProperty("INR")
    private Double inr;

    public Double getUsd() {
        return usd;
    }

    public void setUsd(Double usd) {
        this.usd = usd;
    }

    public Double getInr() {
        return inr;
    }

    public void setInr(Double inr) {
        this.inr = inr;
    }
}
