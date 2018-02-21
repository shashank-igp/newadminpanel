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

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("fkasid")
    private Integer fkAssociateId;

    @JsonProperty("msg")
    private String message;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ValidationModel{" +
            "error=" + error +
            ", userModel=" + userModel +
            ", addressModel=" + addressModel +
            ", productModel=" + productModel +
            ", extraInfoModel=" + extraInfoModel +
            ", id=" + id +
            ", fkAssociateId=" + fkAssociateId +
            ", message='" + message + '\'' +
            '}';
    }
}
