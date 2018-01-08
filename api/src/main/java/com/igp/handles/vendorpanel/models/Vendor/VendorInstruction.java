package com.igp.handles.vendorpanel.models.Vendor;

/**
 * Created by shanky on 29/11/17.
 */
public class VendorInstruction {
    private String orderId;

    private String instruction;

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getInstruction()
    {
        return instruction;
    }

    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }
}
