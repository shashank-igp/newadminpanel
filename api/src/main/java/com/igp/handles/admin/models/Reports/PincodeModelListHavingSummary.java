package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;

import java.util.List;

/**
 * Created by suditi on 30/1/18.
 */
public class PincodeModelListHavingSummary {
    @JsonProperty("tableData")
    private List<PincodeTableData> pincodeTableDataList;

    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;

    public List<PincodeTableData> getPincodeTableDataList() {
        return pincodeTableDataList;
    }

    public void setPincodeTableDataList(List<PincodeTableData> pincodeTableDataList) {
        this.pincodeTableDataList = pincodeTableDataList;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
