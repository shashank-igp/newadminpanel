package com.igp.admin.Voucher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RowLimitModel
{
    @JsonProperty("startIndex")
    int startIndex;

    @JsonProperty("rowsCount")
    int rowsCount;

    public int getStartIndex()
    {
        return startIndex;
    }

    public void setStartIndex(int startIndex)
    {
        this.startIndex = startIndex;
    }

    public int getRowsCount()
    {
        if(rowsCount == 0)
            rowsCount = 20;
        return rowsCount;
    }

    public void setRowsCount(int rowsCount)
    {
        this.rowsCount = rowsCount;
    }
}
