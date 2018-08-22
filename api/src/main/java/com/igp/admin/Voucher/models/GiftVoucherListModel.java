package com.igp.admin.Voucher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by nikhil on 21/08/18.
 */
public class GiftVoucherListModel
{
    @JsonProperty("count")
    Integer count;
    @JsonProperty("giftvouchermodellist")
    List<GiftVoucherModel> giftVoucherModelList;

    public List<GiftVoucherModel> getGiftVoucherModelList()
    {
        return giftVoucherModelList;
    }

    public void setGiftVoucherModelList(List<GiftVoucherModel> giftVoucherModelList)
    {
        this.giftVoucherModelList = giftVoucherModelList;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
