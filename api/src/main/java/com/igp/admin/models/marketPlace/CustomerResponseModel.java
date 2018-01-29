package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 12/1/18.
 */
public class CustomerResponseModel {
    @JsonProperty("l")
    private Boolean login;
    private UserModel user;
    @JsonProperty("msg")
    private String message;

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private CustomerResponseModel(Builder builder) {
        login = builder.login;
        user = builder.user;
        message = builder.message;
    }

    public static final class Builder {
        private Boolean login;
        private UserModel user;
        private String message;

        public Builder() {
        }

        public Builder login(Boolean val) {
            login = val;
            return this;
        }

        public Builder user(UserModel val) {
            user = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public CustomerResponseModel build() {
            return new CustomerResponseModel(this);
        }
    }
}
