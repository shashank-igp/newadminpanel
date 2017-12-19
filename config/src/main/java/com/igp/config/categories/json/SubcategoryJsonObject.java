package com.igp.config.categories.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by pranaya on 08/08/16.
 */
public class SubcategoryJsonObject {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("prod_type")
    private List<ProductTypeJsonObject> productType;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public List<ProductTypeJsonObject> getProductType() {
        return productType;
    }
}
