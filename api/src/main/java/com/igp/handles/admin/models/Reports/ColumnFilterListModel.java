package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import kafka.api.LeaderAndIsr;

import java.util.List;

/**
 * Created by suditi on 19/2/18.
 */
public class ColumnFilterListModel {
    @JsonProperty("colFilterList")
    private List<ColumnFilterRequestModel> colFilterList;

    public List<ColumnFilterRequestModel> getColFilterList() {
        return colFilterList;
    }

    public void setColFilterList(List<ColumnFilterRequestModel> colFilterList) {
        this.colFilterList = colFilterList;
    }
}
