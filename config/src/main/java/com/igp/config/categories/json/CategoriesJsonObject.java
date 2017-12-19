package com.igp.config.categories.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by pranaya on 08/08/16.
 */
public class CategoriesJsonObject {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("subcat")
    private List<SubcategoryJsonObject> subcat;
    @JsonProperty("type")
    private Integer type;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public List<SubcategoryJsonObject> getSubcat() {
        return subcat;
    }
    
    public Integer getType() {
        return type;
    }
}
