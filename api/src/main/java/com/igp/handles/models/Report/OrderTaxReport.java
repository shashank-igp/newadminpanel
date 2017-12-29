package com.igp.handles.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanky on 27/12/17.
 */
public class OrderTaxReport {

    @JsonProperty("invoice number")
    private String invoiceNumber;

    @JsonProperty("orderId")
    private int orderId;

    @JsonProperty("date purchased")
    private String datePurchased;

    @JsonProperty("delivery date")
    private String deliveryDate;

    @JsonProperty("pincode")
    private String pincode;

    @JsonProperty("order status")
    private String orderStatus;

    @JsonProperty("taxable amount")
    private double taxableAmount;

    @JsonProperty("tax")
    private double tax;

    @JsonProperty("total amount")
    private double totalAmount;

    @JsonProperty("payment status")
    private String paymentStatus;

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public String getDatePurchased()
    {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased)
    {
        this.datePurchased = datePurchased;
    }

    public String getPincode()
    {
        return pincode;
    }

    public void setPincode(String pincode)
    {
        this.pincode = pincode;
    }


    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getInvoiceNumber()
    {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    public double getTaxableAmount()
    {
        return taxableAmount;
    }

    public void setTaxableAmount(double taxableAmount)
    {
        this.taxableAmount = taxableAmount;
    }

    public double getTax()
    {
        return tax;
    }

    public void setTax(double tax)
    {
        this.tax = tax;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus()
    {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryDate()
    {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate)
    {
        this.deliveryDate = deliveryDate;
    }
}
