package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 16/1/18.
 */
public class ExtraInfoModel {
    // this includes extra info needed for the market place order generation.

    @JsonProperty("relid")
    private String relId;

    @JsonProperty("mdata")
    private String marketData;

    @JsonProperty("mname")
    private String marketName;

    @JsonProperty("gst")
    private String gstNo;

    private ExtraInfoModel(Builder builder) {
        setRelId(builder.relId);
        setMarketData(builder.marketData);
        setMarketName(builder.marketName);
        setGstNo(builder.gstNo);
    }


    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getMarketData() {
        return marketData;
    }

    public void setMarketData(String marketData) {
        this.marketData = marketData;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public static final class Builder {
        private String relId;
        private String marketData;
        private String marketName;
        private String gstNo;

        public Builder() {
        }

        public Builder relId(String val) {
            relId = val;
            return this;
        }

        public Builder marketData(String val) {
            marketData = val;
            return this;
        }

        public Builder marketName(String val) {
            marketName = val;
            return this;
        }

        public Builder gstNo(String val) {
            gstNo = val;
            return this;
        }

        public ExtraInfoModel build() {
            return new ExtraInfoModel(this);
        }
    }
}
