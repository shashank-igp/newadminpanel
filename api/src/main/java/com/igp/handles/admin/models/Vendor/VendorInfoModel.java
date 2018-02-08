package com.igp.handles.admin.models.Vendor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanky on 26/1/18.
 */
public class VendorInfoModel {
    @JsonProperty("Vendor Id")
    private int vendorId;

    @JsonProperty("Vendor Name")
    private String associateName;

    @JsonProperty("User Id")
    private String userId;

    @JsonProperty("Password")
    private String password;

    @JsonProperty("Contact Person")
    private String contactPerson;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Status")
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
