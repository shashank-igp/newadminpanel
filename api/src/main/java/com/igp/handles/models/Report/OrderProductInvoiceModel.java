package com.igp.handles.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanky on 28/12/17.
 */
public class OrderProductInvoiceModel {
    @JsonProperty("productName")
    private String productName;

    @JsonProperty("unitPrice")
    private double unitPrice;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("netAmount")
    private double netAmount;

    @JsonProperty("taxCode")
    private String taxCode;

    @JsonProperty("taxType")
    private String taxTypeMap;

    @JsonProperty("taxrate")
    private double taxrate;

    @JsonProperty("taxAmount")
    private double taxAmount;

    @JsonProperty("totalAmount")
    private double totalAmount;

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public double getNetAmount()
    {
        return netAmount;
    }

    public void setNetAmount(double netAmount)
    {
        this.netAmount = netAmount;
    }

    public String getTaxCode()
    {
        return taxCode;
    }

    public void setTaxCode(String taxCode)
    {
        this.taxCode = taxCode;
    }

    public String getTaxTypeMap()
    {
        return taxTypeMap;
    }

    public void setTaxTypeMap(String taxTypeMap)
    {
        this.taxTypeMap = taxTypeMap;
    }

    public double getTaxrate()
    {
        return taxrate;
    }

    public void setTaxrate(double taxrate)
    {
        this.taxrate = taxrate;
    }

    public double getTaxAmount()
    {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }
}
