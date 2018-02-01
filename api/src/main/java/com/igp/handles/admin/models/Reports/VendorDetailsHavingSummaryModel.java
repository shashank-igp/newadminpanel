package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igp.handles.admin.models.Vendor.VendorInfoModel;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;

import java.util.List;

/**
 * Created by suditi on 31/1/18.
 */
public class VendorDetailsHavingSummaryModel {
    @JsonProperty("tableData")
    private List<VendorInfoModel> vendorDetailsModels;

    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;

    public List<VendorInfoModel> getVendorDetailsModels() {
        return vendorDetailsModels;
    }

    public void setVendorDetailsModels(List<VendorInfoModel> vendorDetailsModels) {
        this.vendorDetailsModels = vendorDetailsModels;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
