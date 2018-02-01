package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;

import java.util.List;

/**
 * Created by suditi on 30/1/18.
 */
public class PincodeModelListHavingSummaryModel {
    @JsonProperty("tableData")
    private List<PincodeTableDataModel> pincodeTableDataModelList;

    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;

    public List<PincodeTableDataModel> getPincodeTableDataModelList() {
        return pincodeTableDataModelList;
    }

    public void setPincodeTableDataModelList(List<PincodeTableDataModel> pincodeTableDataModelList) {
        this.pincodeTableDataModelList = pincodeTableDataModelList;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
