package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by suditi on 30/1/18.
 */
public class PincodeTableData {

    @JsonProperty("Vendor Id")
    private String  vendorId;

    @JsonProperty("Vendor Name")
    private String  vendorName;

    @JsonProperty("Pincode")
    private String pincode;

    @JsonProperty("Standard Delivery")
    private String  standardDeliveryCharge;

    @JsonProperty("Fixed Time Delivery")
    private String  fixedTimeDeliveryCharge;

    @JsonProperty("Midnight Delivery")
    private String  midnightDeliveryCharge;

    @JsonProperty("Change Required")
    private Map<String,Map<String,String>> changeRequired;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Map<String,Map<String,String>> getChangeRequired() {
        return changeRequired;
    }

    public void setChangeRequired(Map<String,Map<String,String>> changeRequired) {
        this.changeRequired = changeRequired;
    }
}
