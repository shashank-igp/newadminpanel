package com.igp.handles.vendorpanel.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by shanky on 27/12/17.
 */
public class PayoutAndTaxReportSummaryModel {
    @JsonProperty("tableData")
    private List<OrderTaxReport> orderTaxReportList;

    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;

    public List<OrderTaxReport> getOrderTaxReportList()
    {
        return orderTaxReportList;
    }

    public void setOrderTaxReportList(List<OrderTaxReport> orderTaxReportList)
    {
        this.orderTaxReportList = orderTaxReportList;
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
