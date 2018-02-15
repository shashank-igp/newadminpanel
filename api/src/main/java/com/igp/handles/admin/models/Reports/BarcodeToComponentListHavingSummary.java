package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igp.handles.vendorpanel.models.Order.OrderComponent;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;

import java.util.List;

/**
 * Created by suditi on 13/2/18.
 */
public class BarcodeToComponentListHavingSummary {
    @JsonProperty("tableData")
    private List<BarcodeToComponentModel> barcodeToComponentDataModelList;

    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;

    public List<BarcodeToComponentModel> getBarcodeToComponentDataModelList() {
        return barcodeToComponentDataModelList;
    }

    public void setBarcodeToComponentDataModelList(List<BarcodeToComponentModel> barcodeToComponentDataModelList) {
        this.barcodeToComponentDataModelList = barcodeToComponentDataModelList;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
