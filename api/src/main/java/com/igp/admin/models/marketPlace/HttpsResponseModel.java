package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 12/1/18.
 */
public class HttpsResponseModel {
    @JsonProperty("resp")
    private String encryptedResponse;

    @JsonProperty("error")
    private Boolean error;


    public String getEncryptedResponse() {
        return encryptedResponse;
    }

    public void setEncryptedResponse(String encryptedResponse) {
        this.encryptedResponse = encryptedResponse;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
