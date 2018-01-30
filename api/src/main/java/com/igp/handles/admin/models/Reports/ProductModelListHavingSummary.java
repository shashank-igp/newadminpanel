package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;

import java.util.List;

/**
 * Created by suditi on 30/1/18.
 */
public class ProductModelListHavingSummary {
    @JsonProperty("tableData")
    private List<ProductTableData> productTableDataList;

    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;

    public List<ProductTableData> getProductTableDataList() {
        return productTableDataList;
    }

    public void setProductTableDataList(List<ProductTableData> productTableDataList) {
        this.productTableDataList = productTableDataList;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
