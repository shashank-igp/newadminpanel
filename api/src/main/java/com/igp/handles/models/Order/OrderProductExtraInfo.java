package com.igp.handles.models.Order;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shanky on 8/8/17.
 */
public class OrderProductExtraInfo implements Serializable {

    private int					id					= -1;


    private int					orderId				= -1;


    private int					orderProductId		= -1;


    private int					productId			= -1;


    private int					quantity			= -1;


    private String				attributes			= null;


    private int					giftBox				= -1;


    private int					deliveryType		= -1;


    private Date deliveryDate = null;


    private String				deliveryTime		= null;


    private long				productCostPrice	= -1;

    public int getId()
    {
        return id;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public int getOrderProductId()
    {
        return orderProductId;
    }

    public int getProductId()
    {
        return productId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getAttributes()
    {
        return attributes;
    }

    public int getGiftBox()
    {
        return giftBox;
    }

    public int getDeliveryType()
    {
        return deliveryType;
    }

    public Date getDeliveryDate()
    {
        return deliveryDate;
    }

    public String getDeliveryTime()
    {
        return deliveryTime;
    }

    public long getProductCostPrice()
    {
        return productCostPrice;
    }

    public void setDeliveryTime(String deliveryTime)
    {
        this.deliveryTime = deliveryTime;
    }

    private OrderProductExtraInfo(Builder builder)
    {
        id = builder.id;
        orderId = builder.orderId;
        orderProductId = builder.orderProductId;
        productId = builder.productId;
        quantity = builder.quantity;
        attributes = builder.attributes;
        giftBox = builder.giftBox;
        deliveryType = builder.deliveryType;
        deliveryDate = builder.deliveryDate;
        deliveryTime = builder.deliveryTime;
        productCostPrice = builder.productCostPrice;
    }

    public static final class Builder
    {
        private int    id;
        private int    orderId;
        private int    orderProductId;
        private int    productId;
        private int    quantity;
        private String attributes;
        private int    giftBox;
        private int    deliveryType;
        private Date   deliveryDate;
        private String deliveryTime;
        private long   productCostPrice;

        public Builder()
        {
        }

        public Builder id(int val)
        {
            id = val;
            return this;
        }

        public Builder orderId(int val)
        {
            orderId = val;
            return this;
        }

        public Builder orderProductId(int val)
        {
            orderProductId = val;
            return this;
        }

        public Builder productId(int val)
        {
            productId = val;
            return this;
        }

        public Builder quantity(int val)
        {
            quantity = val;
            return this;
        }

        public Builder attributes(String val)
        {
            attributes = val;
            return this;
        }

        public Builder giftBox(int val)
        {
            giftBox = val;
            return this;
        }

        public Builder deliveryType(int val)
        {
            deliveryType = val;
            return this;
        }

        public Builder deliveryDate(Date val)
        {
            deliveryDate = val;
            return this;
        }

        public Builder deliveryTime(String val)
        {
            deliveryTime = val;
            return this;
        }

        public Builder productCostPrice(long val)
        {
            productCostPrice = val;
            return this;
        }

        public OrderProductExtraInfo build()
        {
            return new OrderProductExtraInfo(this);
        }
    }

    @Override public String toString()
    {
        return "OrderProductExtraInfo{" + "id=" + id + ", orderId=" + orderId + ", orderProductId=" + orderProductId
            + ", productId=" + productId + ", quantity=" + quantity + ", attributes='" + attributes + '\''
            + ", giftBox=" + giftBox + ", deliveryType=" + deliveryType + ", deliveryDate=" + deliveryDate
            + ", deliveryTime='" + deliveryTime + '\'' + ", productCostPrice=" + productCostPrice + '}';
    }
}
