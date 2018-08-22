package com.igp.admin.Voucher.models;

public class GiftVoucherRequestContainer
{

    private GiftVoucherModel giftVoucherModel;
    private RowLimitModel    rowLimitModel;

    public GiftVoucherModel getGiftVoucherModel()
    {
        return giftVoucherModel;
    }

    public void setGiftVoucherModel(GiftVoucherModel giftVoucherModel)
    {
        this.giftVoucherModel = giftVoucherModel;
    }

    public RowLimitModel getRowLimitModel()
    {
        return rowLimitModel;
    }

    public void setRowLimitModel(RowLimitModel rowLimitModel)
    {
        this.rowLimitModel = rowLimitModel;
    }

    @Override
    public String toString()
    {
        return "VoucherRequestContainer{" + "giftVoucherModel=" + giftVoucherModel + ", rowLimitModel=" + rowLimitModel + '}';
    }
}
