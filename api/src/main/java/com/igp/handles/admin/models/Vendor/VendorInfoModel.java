package com.igp.handles.admin.models.Vendor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanky on 26/1/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorInfoModel {
    @JsonProperty("Vendor_Id")
    private int vendorId;

    @JsonProperty("Vendor_Name")
    private String associateName;

    @JsonProperty("User_Id")
    private String userId;

    @JsonProperty("Password")
    private String password;

    @JsonProperty("Contact_Person")
    private String contactPerson;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Status")
    private int status;

    @JsonProperty("Rating")
    private int rating;

    @JsonProperty("Daily_Cap")
    private int dailyCap;

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

    public int getRating() {        return rating;    }

    public void setRating(int rating) {        this.rating = rating;    }

    public int getDailyCap() {        return dailyCap;    }

    public void setDailyCap(int dailyCap) {        this.dailyCap = dailyCap;    }
}
