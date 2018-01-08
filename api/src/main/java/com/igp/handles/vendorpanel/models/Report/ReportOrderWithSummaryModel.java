package com.igp.handles.vendorpanel.models.Report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by shal on 22/9/17.
 */
public class ReportOrderWithSummaryModel {

    @JsonProperty("tableData")
    private   List <orderReportObjectModel>  orderReportObjectModelList;


    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;


    @JsonIgnore
    private Double totalAmountSummary   ;

    @JsonIgnore
    private Integer totalNumber;


    public List<orderReportObjectModel> getOrderReportObjectModelList() {
        return orderReportObjectModelList;
    }

    public void setOrderReportObjectModelList(List<orderReportObjectModel> orderReportObjectModelList) {
        this.orderReportObjectModelList = orderReportObjectModelList;
    }

    public Double getTotalAmountSummary() {
        return totalAmountSummary;
    }

    public void setTotalAmountSummary(Double totalAmountSummary) {
        this.totalAmountSummary = totalAmountSummary;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
