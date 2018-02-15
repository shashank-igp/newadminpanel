package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by suditi on 30/1/18.
 */
public class PincodeTableDataModel {

    @JsonIgnore
    private String  vendorId;

    @JsonProperty("Vendor Name")
    private String  vendorName;

    @JsonProperty("Pincode")
    private String pincode;

    @JsonProperty("Standard Delivery")
    private TableDataActionHandels  standardDeliveryCharge;

    @JsonProperty("Fixed Time Delivery")
    private TableDataActionHandels  fixedTimeDeliveryCharge;

    @JsonProperty("Midnight Delivery")
    private TableDataActionHandels  midnightDeliveryCharge;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public TableDataActionHandels getStandardDeliveryCharge() {
        return standardDeliveryCharge;
    }

    public void setStandardDeliveryCharge(TableDataActionHandels standardDeliveryCharge) {
        this.standardDeliveryCharge = standardDeliveryCharge;
    }

    public TableDataActionHandels getFixedTimeDeliveryCharge() {
        return fixedTimeDeliveryCharge;
    }

    public void setFixedTimeDeliveryCharge(TableDataActionHandels fixedTimeDeliveryCharge) {
        this.fixedTimeDeliveryCharge = fixedTimeDeliveryCharge;
    }

    public TableDataActionHandels getMidnightDeliveryCharge() {
        return midnightDeliveryCharge;
    }

    public void setMidnightDeliveryCharge(TableDataActionHandels midnightDeliveryCharge) {
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


}
