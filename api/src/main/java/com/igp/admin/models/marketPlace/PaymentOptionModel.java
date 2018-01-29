package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 18/1/18.
 */
public class PaymentOptionModel {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("gway")
    private String gateway;

    @JsonProperty("type")
    private String type;

    @JsonProperty("code")
    private String code;

    @JsonProperty("pgparam")
    private String paymentGatewayParam;

    @JsonProperty("pgparamext")
    private String paymentGatewayParamExtra;

    @JsonProperty("defaultpgateway")
    private String defaultPaymentGateway;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPaymentGatewayParam() {
        return paymentGatewayParam;
    }

    public void setPaymentGatewayParam(String paymentGatewayParam) {
        this.paymentGatewayParam = paymentGatewayParam;
    }

    public String getPaymentGatewayParamExtra() {
        return paymentGatewayParamExtra;
    }

    public void setPaymentGatewayParamExtra(String paymentGatewayParamExtra) {
        this.paymentGatewayParamExtra = paymentGatewayParamExtra;
    }

    public String getDefaultPaymentGateway() {
        return defaultPaymentGateway;
    }

    public void setDefaultPaymentGateway(String defaultPaymentGateway) {
        this.defaultPaymentGateway = defaultPaymentGateway;
    }
}
