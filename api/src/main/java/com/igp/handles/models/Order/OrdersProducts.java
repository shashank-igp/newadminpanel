package com.igp.handles.models.Order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shanky on 8/8/17.
 */
public class OrdersProducts implements Serializable{


    private int					orderProductId									= -1;


    private int					orderId											= -1;


    private int					productId										= -1;


    private String				productName										= null;


    private BigDecimal productPrice = null;


    private double				productPrice_inr								= 0;


    private int					productQuantity									= -1;


    private String				productSize										= null;


    private String				products_weight									= null;


    private String				products_code									= null;


    private String				fkAssociateId									= null;


    private BigDecimal			orderShippingAssociatewise						= null;


    private String				ordersProductStatus								= null;


    private String				ordersAwbnumberAssociatewise					= null;


    private Integer				ordersProductsCourierid							= -1;


    private String				ordersProductsCancel_id							= null;


    private String				airBillWeight									= null;


    private Date				dispatchDate									= null;


    private int					payoutOnHold									= -1;


    private int					ordersProductsBaseCurrency						= -1;


    private double				ordersProductsBaseCurrencyConversionRateInUsd	= 0;


    private double				ordersProductsBaseCurrencyConversionRateInInr	= 0;


    private long				SpecialChargesShip								= -1;


    private String				shippingTypeG									= null;


    private int					deliveryStatus									= -1;


    private int						slaCode											= -1;


    private boolean 				slaFlag											=false;


    private boolean 				alertFlag											=false;


    private String					productImage									= null;


    private Date productUpdateDateTime = null;


    private String					productNameForUrl								= null;


    private double					vendorPrice										= 0.0;


    private boolean					personalized									= false;


    private List<OrderComponent> componentList = new ArrayList<>();


    private double componentTotal=0.0;

    private double priceAdjustmentPerProduct=0.0;

    private String timeSlaVoilates=null;

    public String getTimeSlaVoilates()
    {
        return timeSlaVoilates;
    }

    public double getPriceAdjustmentPerProduct()
    {
        return priceAdjustmentPerProduct;
    }

    public void setPriceAdjustmentPerProduct(double priceAdjustmentPerProduct)
    {
        this.priceAdjustmentPerProduct = priceAdjustmentPerProduct;
    }

    public double getComponentTotal()
    {
        return componentTotal;
    }

    public void setComponentTotal(double componentTotal)
    {
        this.componentTotal = componentTotal;
    }

    private OrderProductExtraInfo orderProductExtraInfo = null;

    public int getOrderProductId()
    {
        return orderProductId;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public int getProductId()
    {
        return productId;
    }

    public String getProductName()
    {
        return productName;
    }

    public BigDecimal getProductPrice()
    {
        return productPrice;
    }

    public double getProductPrice_inr()
    {
        return productPrice_inr;
    }

    public int getProductQuantity()
    {
        return productQuantity;
    }

    public String getProductSize()
    {
        return productSize;
    }

    public String getProducts_weight()
    {
        return products_weight;
    }

    public String getProducts_code()
    {
        return products_code;
    }

    public String getFkAssociateId()
    {
        return fkAssociateId;
    }

    public BigDecimal getOrderShippingAssociatewise()
    {
        return orderShippingAssociatewise;
    }

    public String getOrdersProductStatus()
    {
        return ordersProductStatus;
    }

    public String getOrdersAwbnumberAssociatewise()
    {
        return ordersAwbnumberAssociatewise;
    }

    public Integer getOrdersProductsCourierid()
    {
        return ordersProductsCourierid;
    }

    public String getOrdersProductsCancel_id()
    {
        return ordersProductsCancel_id;
    }

    public String getAirBillWeight()
    {
        return airBillWeight;
    }

    public Date getDispatchDate()
    {
        return dispatchDate;
    }

    public int getPayoutOnHold()
    {
        return payoutOnHold;
    }

    public int getOrdersProductsBaseCurrency()
    {
        return ordersProductsBaseCurrency;
    }

    public double getOrdersProductsBaseCurrencyConversionRateInUsd()
    {
        return ordersProductsBaseCurrencyConversionRateInUsd;
    }

    public double getOrdersProductsBaseCurrencyConversionRateInInr()
    {
        return ordersProductsBaseCurrencyConversionRateInInr;
    }

    public long getSpecialChargesShip()
    {
        return SpecialChargesShip;
    }

    public String getShippingTypeG()
    {
        return shippingTypeG;
    }

    public int getDeliveryStatus()
    {
        return deliveryStatus;
    }

    public int getSlaCode()
    {
        return slaCode;
    }

    public boolean isSlaFlag()
    {
        return slaFlag;
    }

    public boolean isAlertFlag()
    {
        return alertFlag;
    }

    public String getProductImage()
    {
        return productImage;
    }

    public Date getProductUpdateDateTime()
    {
        return productUpdateDateTime;
    }

    public String getProductNameForUrl()
    {
        return productNameForUrl;
    }

    public double getVendorPrice()
    {
        return vendorPrice;
    }

    public boolean isPersonalized()
    {
        return personalized;
    }

    public List<OrderComponent> getComponentList()
    {
        return componentList;
    }

    public OrderProductExtraInfo getOrderProductExtraInfo()
    {
        return orderProductExtraInfo;
    }

    public void setSlaFlag(boolean slaFlag)
    {
        this.slaFlag = slaFlag;
    }

    public void setAlertFlag(boolean alertFlag)
    {
        this.alertFlag = alertFlag;
    }

    public void setOrderProductExtraInfo(OrderProductExtraInfo orderProductExtraInfo)
    {
        this.orderProductExtraInfo = orderProductExtraInfo;
    }

    public void setVendorPrice(double vendorPrice)
    {
        this.vendorPrice = vendorPrice;
    }

    public void setComponentList(List<OrderComponent> componentList)
    {
        this.componentList = componentList;
    }

    private OrdersProducts(Builder builder)
    {
        orderProductId = builder.orderProductId;
        orderId = builder.orderId;
        productId = builder.productId;
        productName = builder.productName;
        productPrice = builder.productPrice;
        productPrice_inr = builder.productPrice_inr;
        productQuantity = builder.productQuantity;
        productSize = builder.productSize;
        products_weight = builder.products_weight;
        products_code = builder.products_code;
        fkAssociateId = builder.fkAssociateId;
        orderShippingAssociatewise = builder.orderShippingAssociatewise;
        ordersProductStatus = builder.ordersProductStatus;
        ordersAwbnumberAssociatewise = builder.ordersAwbnumberAssociatewise;
        ordersProductsCourierid = builder.ordersProductsCourierid;
        ordersProductsCancel_id = builder.ordersProductsCancel_id;
        airBillWeight = builder.airBillWeight;
        dispatchDate = builder.dispatchDate;
        payoutOnHold = builder.payoutOnHold;
        ordersProductsBaseCurrency = builder.ordersProductsBaseCurrency;
        ordersProductsBaseCurrencyConversionRateInUsd = builder.ordersProductsBaseCurrencyConversionRateInUsd;
        ordersProductsBaseCurrencyConversionRateInInr = builder.ordersProductsBaseCurrencyConversionRateInInr;
        SpecialChargesShip = builder.SpecialChargesShip;
        shippingTypeG = builder.shippingTypeG;
        deliveryStatus = builder.deliveryStatus;
        slaCode = builder.slaCode;
        slaFlag = builder.slaFlag;
        alertFlag = builder.alertFlag;
        productImage = builder.productImage;
        productUpdateDateTime = builder.productUpdateDateTime;
        productNameForUrl = builder.productNameForUrl;
        vendorPrice = builder.vendorPrice;
        personalized = builder.personalized;
        componentList = builder.componentList;
        orderProductExtraInfo = builder.orderProductExtraInfo;
        timeSlaVoilates=builder.timeSlaVoilates;
    }

    public static final class Builder
    {
        private int                   orderProductId;
        private int                   orderId;
        private int                   productId;
        private String                productName;
        private BigDecimal            productPrice;
        private double                productPrice_inr;
        private int                   productQuantity;
        private String                productSize;
        private String                products_weight;
        private String                products_code;
        private String                fkAssociateId;
        private BigDecimal            orderShippingAssociatewise;
        private String                ordersProductStatus;
        private String                ordersAwbnumberAssociatewise;
        private Integer               ordersProductsCourierid;
        private String                ordersProductsCancel_id;
        private String                airBillWeight;
        private Date                  dispatchDate;
        private int                   payoutOnHold;
        private int                   ordersProductsBaseCurrency;
        private double                ordersProductsBaseCurrencyConversionRateInUsd;
        private double                ordersProductsBaseCurrencyConversionRateInInr;
        private long                  SpecialChargesShip;
        private String                shippingTypeG;
        private int                   deliveryStatus;
        private int                   slaCode;
        private boolean               slaFlag;
        private boolean               alertFlag;
        private String                productImage;
        private Date                  productUpdateDateTime;
        private String                productNameForUrl;
        private double                vendorPrice;
        private boolean               personalized;
        private List<OrderComponent>  componentList;
        private OrderProductExtraInfo orderProductExtraInfo;
        private String timeSlaVoilates;

        public Builder()
        {
        }

        public Builder orderProductId(int val)
        {
            orderProductId = val;
            return this;
        }

        public Builder orderId(int val)
        {
            orderId = val;
            return this;
        }

        public Builder productId(int val)
        {
            productId = val;
            return this;
        }

        public Builder productName(String val)
        {
            productName = val;
            return this;
        }

        public Builder productPrice(BigDecimal val)
        {
            productPrice = val;
            return this;
        }

        public Builder productPrice_inr(double val)
        {
            productPrice_inr = val;
            return this;
        }

        public Builder productQuantity(int val)
        {
            productQuantity = val;
            return this;
        }

        public Builder productSize(String val)
        {
            productSize = val;
            return this;
        }

        public Builder products_weight(String val)
        {
            products_weight = val;
            return this;
        }

        public Builder products_code(String val)
        {
            products_code = val;
            return this;
        }

        public Builder fkAssociateId(String val)
        {
            fkAssociateId = val;
            return this;
        }

        public Builder orderShippingAssociatewise(BigDecimal val)
        {
            orderShippingAssociatewise = val;
            return this;
        }

        public Builder ordersProductStatus(String val)
        {
            ordersProductStatus = val;
            return this;
        }

        public Builder ordersAwbnumberAssociatewise(String val)
        {
            ordersAwbnumberAssociatewise = val;
            return this;
        }

        public Builder ordersProductsCourierid(Integer val)
        {
            ordersProductsCourierid = val;
            return this;
        }

        public Builder ordersProductsCancel_id(String val)
        {
            ordersProductsCancel_id = val;
            return this;
        }

        public Builder airBillWeight(String val)
        {
            airBillWeight = val;
            return this;
        }

        public Builder dispatchDate(Date val)
        {
            dispatchDate = val;
            return this;
        }

        public Builder payoutOnHold(int val)
        {
            payoutOnHold = val;
            return this;
        }

        public Builder ordersProductsBaseCurrency(int val)
        {
            ordersProductsBaseCurrency = val;
            return this;
        }

        public Builder ordersProductsBaseCurrencyConversionRateInUsd(double val)
        {
            ordersProductsBaseCurrencyConversionRateInUsd = val;
            return this;
        }

        public Builder ordersProductsBaseCurrencyConversionRateInInr(double val)
        {
            ordersProductsBaseCurrencyConversionRateInInr = val;
            return this;
        }

        public Builder SpecialChargesShip(long val)
        {
            SpecialChargesShip = val;
            return this;
        }

        public Builder shippingTypeG(String val)
        {
            shippingTypeG = val;
            return this;
        }

        public Builder deliveryStatus(int val)
        {
            deliveryStatus = val;
            return this;
        }

        public Builder slaCode(int val)
        {
            slaCode = val;
            return this;
        }

        public Builder slaFlag(boolean val)
        {
            slaFlag = val;
            return this;
        }

        public Builder alertFlag(boolean val)
        {
            alertFlag = val;
            return this;
        }

        public Builder productImage(String val)
        {
            productImage = val;
            return this;
        }

        public Builder productUpdateDateTime(Date val)
        {
            productUpdateDateTime = val;
            return this;
        }

        public Builder productNameForUrl(String val)
        {
            productNameForUrl = val;
            return this;
        }

        public Builder vendorPrice(double val)
        {
            vendorPrice = val;
            return this;
        }

        public Builder personalized(boolean val)
        {
            personalized = val;
            return this;
        }

        public Builder componentList(List<OrderComponent> val)
        {
            componentList = val;
            return this;
        }

        public Builder orderProductExtraInfo(OrderProductExtraInfo val)
        {
            orderProductExtraInfo = val;
            return this;
        }
        public Builder timeSlaVoilates(String val){
            timeSlaVoilates=val;
            return this;
        }

        public OrdersProducts build()
        {
            return new OrdersProducts(this);
        }
    }

    @Override public String toString()
    {
        return "OrdersProducts{" + "orderProductId=" + orderProductId + ", orderId=" + orderId + ", productId="
            + productId + ", productName='" + productName + '\'' + ", productPrice=" + productPrice
            + ", productPrice_inr=" + productPrice_inr + ", productQuantity=" + productQuantity + ", productSize='"
            + productSize + '\'' + ", products_weight='" + products_weight + '\'' + ", products_code='" + products_code
            + '\'' + ", fkAssociateId='" + fkAssociateId + '\'' + ", orderShippingAssociatewise="
            + orderShippingAssociatewise + ", ordersProductStatus='" + ordersProductStatus + '\''
            + ", ordersAwbnumberAssociatewise='" + ordersAwbnumberAssociatewise + '\'' + ", ordersProductsCourierid="
            + ordersProductsCourierid + ", ordersProductsCancel_id='" + ordersProductsCancel_id + '\''
            + ", airBillWeight='" + airBillWeight + '\'' + ", dispatchDate=" + dispatchDate + ", payoutOnHold="
            + payoutOnHold + ", ordersProductsBaseCurrency=" + ordersProductsBaseCurrency
            + ", ordersProductsBaseCurrencyConversionRateInUsd=" + ordersProductsBaseCurrencyConversionRateInUsd
            + ", ordersProductsBaseCurrencyConversionRateInInr=" + ordersProductsBaseCurrencyConversionRateInInr
            + ", SpecialChargesShip=" + SpecialChargesShip + ", shippingTypeG='" + shippingTypeG + '\''
            + ", deliveryStatus=" + deliveryStatus + ", slaCode=" + slaCode + ", slaFlag=" + slaFlag + ", alertFlag="
            + alertFlag + ", productImage='" + productImage + '\'' + ", productUpdateDateTime=" + productUpdateDateTime
            + ", productNameForUrl='" + productNameForUrl + '\'' + ", vendorPrice=" + vendorPrice + ", personalized="
            + personalized + ", componentList=" + componentList + ", componentTotal=" + componentTotal
            + ", priceAdjustmentPerProduct=" + priceAdjustmentPerProduct + ", timeSlaVoilates='" + timeSlaVoilates
            + '\'' + ", orderProductExtraInfo=" + orderProductExtraInfo + '}';
    }
}
