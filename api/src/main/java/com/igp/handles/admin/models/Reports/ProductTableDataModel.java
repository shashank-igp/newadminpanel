package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Created by suditi on 30/1/18.
 */
public class ProductTableDataModel {
    @JsonIgnore
    private String associateId;

    private String Component_Id_Hide;

    @JsonProperty("Component Image")
    private String componentImage;

    @JsonProperty("Component_Name")
    private String componentName;

    @JsonProperty("Price")
    private TableDataActionHandels  price;

    @JsonProperty("InStock")
    private TableDataActionHandels inStock;

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public String getComponent_Id_Hide() {
        return Component_Id_Hide;
    }

    public void setComponent_Id_Hide(String component_Id_Hide) {
        Component_Id_Hide = component_Id_Hide;
    }

    public String getComponentImage() {
        return componentImage;
    }

    public void setComponentImage(String componentImage) {
        this.componentImage = componentImage;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public TableDataActionHandels getPrice() {    return price;    }

    public void setPrice(TableDataActionHandels price) {
        this.price = price;
    }

    public TableDataActionHandels getInStock() {
        return inStock;
    }

    public void setInStock(TableDataActionHandels inStock) {
        this.inStock = inStock;
    }

}
