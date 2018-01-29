package com.igp.handles.admin.models.Vendor;

/**
 * Created by shanky on 29/1/18.
 */
public class VendorAssignModel {
    private int orderId;

    private int fkAssociateId;

    private double orderSubtotal;

    private double shipping;

    private double vendorPrice;

    private int productId;

    private String productName;

    private String productCode;

    private String city;

    private int mailRemainderFlag;

    private int assignThrough;

    private String assignByUser;

    private String shipppingType;

    private String deliveryDate;

    private String deliveryTime;

    private int flagEggless;

    public int getOrderId()
    {
        return orderId;
    }

    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }

    public int getFkAssociateId()
    {
        return fkAssociateId;
    }

    public void setFkAssociateId(int fkAssociateId)
    {
        this.fkAssociateId = fkAssociateId;
    }

    public double getOrderSubtotal()
    {
        return orderSubtotal;
    }

    public void setOrderSubtotal(double orderSubtotal)
    {
        this.orderSubtotal = orderSubtotal;
    }

    public double getShipping()
    {
        return shipping;
    }

    public void setShipping(double shipping)
    {
        this.shipping = shipping;
    }

    public double getVendorPrice()
    {
        return vendorPrice;
    }

    public void setVendorPrice(double vendorPrice)
    {
        this.vendorPrice = vendorPrice;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        this.productId = productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public int getMailRemainderFlag()
    {
        return mailRemainderFlag;
    }

    public void setMailRemainderFlag(int mailRemainderFlag)
    {
        this.mailRemainderFlag = mailRemainderFlag;
    }

    public int getAssignThrough()
    {
        return assignThrough;
    }

    public void setAssignThrough(int assignThrough)
    {
        this.assignThrough = assignThrough;
    }

    public String getAssignByUser()
    {
        return assignByUser;
    }

    public void setAssignByUser(String assignByUser)
    {
        this.assignByUser = assignByUser;
    }

    public String getShipppingType()
    {
        return shipppingType;
    }

    public void setShipppingType(String shipppingType)
    {
        this.shipppingType = shipppingType;
    }

    public String getDeliveryDate()
    {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate)
    {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime()
    {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime)
    {
        this.deliveryTime = deliveryTime;
    }

    public int getFlagEggless()
    {
        return flagEggless;
    }

    public void setFlagEggless(int flagEggless)
    {
        this.flagEggless = flagEggless;
    }
}
