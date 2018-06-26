package com.igp.admin.Voucher.models;

import java.util.Map;

public class VoucherMetaData
{
    Map<Integer, String> type1;
    Map<Integer, String> type2;
    Map<Integer, String> orderValueCheck;
    Map<Integer, String> shippingWaiverType;

    public Map<Integer, String> getType1()
    {
        return type1;
    }

    public void setType1(Map<Integer, String> type1)
    {
        this.type1 = type1;
    }

    public Map<Integer, String> getType2()
    {
        return type2;
    }

    public void setType2(Map<Integer, String> type2)
    {
        this.type2 = type2;
    }

    public Map<Integer, String> getOrderValueCheck()
    {
        return orderValueCheck;
    }

    public void setOrderValueCheck(Map<Integer, String> orderValueCheck)
    {
        this.orderValueCheck = orderValueCheck;
    }

    public Map<Integer, String> getShippingWaiverType()
    {
        return shippingWaiverType;
    }

    public void setShippingWaiverType(Map<Integer, String> shippingWaiverType)
    {
        this.shippingWaiverType = shippingWaiverType;
    }
}
