package com.igp.handles.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by shal on 30/9/17.
 */
public class PincodeModelListWithSummary {

    @JsonProperty("tableData")
    private List<PincodeReportModel>  pincodeReportModelList;


    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;


    public List<PincodeReportModel> getPincodeReportModelList() {
        return pincodeReportModelList;
    }

    public void setPincodeReportModelList(List<PincodeReportModel> pincodeReportModelList) {
        this.pincodeReportModelList = pincodeReportModelList;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
