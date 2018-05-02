package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by suditi on 23/1/18.
 */


public class UserAddressModel implements Cloneable {

    @Override
    public UserAddressModel clone() throws CloneNotSupportedException {
        return (UserAddressModel) super.clone();
    }

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("aid")
    private Integer addressId;

    @JsonProperty("uid")
    private Integer userId;

    @JsonProperty("bill")
    private AddressModel billingAddress;

    @JsonProperty("ship")
    private List<AddressModel> shippingAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public AddressModel getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressModel billingAddress) {
        this.billingAddress = billingAddress;
    }

    public List<AddressModel> getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(List<AddressModel> shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}

