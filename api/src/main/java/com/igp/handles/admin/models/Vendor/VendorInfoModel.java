package com.igp.handles.admin.models.Vendor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanky on 26/1/18.
 */
public class VendorInfoModel {
    @JsonProperty("fkAssociateId")
    private int vendorId;

    @JsonProperty("associateName")
    private String associateName;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("password")
    private String password;

    @JsonProperty("contactPerson")
    private String contactPerson;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("status")
    private int status;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
