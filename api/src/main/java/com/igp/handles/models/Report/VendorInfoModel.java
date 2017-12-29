package com.igp.handles.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanky on 28/12/17.
 */
public class VendorInfoModel
{
    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("ph")
    private String contactDetails;

    @JsonProperty("gstn")
    private String gstNumber;

    @JsonProperty("pan")
    private String pan;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getContactDetails()
    {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails)
    {
        this.contactDetails = contactDetails;
    }

    public String getGstNumber()
    {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber)
    {
        this.gstNumber = gstNumber;
    }

    public String getPan()
    {
        return pan;
    }

    public void setPan(String pan)
    {
        this.pan = pan;
    }
}
