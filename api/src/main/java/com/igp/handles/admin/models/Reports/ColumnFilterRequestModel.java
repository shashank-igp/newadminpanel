package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 19/2/18.
 */
public class ColumnFilterRequestModel {
    @JsonProperty("filterBy")
    private String filterBy;

    @JsonProperty("filterValue")
    private String filterValue;

    @JsonProperty("colName")
    private String colName;

    @JsonProperty("colDataType")
    private String colDataType;

    public String getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    public String getFilterValue() { return filterValue;}

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColDataType() {
        return colDataType;
    }

    public void setColDataType(String colDataType) {
        this.colDataType = colDataType;
    }
}
