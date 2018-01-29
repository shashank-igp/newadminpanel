package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 19/1/18.
 */
public class MarketPlaceResponseModel {

    @JsonProperty("error")
    private Boolean error;

    @JsonProperty("rowNo")
    private Integer rowNumber;

    @JsonProperty("msg")
    private String message;

    private MarketPlaceResponseModel(Builder builder) {
        setError(builder.error);
        setRowNumber(builder.rowNumber);
        setMessage(builder.message);
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public static final class Builder {
        private Boolean error;
        private Integer rowNumber;
        private String message;

        public Builder() {
        }

        public Builder error(Boolean val) {
            error = val;
            return this;
        }

        public Builder rowNumber(Integer val) {
            rowNumber = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public MarketPlaceResponseModel build() {
            return new MarketPlaceResponseModel(this);
        }
    }
}
