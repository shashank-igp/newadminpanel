package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by suditi on 15/2/18.
 */
public class BarcodeReportResponseModel {
    @JsonProperty("list")
    private List<String> list;

    @JsonProperty("count")
    private int count;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
