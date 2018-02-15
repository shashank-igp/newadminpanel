package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 15/2/18.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BarcodeToComponentModel {
    @JsonProperty("Product_Code")
    private String productCode;

    @JsonProperty("Product_Id")
    private String productId;

    @JsonProperty("Product_Name")
    private String productName;

    @JsonProperty("Product_Image")
    private String productImage;

    @JsonProperty("Component_Code")
    private String componentCode;

    @JsonProperty("Component_Name")
    private String componentName;

    @JsonProperty("Quantity")
    private String quantity;

    @JsonProperty("Component_Image")
    private String componentImage;

    @JsonProperty("Component_Id")
    private int componentId;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getComponentImage() {
        return componentImage;
    }

    public void setComponentImage(String componentImage) {
        this.componentImage = componentImage;
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    private BarcodeToComponentModel(Builder builder) {
        productCode = builder.productCode;
        productId = builder.productId;
        productName = builder.productName;
        productImage = builder.productImage;
        componentCode = builder.componentCode;
        componentName = builder.componentName;
        quantity = builder.quantity;
        componentImage = builder.componentImage;
        componentId = builder.componentId;
    }


    public static final class Builder {
        private String productCode;
        private String productId;
        private String productName;
        private String productImage;
        private String componentCode;
        private String componentName;
        private String quantity;
        private String componentImage;
        private int componentId;

        public Builder() {
        }

        public Builder productCode(String val) {
            productCode = val;
            return this;
        }

        public Builder productId(String val) {
            productId = val;
            return this;
        }

        public Builder productName(String val) {
            productName = val;
            return this;
        }

        public Builder productImage(String val) {
            productImage = val;
            return this;
        }

        public Builder componentCode(String val) {
            componentCode = val;
            return this;
        }

        public Builder componentName(String val) {
            componentName = val;
            return this;
        }

        public Builder quantity(String val) {
            quantity = val;
            return this;
        }

        public Builder componentImage(String val) {
            componentImage = val;
            return this;
        }

        public Builder componentId(int val) {
            componentId = val;
            return this;
        }

        public BarcodeToComponentModel build() {
            return new BarcodeToComponentModel(this);
        }
    }
}
