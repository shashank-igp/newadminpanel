package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 22/1/18.
 */
public class ErrorModel {
    @JsonProperty("row")
    private Integer row;

    @JsonProperty("msg")
    private String msg;

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
