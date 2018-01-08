package com.igp.handles.vendorpanel.models.Report;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

/**
 * Created by shanky on 21/9/17.
 */
@JsonDeserialize
@JsonSerialize
public class OrderReportModel {
   private Objects[] orderDetails;

    public Objects[] getOrderDetails()
    {
        return orderDetails;
    }

    public void setOrderDetails(Objects[] orderDetails)
    {
        this.orderDetails = orderDetails;
    }
}
