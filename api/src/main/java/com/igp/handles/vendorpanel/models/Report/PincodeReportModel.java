package com.igp.handles.vendorpanel.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shal on 1/10/17.
 */
public class PincodeReportModel {


    @JsonProperty("Pincode")
    private Integer pincode;

    @JsonProperty("Standard Delivery")
    private String  standardDeliveryCharge;

    @JsonProperty("Fixed Time Delivery")
    private String  fixedTimeDeliveryCharge;

    @JsonProperty("Midnight Delivery")
    private String  midnightDeliveryCharge;

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public String getStandardDeliveryCharge() {
        return standardDeliveryCharge;
    }

    public void setStandardDeliveryCharge(String standardDeliveryCharge) {
        this.standardDeliveryCharge = standardDeliveryCharge;
    }

    public String getFixedTimeDeliveryCharge() {
        return fixedTimeDeliveryCharge;
    }

    public void setFixedTimeDeliveryCharge(String fixedTimeDeliveryCharge) {
        this.fixedTimeDeliveryCharge = fixedTimeDeliveryCharge;
    }

    public String getMidnightDeliveryCharge() {
        return midnightDeliveryCharge;
    }

    public void setMidnightDeliveryCharge(String midnightDeliveryCharge) {
        this.midnightDeliveryCharge = midnightDeliveryCharge;
    }
}
