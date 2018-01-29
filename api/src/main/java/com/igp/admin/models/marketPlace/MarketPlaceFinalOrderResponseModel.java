package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suditi on 22/1/18.
 */
@JsonSerialize
@JsonDeserialize

public class MarketPlaceFinalOrderResponseModel {

    @JsonProperty("error")
    List<ErrorModel> error;
    @JsonProperty("count")
    CountModel count;

    public List<ErrorModel> getError() {
        return error;
    }

    public void setError(List<ErrorModel> error) {
        this.error = error;
    }

    public CountModel getCount() {
        return count;
    }

    public void setCount(CountModel count) {
        this.count = count;
    }
}
