package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 15/1/18.
 */
public class ValidationModel {

    @JsonProperty("error")
    private Boolean error;

    @JsonProperty("user")
    private UserModel userModel;

    @JsonProperty("ship")
    private AddressModel addressModel;

    @JsonProperty("product")
    private ProductModel productModel;

    @JsonProperty("extrainfo")
    private ExtraInfoModel extraInfoModel;

    @JsonProperty("delmsg")
    private DeliveryMessageModel deliveryMessageModel;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("fkasid")
    private Integer fkAssociateId;

    @JsonProperty("msg")
    private String message;

    @JsonProperty("row")
    private int rowNum;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public ExtraInfoModel getExtraInfoModel() {
        return extraInfoModel;
    }

    public void setExtraInfoModel(ExtraInfoModel extraInfoModel) {
        this.extraInfoModel = extraInfoModel;
    }

    public DeliveryMessageModel getDeliveryMessageModel() {
        return deliveryMessageModel;
    }

    public void setDeliveryMessageModel(DeliveryMessageModel deliveryMessageModel) {
        this.deliveryMessageModel = deliveryMessageModel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkAssociateId() {
        return fkAssociateId;
    }

    public void setFkAssociateId(Integer fkAssociateId) {
        this.fkAssociateId = fkAssociateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String toString() {
        return "ValidationModel{" +
            "error=" + error +
            ", userModel=" + userModel +
            ", addressModel=" + addressModel +
            ", productModel=" + productModel +
            ", extraInfoModel=" + extraInfoModel +
            ", deliveryMessageModel=" + deliveryMessageModel +
            ", id=" + id +
            ", fkAssociateId=" + fkAssociateId +
            ", message='" + message + '\'' +
            ", rowNum=" + rowNum +
            '}';
    }
}
