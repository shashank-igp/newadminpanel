package com.igp.admin.Voucher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by nikhil on 21/08/18.
 */
public class GiftVoucherModel
{
    @JsonProperty("id")
    private int id;

    @JsonProperty("couponcode")
    private String couponCode;

    @JsonProperty("couponcost")
    private BigDecimal couponcost;

    @JsonProperty("couponcostdollar")
    private BigDecimal couponDostDollar;

    @JsonProperty("couponuses")
    private int couponUses;

    @JsonProperty("couponlink")
    private String couponLink;

    @JsonProperty("couponstatus")
    private String couponStatus;

    @JsonProperty("purchaseorderid")
    private String purchaseOrderId;

    @JsonProperty("usedorderid")
    private String usedOrderId;

    @JsonProperty("vouchercat")
    private String voucherCat;;

    @JsonProperty("expirydate")
    private String expiryDate;

    @JsonProperty("fkasid")
    private int fkAssociateId;

    @JsonProperty("description")
    private String description;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCouponCode()
    {
        return couponCode;
    }

    public void setCouponCode(String couponCode)
    {
        this.couponCode = couponCode;
    }

    public BigDecimal getCouponcost()
    {
        return couponcost;
    }

    public void setCouponcost(BigDecimal couponcost)
    {
        this.couponcost = couponcost;
    }

    public BigDecimal getCouponDostDollar()
    {
        return couponDostDollar;
    }

    public void setCouponDostDollar(BigDecimal couponDostDollar)
    {
        this.couponDostDollar = couponDostDollar;
    }

    public int getCouponUses()
    {
        return couponUses;
    }

    public void setCouponUses(int couponUses)
    {
        this.couponUses = couponUses;
    }

    public String getCouponLink()
    {
        return couponLink;
    }

    public void setCouponLink(String couponLink)
    {
        this.couponLink = couponLink;
    }

    public String getCouponStatus()
    {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus)
    {
        this.couponStatus = couponStatus;
    }

    public String getPurchaseOrderId()
    {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId)
    {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getUsedOrderId()
    {
        return usedOrderId;
    }

    public void setUsedOrderId(String usedOrderId)
    {
        this.usedOrderId = usedOrderId;
    }

    public String getVoucherCat()
    {
        return voucherCat;
    }

    public void setVoucherCat(String voucherCat)
    {
        this.voucherCat = voucherCat;
    }

    public String getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public int getFkAssociateId()
    {
        return fkAssociateId;
    }

    public void setFkAssociateId(int fkAssociateId)
    {
        this.fkAssociateId = fkAssociateId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "GiftVoucherModel{" + "id=" + id + ", couponCode='" + couponCode + '\'' + ", couponcost=" + couponcost
            + ", couponDostDollar=" + couponDostDollar + ", couponUses=" + couponUses + ", couponLink='" + couponLink
            + '\'' + ", couponStatus='" + couponStatus + '\'' + ", purchaseOrderId='" + purchaseOrderId + '\''
            + ", usedOrderId='" + usedOrderId + '\'' + ", voucherCat='" + voucherCat + '\'' + ", expiryDate='"
            + expiryDate + '\'' + ", fkAssociateId=" + fkAssociateId + ", description='" + description + '\'' + '}';
    }
}
