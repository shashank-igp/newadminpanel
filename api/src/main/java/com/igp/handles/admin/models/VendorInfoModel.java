package com.igp.handles.admin.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanky on 26/1/18.
 */
public class VendorInfoModel {
    @JsonProperty("vendorId")
    private int vendorId;

    @JsonProperty("associateName")
    private String associateName;

    public int getVendorId()
    {
        return vendorId;
    }

    public void setVendorId(int vendorId)
    {
        this.vendorId = vendorId;
    }

    public String getAssociateName()
    {
        return associateName;
    }

    public void setAssociateName(String associateName)
    {
        this.associateName = associateName;
    }
}
