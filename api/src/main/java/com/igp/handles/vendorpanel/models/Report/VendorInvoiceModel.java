package com.igp.handles.vendorpanel.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by shanky on 28/12/17.
 */
public class VendorInvoiceModel {
    @JsonProperty("orderId")
    private int orderId;

    @JsonProperty("invoiceNumber")
    private String invoiceNumber;

    @JsonProperty("datePurchased")
    private String datePurchased;

    @JsonProperty("billingAddress")
    private VendorInfoModel billingAddressModel;

    @JsonProperty("sellerAddress")
    private VendorInfoModel sellerAddressModel;

    @JsonProperty("productDetail")
    private List<OrderProductInvoiceModel> orderProductInvoiceModelList;

    @JsonProperty("total")
    private double total;

    @JsonProperty("totalNetAmount")
    private double totalNetAmount;

    @JsonProperty("totalTaxAmount")
    private double totalTaxAmount;

    public double getTotalNetAmount()
    {
        return totalNetAmount;
    }

    public void setTotalNetAmount(double totalNetAmount)
    {
        this.totalNetAmount = totalNetAmount;
    }

    public double getTotalTaxAmount()
    {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount)
    {
        this.totalTaxAmount = totalTaxAmount;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public String getInvoiceNumber()
    {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    public String getDatePurchased()
    {
        return datePurchased;
    }

    public void setDatePurchased(String datePurchased)
    {
        this.datePurchased = datePurchased;
    }

    public VendorInfoModel getBillingAddressModel()
    {
        return billingAddressModel;
    }

    public void setBillingAddressModel(VendorInfoModel billingAddressModel)
    {
        this.billingAddressModel = billingAddressModel;
    }

    public VendorInfoModel getSellerAddressModel()
    {
        return sellerAddressModel;
    }

    public void setSellerAddressModel(VendorInfoModel sellerAddressModel)
    {
        this.sellerAddressModel = sellerAddressModel;
    }

    public List<OrderProductInvoiceModel> getOrderProductInvoiceModelList()
    {
        return orderProductInvoiceModelList;
    }

    public void setOrderProductInvoiceModelList(List<OrderProductInvoiceModel> orderProductInvoiceModelList)
    {
        this.orderProductInvoiceModelList = orderProductInvoiceModelList;
    }

    public double getTotal()
    {
        return total;
    }

    public void setTotal(double total)
    {
        this.total = total;
    }
}
