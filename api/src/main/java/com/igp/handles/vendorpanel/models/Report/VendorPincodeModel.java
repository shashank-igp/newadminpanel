package com.igp.handles.vendorpanel.models.Report;

/**
 * Created by suditi on 19/3/18.
 */
public class VendorPincodeModel {
    private int vendorId;
    private String vendorName;
    private Integer pincode;
    private int shipTypeId;
    private String shipType;
    private Integer shipCharge;
    private int flagEnabled;
    private int cityId;
    private Integer reqdPrice;

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public void setShipCharge(Integer shipCharge) {
        this.shipCharge = shipCharge;
    }

    public void setReqdPrice(Integer reqdPrice) {
        this.reqdPrice = reqdPrice;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public int getShipTypeId() {
        return shipTypeId;
    }

    public void setShipTypeId(int shipTypeId) {
        this.shipTypeId = shipTypeId;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public int getShipCharge() {
        return shipCharge;
    }

    public void setShipCharge(int shipCharge) {
        this.shipCharge = shipCharge;
    }

    public int getFlagEnabled() {
        return flagEnabled;
    }

    public void setFlagEnabled(int flagEnabled) {
        this.flagEnabled = flagEnabled;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getReqdPrice() {
        return reqdPrice;
    }

    public void setReqdPrice(int reqdPrice) {
        this.reqdPrice = reqdPrice;
    }
}
