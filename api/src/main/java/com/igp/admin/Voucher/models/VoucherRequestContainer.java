package com.igp.admin.Voucher.models;

public class VoucherRequestContainer
{

    private VoucherModel voucherModel;
    private RowLimitModel rowLimitModel;

    public VoucherModel getVoucherModel()
    {
        return voucherModel;
    }

    public void setVoucherModel(VoucherModel voucherModel)
    {
        this.voucherModel = voucherModel;
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
        return "VoucherRequestContainer{" + "voucherModel=" + voucherModel + ", rowLimitModel=" + rowLimitModel + '}';
    }
}
