package com.igp.handles.vendorpanel.models.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Date;

@JsonDeserialize(builder = UserModel.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel implements Cloneable {
    @JsonProperty("user_id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    @JsonProperty("password")
    private String password;
    @JsonProperty("fkAssociateId")
    private String fkAssociateId;
    @JsonProperty("associateName")
    private String associateName;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("expires")
    private Date expires;
    @JsonProperty("accountExpired")
    private boolean accountExpired;
    @JsonProperty("credentialExpired")
    private boolean credentialExpired;
    @JsonProperty("accountLocked")
    private boolean accountLocked;
    @JsonProperty("accountEnabled")
    private int accountEnabled;

    private UserModel(Builder builder) {
        setId(builder.id);
        name = builder.name;
        password = builder.password;
        fkAssociateId = builder.fkAssociateId;
        associateName = builder.associateName;
        phoneNumber = builder.phoneNumber;
        expires = builder.expires;
        accountExpired = builder.accountExpired;
        credentialExpired = builder.credentialExpired;
        accountLocked = builder.accountLocked;
        accountEnabled = builder.accountEnabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getFkAssociateId() {
        return fkAssociateId;
    }

    public String getAssociateName() {
        return associateName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getExpires() {
        return expires;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public boolean isCredentialExpired() {
        return credentialExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public int getAccountEnabled()
    {
        return accountEnabled;
    }

    @Override
    public UserModel clone() throws CloneNotSupportedException {
        return (UserModel) super.clone();
    }

    @Override
    public String toString() {
        return "UserModel{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", password='" + password + '\'' +
            ", fkAssociateId='" + fkAssociateId + '\'' +
            ", associateName='" + associateName + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", expires=" + expires +
            ", accountExpired=" + accountExpired +
            ", credentialExpired=" + credentialExpired +
            ", accountLocked=" + accountLocked +
            ", accountEnabled=" + accountEnabled +
            '}';
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder
    {
        @JsonProperty("user_id")
        private long id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("password")
        private String password;
        @JsonProperty("fkAssociateId")
        private String fkAssociateId;
        @JsonProperty("associateName")
        private String associateName;
        @JsonProperty("phoneNumber")
        private String phoneNumber;
        @JsonProperty("expires")
        private Date expires;
        @JsonProperty("accountExpired")
        private boolean accountExpired;
        @JsonProperty("credentialExpired")
        private boolean credentialExpired;
        @JsonProperty("accountLocked")
        private boolean accountLocked;
        @JsonProperty("accountEnabled")
        private int accountEnabled;

        public Builder() {
        }

        public Builder id(long val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder fkAssociateId(String val) {
            fkAssociateId = val;
            return this;
        }

        public Builder associateName(String val) {
            associateName = val;
            return this;
        }

        public Builder phoneNumber(String val) {
            phoneNumber = val;
            return this;
        }

        public Builder expires(Date val) {
            expires = val;
            return this;
        }

        public Builder accountExpired(boolean val) {
            accountExpired = val;
            return this;
        }

        public Builder credentialExpired(boolean val) {
            credentialExpired = val;
            return this;
        }

        public Builder accountLocked(boolean val) {
            accountLocked = val;
            return this;
        }

        public Builder accountEnabled(int val) {
            accountEnabled = val;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }
    }
}
