package com.igp.admin.Voucher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by suditi on 12/6/18.
 */
public class VoucherListModel {
    @JsonProperty("count")
    Integer count;
    @JsonProperty("vouchermodellist")
    List<VoucherModel> voucherModelList;


    public List<VoucherModel> getVoucherModelList() {
        return voucherModelList;
    }

    public void setVoucherModelList(List<VoucherModel> voucherModelList) {
        this.voucherModelList = voucherModelList;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
