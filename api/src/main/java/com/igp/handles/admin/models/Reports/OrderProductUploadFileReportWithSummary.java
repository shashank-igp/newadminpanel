package com.igp.handles.admin.models.Reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.igp.handles.vendorpanel.models.Report.SummaryModel;

import java.util.List;

/**
 * Created by shanky on 27/2/18.
 */
public class OrderProductUploadFileReportWithSummary
{
    @JsonProperty("tableData")
    private List<OrderProductUploadFileModel> orderProductUploadFileModelList;


    @JsonProperty("tableSummary")
    private  List<SummaryModel> summaryModelList;

    public List<OrderProductUploadFileModel> getOrderProductUploadFileModelList()
    {
        return orderProductUploadFileModelList;
    }

    public void setOrderProductUploadFileModelList(List<OrderProductUploadFileModel> orderProductUploadFileModelList)
    {
        this.orderProductUploadFileModelList = orderProductUploadFileModelList;
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
