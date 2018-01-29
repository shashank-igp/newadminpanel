package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * Created by suditi on 22/1/18.
 */
@JsonDeserialize
@JsonSerialize
public class AuthResponseModel implements Cloneable {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)

    @JsonProperty("l")
    private Boolean login;

    private UserModel user;
    @JsonProperty("msg")
    private String message;

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getLogin() {
        return login;
    }

    public UserModel getUser() {
        return user;
    }



}
