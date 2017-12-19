package com.igp.handles.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.igp.handles.models.Report.SummaryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shanky on 21/9/17.
 */
@JsonDeserialize
@JsonSerialize
public class ReportResponse {
    @JsonProperty("summary")
    private List<SummaryModel> summary;

    @JsonProperty("tableHeaders")
    private String[] tableHeaders;

    @JsonProperty("tableData")
    private List<Object> tableData;

    @JsonProperty("tableDataAction")
    private List<Map.Entry<String,List<String>>> tableDataAction=new ArrayList<>();

    public List<Map.Entry<String, List<String>>> getTableDataAction()
    {
        return tableDataAction;
    }

    public void setTableDataAction(List<Map.Entry<String, List<String>>> tableDataAction)
    {
        this.tableDataAction = tableDataAction;
    }

    public List<SummaryModel> getSummary()
    {
        return summary;
    }

    public void setSummary(List<SummaryModel> summary)
    {
        this.summary = summary;
    }

    public String[] getTableHeaders()
    {
        return tableHeaders;
    }

    public List<Object> getTableData() {
        return tableData;
    }

    public void setTableData(List<Object> tableData) {
        this.tableData = tableData;
    }

    public void setTableHeaders(String[] tableHeaders)
    {
        this.tableHeaders = tableHeaders;
    }



}
