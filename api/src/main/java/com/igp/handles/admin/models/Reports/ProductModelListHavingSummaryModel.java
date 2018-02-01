package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;

import java.util.List;

/**
 * Created by suditi on 30/1/18.
 */
public class ProductModelListHavingSummaryModel {
    @JsonProperty("tableData")
    private List<ProductTableDataModel> productTableDataModelList;

    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;

    public List<ProductTableDataModel> getProductTableDataModelList() {
        return productTableDataModelList;
    }

    public void setProductTableDataModelList(List<ProductTableDataModel> productTableDataModelList) {
        this.productTableDataModelList = productTableDataModelList;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
