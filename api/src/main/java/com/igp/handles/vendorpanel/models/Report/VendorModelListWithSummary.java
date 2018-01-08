package com.igp.handles.vendorpanel.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by shal on 30/9/17.
 */
public class VendorModelListWithSummary {

    @JsonProperty("tableData")
    private List<VendorReportObject> vendorReportObjectList;


    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;


    public List<VendorReportObject> getVendorReportObjectList() {
        return vendorReportObjectList;
    }

    public void setVendorReportObjectList(List<VendorReportObject> vendorReportObjectList) {
        this.vendorReportObjectList = vendorReportObjectList;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
