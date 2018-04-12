package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;

import java.util.List;

/**
 * Created by shanky on 12/4/18.
 */
public class SlaReportWithSummary {

    @JsonProperty("tableData")
    private List<SlaReportModel> slaReportModelList;

    @JsonProperty("tableSummary")
    private List<SummaryModel> summaryModelList;

    public List<SlaReportModel> getSlaReportModelList()
    {
        return slaReportModelList;
    }

    public void setSlaReportModelList(List<SlaReportModel> slaReportModelList)
    {
        this.slaReportModelList = slaReportModelList;
    }

    public List<SummaryModel> getSummaryModelList()
    {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList)
    {
        this.summaryModelList = summaryModelList;
    }
}
