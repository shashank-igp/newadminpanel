package com.igp.handles.models.Order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by shanky on 8/7/17.
 */
public class Order implements Serializable {

    private int					orderId					= -1;


    private long				customerId				= -1;


    private String				customersSalute			= null;


    private String				customersName			= null;


    private String				customersStreetAddress	= null;


    private String				customersStreetAddress2	= null;


    private String				customersCity			= null;


    private String				customersPostcode		= null;


    private String				customersDate			= null;


    private String				customersCountry		= null;


    private String				customerstelephone		= null;


    private String				customersEmail			= null;


    private String				customersMobile			= null;


    private String				deliverySalute			= null;


    private String				deliveryName			= null;


    private String				deliveryStreetAddress	= null;


    private String				deliveryCity			= null;


    private String				deliveryPostcode		= null;


    private String				deliveryState			= null;


    private String				deliveryCountry			= null;


    private String				deliveryEmail			= null;


    private String				deliveryMobile			= null;


    private Date lastModified = null;


    private Date				datePurchased			= null;


    private BigDecimal shippingCost = null;


    private BigDecimal			shippingCostInInr		= null;


    private BigDecimal			ordersProductDiscount	= null;


    private BigDecimal			ordersProductTotal		= null;


    private BigDecimal			ordersProductTotalInr	= null;


    private String				ordersStatus			= null;


    private String				commments				= null;


    private String				delivery_instruction	= null;


    private String				currency				= null;


    private BigDecimal			currencyValue			= null;


    private String				customersFax			= null;


    private int					ordersTempId			= -1;


    private Date				dateOfDelivery			= null;


    private String				bankTransactionId		= null;


    private String				bankAuthorisationCode	= null;


    private double				daysConversionFactor	= 0;


    private String				ordersIp				= null;


    private String				whenToDeliver			= null;


    private String				deliverytelephone		= null;


    private int					fkAssociateId			= -1;


    private int					ordersCancelId			= -1;


    private int					themeId					= -1;


    private int					ordersOccasionId		= -1;


    private int					ordersIsGenerated		= -1;


    private String				ordersPaySite			= null;


    private String				paypalStatus			= null;


    private String				paypalTrnsId			= null;


    private int					chk						= -1;


    private int					hold					= -1;


    private String				blueDartStatus			= null;


    private int					blueDartHold			= -1;


    private int					ureadCheck				= -1;


    private String				rlReqId					= null;


    private String				marketPlaceData			= null;


    private String				marketPlaceName			= null;


    private String					addressType										= null;



    private String					orderInstruction										= null;


    private double					vendorDeliveryCharge	= 0.0;


    private double					vendorOrderTotal		= 0.0;


    private String					deliverWhen				= "";

    private double componentTotal=0.0;

    private double priceAdjustment=0.0;

    private double orderNetProductPrice=0.0;

    private Map<String,List<String>> uploadedFilePath;

    public Map<String, List<String>> getUploadedFilePath()
    {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(Map<String, List<String>> uploadedFilePath)
    {
        this.uploadedFilePath = uploadedFilePath;
    }

    public double getOrderNetProductPrice()
    {
        return orderNetProductPrice;
    }

    public void setOrderNetProductPrice(double orderNetProductPrice)
    {
        this.orderNetProductPrice = orderNetProductPrice;
    }

    public double getPriceAdjustment()
    {
        return priceAdjustment;
    }

    public void setPriceAdjustment(double priceAdjustment)
    {
        this.priceAdjustment = priceAdjustment;
    }

    public double getComponentTotal()
    {
        return componentTotal;
    }

    public void setComponentTotal(double componentTotal)
    {
        this.componentTotal = componentTotal;
    }

    private List<OrdersProducts> orderProducts = new ArrayList<>();

    public int getOrderId()
    {
        return orderId;
    }

    public long getCustomerId()
    {
        return customerId;
    }

    public String getCustomersSalute()
    {
        return customersSalute;
    }

    public String getCustomersName()
    {
        return customersName;
    }

    public String getCustomersStreetAddress()
    {
        return customersStreetAddress;
    }

    public String getCustomersStreetAddress2()
    {
        return customersStreetAddress2;
    }

    public String getCustomersCity()
    {
        return customersCity;
    }

    public String getCustomersPostcode()
    {
        return customersPostcode;
    }

    public String getCustomersDate()
    {
        return customersDate;
    }

    public String getCustomersCountry()
    {
        return customersCountry;
    }

    public String getCustomerstelephone()
    {
        return customerstelephone;
    }

    public String getCustomersEmail()
    {
        return customersEmail;
    }

    public String getCustomersMobile()
    {
        return customersMobile;
    }

    public String getDeliverySalute()
    {
        return deliverySalute;
    }

    public String getDeliveryName()
    {
        return deliveryName;
    }

    public String getDeliveryStreetAddress()
    {
        return deliveryStreetAddress;
    }

    public String getDeliveryCity()
    {
        return deliveryCity;
    }

    public String getDeliveryPostcode()
    {
        return deliveryPostcode;
    }

    public String getDeliveryState()
    {
        return deliveryState;
    }

    public String getDeliveryCountry()
    {
        return deliveryCountry;
    }

    public String getDeliveryEmail()
    {
        return deliveryEmail;
    }

    public String getDeliveryMobile()
    {
        return deliveryMobile;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public Date getDatePurchased()
    {
        return datePurchased;
    }

    public BigDecimal getShippingCost()
    {
        return shippingCost;
    }

    public BigDecimal getShippingCostInInr()
    {
        return shippingCostInInr;
    }

    public BigDecimal getOrdersProductDiscount()
    {
        return ordersProductDiscount;
    }

    public BigDecimal getOrdersProductTotal()
    {
        return ordersProductTotal;
    }

    public BigDecimal getOrdersProductTotalInr()
    {
        return ordersProductTotalInr;
    }

    public String getOrdersStatus()
    {
        return ordersStatus;
    }

    public String getCommments()
    {
        return commments;
    }

    public String getDelivery_instruction()
    {
        return delivery_instruction;
    }

    public String getCurrency()
    {
        return currency;
    }

    public BigDecimal getCurrencyValue()
    {
        return currencyValue;
    }

    public String getCustomersFax()
    {
        return customersFax;
    }

    public int getOrdersTempId()
    {
        return ordersTempId;
    }

    public Date getDateOfDelivery()
    {
        return dateOfDelivery;
    }

    public String getBankTransactionId()
    {
        return bankTransactionId;
    }

    public String getBankAuthorisationCode()
    {
        return bankAuthorisationCode;
    }

    public double getDaysConversionFactor()
    {
        return daysConversionFactor;
    }

    public String getOrdersIp()
    {
        return ordersIp;
    }

    public String getWhenToDeliver()
    {
        return whenToDeliver;
    }

    public String getDeliverytelephone()
    {
        return deliverytelephone;
    }

    public int getFkAssociateId()
    {
        return fkAssociateId;
    }

    public int getOrdersCancelId()
    {
        return ordersCancelId;
    }

    public int getThemeId()
    {
        return themeId;
    }

    public int getOrdersOccasionId()
    {
        return ordersOccasionId;
    }

    public int getOrdersIsGenerated()
    {
        return ordersIsGenerated;
    }

    public String getOrdersPaySite()
    {
        return ordersPaySite;
    }

    public String getPaypalStatus()
    {
        return paypalStatus;
    }

    public String getPaypalTrnsId()
    {
        return paypalTrnsId;
    }

    public int getChk()
    {
        return chk;
    }

    public int getHold()
    {
        return hold;
    }

    public String getBlueDartStatus()
    {
        return blueDartStatus;
    }

    public int getBlueDartHold()
    {
        return blueDartHold;
    }

    public int getUreadCheck()
    {
        return ureadCheck;
    }

    public String getRlReqId()
    {
        return rlReqId;
    }

    public String getMarketPlaceData()
    {
        return marketPlaceData;
    }

    public String getMarketPlaceName()
    {
        return marketPlaceName;
    }

    public String getAddressType()
    {
        return addressType;
    }

    public String getOrderInstruction()
    {
        return orderInstruction;
    }

    public double getVendorDeliveryCharge()
    {
        return vendorDeliveryCharge;
    }

    public double getVendorOrderTotal()
    {
        return vendorOrderTotal;
    }

    public String getDeliverWhen()
    {
        return deliverWhen;
    }

    public List<OrdersProducts> getOrderProducts()
    {
        return orderProducts;
    }

    public void addOrderProducts(OrdersProducts orderProducts)
    {
        this.orderProducts.add(orderProducts);
    }

    public void setVendorDeliveryCharge(double vendorDeliveryCharge)
    {
        this.vendorDeliveryCharge = vendorDeliveryCharge;
    }

    public void setDeliverWhen(String deliverWhen)
    {
        this.deliverWhen = deliverWhen;
    }

    public void setVendorOrderTotal(double vendorOrderTotal)
    {
        this.vendorOrderTotal = vendorOrderTotal;
    }

    private Order(Builder builder)
    {
        orderId = builder.orderId;
        customerId = builder.customerId;
        customersSalute = builder.customersSalute;
        customersName = builder.customersName;
        customersStreetAddress = builder.customersStreetAddress;
        customersStreetAddress2 = builder.customersStreetAddress2;
        customersCity = builder.customersCity;
        customersPostcode = builder.customersPostcode;
        customersDate = builder.customersDate;
        customersCountry = builder.customersCountry;
        customerstelephone = builder.customerstelephone;
        customersEmail = builder.customersEmail;
        customersMobile = builder.customersMobile;
        deliverySalute = builder.deliverySalute;
        deliveryName = builder.deliveryName;
        deliveryStreetAddress = builder.deliveryStreetAddress;
        deliveryCity = builder.deliveryCity;
        deliveryPostcode = builder.deliveryPostcode;
        deliveryState = builder.deliveryState;
        deliveryCountry = builder.deliveryCountry;
        deliveryEmail = builder.deliveryEmail;
        deliveryMobile = builder.deliveryMobile;
        lastModified = builder.lastModified;
        datePurchased = builder.datePurchased;
        shippingCost = builder.shippingCost;
        shippingCostInInr = builder.shippingCostInInr;
        ordersProductDiscount = builder.ordersProductDiscount;
        ordersProductTotal = builder.ordersProductTotal;
        ordersProductTotalInr = builder.ordersProductTotalInr;
        ordersStatus = builder.ordersStatus;
        commments = builder.commments;
        delivery_instruction = builder.delivery_instruction;
        currency = builder.currency;
        currencyValue = builder.currencyValue;
        customersFax = builder.customersFax;
        ordersTempId = builder.ordersTempId;
        dateOfDelivery = builder.dateOfDelivery;
        bankTransactionId = builder.bankTransactionId;
        bankAuthorisationCode = builder.bankAuthorisationCode;
        daysConversionFactor = builder.daysConversionFactor;
        ordersIp = builder.ordersIp;
        whenToDeliver = builder.whenToDeliver;
        deliverytelephone = builder.deliverytelephone;
        fkAssociateId = builder.fkAssociateId;
        ordersCancelId = builder.ordersCancelId;
        themeId = builder.themeId;
        ordersOccasionId = builder.ordersOccasionId;
        ordersIsGenerated = builder.ordersIsGenerated;
        ordersPaySite = builder.ordersPaySite;
        paypalStatus = builder.paypalStatus;
        paypalTrnsId = builder.paypalTrnsId;
        chk = builder.chk;
        hold = builder.hold;
        blueDartStatus = builder.blueDartStatus;
        blueDartHold = builder.blueDartHold;
        ureadCheck = builder.ureadCheck;
        rlReqId = builder.rlReqId;
        marketPlaceData = builder.marketPlaceData;
        marketPlaceName = builder.marketPlaceName;
        addressType = builder.addressType;
        orderInstruction = builder.orderInstruction;
        vendorDeliveryCharge = builder.vendorDeliveryCharge;
        vendorOrderTotal = builder.vendorOrderTotal;
        deliverWhen = builder.deliverWhen;
        orderProducts = builder.orderProducts;
    }

    public static final class Builder
    {
        private int                  orderId;
        private long                 customerId;
        private String               customersSalute;
        private String               customersName;
        private String               customersStreetAddress;
        private String               customersStreetAddress2;
        private String               customersCity;
        private String               customersPostcode;
        private String               customersDate;
        private String               customersCountry;
        private String               customerstelephone;
        private String               customersEmail;
        private String               customersMobile;
        private String               deliverySalute;
        private String               deliveryName;
        private String               deliveryStreetAddress;
        private String               deliveryCity;
        private String               deliveryPostcode;
        private String               deliveryState;
        private String               deliveryCountry;
        private String               deliveryEmail;
        private String               deliveryMobile;
        private Date                 lastModified;
        private Date                 datePurchased;
        private BigDecimal           shippingCost;
        private BigDecimal           shippingCostInInr;
        private BigDecimal           ordersProductDiscount;
        private BigDecimal           ordersProductTotal;
        private BigDecimal           ordersProductTotalInr;
        private String               ordersStatus;
        private String               commments;
        private String               delivery_instruction;
        private String               currency;
        private BigDecimal           currencyValue;
        private String               customersFax;
        private int                  ordersTempId;
        private Date                 dateOfDelivery;
        private String               bankTransactionId;
        private String               bankAuthorisationCode;
        private double               daysConversionFactor;
        private String               ordersIp;
        private String               whenToDeliver;
        private String               deliverytelephone;
        private int                  fkAssociateId;
        private int                  ordersCancelId;
        private int                  themeId;
        private int                  ordersOccasionId;
        private int                  ordersIsGenerated;
        private String               ordersPaySite;
        private String               paypalStatus;
        private String               paypalTrnsId;
        private int                  chk;
        private int                  hold;
        private String               blueDartStatus;
        private int                  blueDartHold;
        private int                  ureadCheck;
        private String               rlReqId;
        private String               marketPlaceData;
        private String               marketPlaceName;
        private String               addressType;
        private String               orderInstruction;
        private double               vendorDeliveryCharge;
        private double               vendorOrderTotal;
        private String               deliverWhen;
        private List<OrdersProducts> orderProducts;

        public Builder()
        {
        }

        public Builder orderId(int val)
        {
            orderId = val;
            return this;
        }

        public Builder customerId(long val)
        {
            customerId = val;
            return this;
        }

        public Builder customersSalute(String val)
        {
            customersSalute = val;
            return this;
        }

        public Builder customersName(String val)
        {
            customersName = val;
            return this;
        }

        public Builder customersStreetAddress(String val)
        {
            customersStreetAddress = val;
            return this;
        }

        public Builder customersStreetAddress2(String val)
        {
            customersStreetAddress2 = val;
            return this;
        }

        public Builder customersCity(String val)
        {
            customersCity = val;
            return this;
        }

        public Builder customersPostcode(String val)
        {
            customersPostcode = val;
            return this;
        }

        public Builder customersDate(String val)
        {
            customersDate = val;
            return this;
        }

        public Builder customersCountry(String val)
        {
            customersCountry = val;
            return this;
        }

        public Builder customerstelephone(String val)
        {
            customerstelephone = val;
            return this;
        }

        public Builder customersEmail(String val)
        {
            customersEmail = val;
            return this;
        }

        public Builder customersMobile(String val)
        {
            customersMobile = val;
            return this;
        }

        public Builder deliverySalute(String val)
        {
            deliverySalute = val;
            return this;
        }

        public Builder deliveryName(String val)
        {
            deliveryName = val;
            return this;
        }

        public Builder deliveryStreetAddress(String val)
        {
            deliveryStreetAddress = val;
            return this;
        }

        public Builder deliveryCity(String val)
        {
            deliveryCity = val;
            return this;
        }

        public Builder deliveryPostcode(String val)
        {
            deliveryPostcode = val;
            return this;
        }

        public Builder deliveryState(String val)
        {
            deliveryState = val;
            return this;
        }

        public Builder deliveryCountry(String val)
        {
            deliveryCountry = val;
            return this;
        }

        public Builder deliveryEmail(String val)
        {
            deliveryEmail = val;
            return this;
        }

        public Builder deliveryMobile(String val)
        {
            deliveryMobile = val;
            return this;
        }

        public Builder lastModified(Date val)
        {
            lastModified = val;
            return this;
        }

        public Builder datePurchased(Date val)
        {
            datePurchased = val;
            return this;
        }

        public Builder shippingCost(BigDecimal val)
        {
            shippingCost = val;
            return this;
        }

        public Builder shippingCostInInr(BigDecimal val)
        {
            shippingCostInInr = val;
            return this;
        }

        public Builder ordersProductDiscount(BigDecimal val)
        {
            ordersProductDiscount = val;
            return this;
        }

        public Builder ordersProductTotal(BigDecimal val)
        {
            ordersProductTotal = val;
            return this;
        }

        public Builder ordersProductTotalInr(BigDecimal val)
        {
            ordersProductTotalInr = val;
            return this;
        }

        public Builder ordersStatus(String val)
        {
            ordersStatus = val;
            return this;
        }

        public Builder commments(String val)
        {
            commments = val;
            return this;
        }

        public Builder delivery_instruction(String val)
        {
            delivery_instruction = val;
            return this;
        }

        public Builder currency(String val)
        {
            currency = val;
            return this;
        }

        public Builder currencyValue(BigDecimal val)
        {
            currencyValue = val;
            return this;
        }

        public Builder customersFax(String val)
        {
            customersFax = val;
            return this;
        }

        public Builder ordersTempId(int val)
        {
            ordersTempId = val;
            return this;
        }

        public Builder dateOfDelivery(Date val)
        {
            dateOfDelivery = val;
            return this;
        }

        public Builder bankTransactionId(String val)
        {
            bankTransactionId = val;
            return this;
        }

        public Builder bankAuthorisationCode(String val)
        {
            bankAuthorisationCode = val;
            return this;
        }

        public Builder daysConversionFactor(double val)
        {
            daysConversionFactor = val;
            return this;
        }

        public Builder ordersIp(String val)
        {
            ordersIp = val;
            return this;
        }

        public Builder whenToDeliver(String val)
        {
            whenToDeliver = val;
            return this;
        }

        public Builder deliverytelephone(String val)
        {
            deliverytelephone = val;
            return this;
        }

        public Builder fkAssociateId(int val)
        {
            fkAssociateId = val;
            return this;
        }

        public Builder ordersCancelId(int val)
        {
            ordersCancelId = val;
            return this;
        }

        public Builder themeId(int val)
        {
            themeId = val;
            return this;
        }

        public Builder ordersOccasionId(int val)
        {
            ordersOccasionId = val;
            return this;
        }

        public Builder ordersIsGenerated(int val)
        {
            ordersIsGenerated = val;
            return this;
        }

        public Builder ordersPaySite(String val)
        {
            ordersPaySite = val;
            return this;
        }

        public Builder paypalStatus(String val)
        {
            paypalStatus = val;
            return this;
        }

        public Builder paypalTrnsId(String val)
        {
            paypalTrnsId = val;
            return this;
        }

        public Builder chk(int val)
        {
            chk = val;
            return this;
        }

        public Builder hold(int val)
        {
            hold = val;
            return this;
        }

        public Builder blueDartStatus(String val)
        {
            blueDartStatus = val;
            return this;
        }

        public Builder blueDartHold(int val)
        {
            blueDartHold = val;
            return this;
        }

        public Builder ureadCheck(int val)
        {
            ureadCheck = val;
            return this;
        }

        public Builder rlReqId(String val)
        {
            rlReqId = val;
            return this;
        }

        public Builder marketPlaceData(String val)
        {
            marketPlaceData = val;
            return this;
        }

        public Builder marketPlaceName(String val)
        {
            marketPlaceName = val;
            return this;
        }

        public Builder addressType(String val)
        {
            addressType = val;
            return this;
        }

        public Builder orderInstruction(String val)
        {
            orderInstruction = val;
            return this;
        }

        public Builder vendorDeliveryCharge(double val)
        {
            vendorDeliveryCharge = val;
            return this;
        }

        public Builder vendorOrderTotal(double val)
        {
            vendorOrderTotal = val;
            return this;
        }

        public Builder deliverWhen(String val)
        {
            deliverWhen = val;
            return this;
        }

        public Builder orderProducts(List<OrdersProducts> val)
        {
            orderProducts = val;
            return this;
        }

        public Order build()
        {
            return new Order(this);
        }
    }

    @Override public String toString()
    {
        return "Order{" + "orderId=" + orderId + ", customerId=" + customerId + ", customersSalute='" + customersSalute
            + '\'' + ", customersName='" + customersName + '\'' + ", customersStreetAddress='" + customersStreetAddress
            + '\'' + ", customersStreetAddress2='" + customersStreetAddress2 + '\'' + ", customersCity='"
            + customersCity + '\'' + ", customersPostcode='" + customersPostcode + '\'' + ", customersDate='"
            + customersDate + '\'' + ", customersCountry='" + customersCountry + '\'' + ", customerstelephone='"
            + customerstelephone + '\'' + ", customersEmail='" + customersEmail + '\'' + ", customersMobile='"
            + customersMobile + '\'' + ", deliverySalute='" + deliverySalute + '\'' + ", deliveryName='" + deliveryName
            + '\'' + ", deliveryStreetAddress='" + deliveryStreetAddress + '\'' + ", deliveryCity='" + deliveryCity
            + '\'' + ", deliveryPostcode='" + deliveryPostcode + '\'' + ", deliveryState='" + deliveryState + '\''
            + ", deliveryCountry='" + deliveryCountry + '\'' + ", deliveryEmail='" + deliveryEmail + '\''
            + ", deliveryMobile='" + deliveryMobile + '\'' + ", lastModified=" + lastModified + ", datePurchased="
            + datePurchased + ", shippingCost=" + shippingCost + ", shippingCostInInr=" + shippingCostInInr
            + ", ordersProductDiscount=" + ordersProductDiscount + ", ordersProductTotal=" + ordersProductTotal
            + ", ordersProductTotalInr=" + ordersProductTotalInr + ", ordersStatus='" + ordersStatus + '\''
            + ", commments='" + commments + '\'' + ", delivery_instruction='" + delivery_instruction + '\''
            + ", currency='" + currency + '\'' + ", currencyValue=" + currencyValue + ", customersFax='" + customersFax
            + '\'' + ", ordersTempId=" + ordersTempId + ", dateOfDelivery=" + dateOfDelivery + ", bankTransactionId='"
            + bankTransactionId + '\'' + ", bankAuthorisationCode='" + bankAuthorisationCode + '\''
            + ", daysConversionFactor=" + daysConversionFactor + ", ordersIp='" + ordersIp + '\'' + ", whenToDeliver='"
            + whenToDeliver + '\'' + ", deliverytelephone='" + deliverytelephone + '\'' + ", fkAssociateId="
            + fkAssociateId + ", ordersCancelId=" + ordersCancelId + ", themeId=" + themeId + ", ordersOccasionId="
            + ordersOccasionId + ", ordersIsGenerated=" + ordersIsGenerated + ", ordersPaySite='" + ordersPaySite + '\''
            + ", paypalStatus='" + paypalStatus + '\'' + ", paypalTrnsId='" + paypalTrnsId + '\'' + ", chk=" + chk
            + ", hold=" + hold + ", blueDartStatus='" + blueDartStatus + '\'' + ", blueDartHold=" + blueDartHold
            + ", ureadCheck=" + ureadCheck + ", rlReqId='" + rlReqId + '\'' + ", marketPlaceData='" + marketPlaceData
            + '\'' + ", marketPlaceName='" + marketPlaceName + '\'' + ", addressType='" + addressType + '\''
            + ", orderInstruction='" + orderInstruction + '\'' + ", vendorDeliveryCharge=" + vendorDeliveryCharge
            + ", vendorOrderTotal=" + vendorOrderTotal + ", deliverWhen='" + deliverWhen + '\'' + ", componentTotal="
            + componentTotal + ", priceAdjustment=" + priceAdjustment + ", orderNetProductPrice=" + orderNetProductPrice
            + ", uploadedFilePath=" + uploadedFilePath + ", orderProducts=" + orderProducts + '}';
    }
}
