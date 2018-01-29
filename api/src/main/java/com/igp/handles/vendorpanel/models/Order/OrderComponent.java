package com.igp.handles.vendorpanel.models.Order;

import java.io.Serializable;

/**
 * Created by shanky on 8/8/17.
 */
public class OrderComponent implements Serializable{

    private String				productId			= null;
    private String				componentCode		= null;
    private String				componentName		= null;
    private String				type				= null;
    private String				quantity			= null;
    private String				componentImage		= null;
    private String				componentPrice		= "N/A";
    private boolean				eggless				= false;
    private String              timestamp           =null;
    private int                 componentId         =0;

    public String getTimestamp()
    {
        return timestamp;
    }

    public String getProductId()
    {
        return productId;
    }

    public String getComponentCode()
    {
        return componentCode;
    }

    public String getComponentName()
    {
        return componentName;
    }

    public String getType()
    {
        return type;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public String getComponentImage()
    {
        return componentImage;
    }

    public String getComponentPrice()
    {
        return componentPrice;
    }

    public boolean isEggless()
    {
        return eggless;
    }

    public void setEggless(boolean eggless)
    {
        this.eggless = eggless;
    }

    public void setComponentPrice(String componentPrice)
    {
        this.componentPrice = componentPrice;
    }

    public int getComponentId()
    {
        return componentId;
    }

    private OrderComponent(Builder builder)
    {
        productId = builder.productId;
        componentCode = builder.componentCode;
        componentName = builder.componentName;
        type = builder.type;
        quantity = builder.quantity;
        componentImage = builder.componentImage;
        componentPrice = builder.componentPrice;
        eggless = builder.eggless;
        timestamp=builder.timestamp;
        componentId=builder.componentId;
    }

    public static final class Builder
    {
        private String  productId;
        private String  componentCode;
        private String  componentName;
        private String  type;
        private String  quantity;
        private String  componentImage;
        private String  componentPrice;
        private boolean eggless;
        private String  timestamp;
        private int     componentId;

        public Builder()
        {
        }

        public Builder productId(String val)
        {
            productId = val;
            return this;
        }

        public Builder componentCode(String val)
        {
            componentCode = val;
            return this;
        }

        public Builder componentName(String val)
        {
            componentName = val;
            return this;
        }

        public Builder type(String val)
        {
            type = val;
            return this;
        }

        public Builder quantity(String val)
        {
            quantity = val;
            return this;
        }

        public Builder componentImage(String val)
        {
            componentImage = val;
            return this;
        }

        public Builder componentPrice(String val)
        {
            componentPrice = val;
            return this;
        }

        public Builder eggless(boolean val)
        {
            eggless = val;
            return this;
        }
        public Builder timestamp(String val)
        {
            timestamp = val;
            return this;
        }

        public Builder componentId(int val)
        {
            componentId = val;
            return this;
        }

        public OrderComponent build()
        {
            return new OrderComponent(this);
        }
    }
}
