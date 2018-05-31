package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by suditi on 11/1/18.
 */
@JsonDeserialize
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressModel {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)

    @JsonProperty("id")
    private String id; // this is customer ID

    @JsonProperty("aid")
    private String aid;

    @JsonProperty("title")
    private String title;

    @JsonProperty("fname")
    private String firstname;

    @JsonProperty("lname")
    private String lastname;

    @JsonProperty("saddr")
    private String streetAddress;

    @JsonProperty("saddr2")
    private String streetAddress2;

    @JsonProperty("pcode")
    private String postcode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("cid")
    private String countryId;

    @JsonProperty("rel")
    private String relation;

    @JsonProperty("email")
    private String email;

    @JsonProperty("ph")
    private String landline;

    @JsonProperty("mob")
    private String mobile;

    @JsonProperty("mprefix")
    private String mobilePrefix;

    @JsonProperty("saddrtype")
    private int addressType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getTitle() {
        if(title==null){
            title="";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobilePrefix() {
        return mobilePrefix;
    }

    public void setMobilePrefix(String mobilePrefix) {
        this.mobilePrefix = mobilePrefix;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    @Override
    public String toString() {
        return "AddressModel{" +
            "id='" + id + '\'' +
            ", aid='" + aid + '\'' +
            ", title='" + title + '\'' +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", streetAddress='" + streetAddress + '\'' +
            ", streetAddress2='" + streetAddress2 + '\'' +
            ", postcode='" + postcode + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", countryId='" + countryId + '\'' +
            ", relation='" + relation + '\'' +
            ", email='" + email + '\'' +
            ", landline='" + landline + '\'' +
            ", mobile='" + mobile + '\'' +
            ", mobilePrefix='" + mobilePrefix + '\'' +
            ", addressType=" + addressType +
            '}';
    }
}
