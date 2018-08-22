package com.igp.admin.Voucher.models;

import java.util.List;

public class VoucherMetaData
{
    List<MetaDataModel> type1;
    List<MetaDataModel>       type2;
    List<MetaDataModel>       orderValueCheck;
    List<MetaDataModel>       shippingWaiverType;

    public List<MetaDataModel> getType1()
    {
        return type1;
    }

    public void setType1(List<MetaDataModel> type1)
    {
        this.type1 = type1;
    }

    public List<MetaDataModel> getType2()
    {
        return type2;
    }

    public void setType2(List<MetaDataModel> type2)
    {
        this.type2 = type2;
    }

    public List<MetaDataModel> getOrderValueCheck()
    {
        return orderValueCheck;
    }

    public void setOrderValueCheck(List<MetaDataModel> orderValueCheck)
    {
        this.orderValueCheck = orderValueCheck;
    }

    public List<MetaDataModel> getShippingWaiverType()
    {
        return shippingWaiverType;
    }

    public void setShippingWaiverType(List<MetaDataModel> shippingWaiverType)
    {
        this.shippingWaiverType = shippingWaiverType;
    }
}
