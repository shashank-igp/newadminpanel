package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by suditi on 12/1/18.
 */
@JsonDeserialize(builder = UserModel.UserBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize
public class UserModel implements Cloneable {

    @Override
    public UserModel clone() throws CloneNotSupportedException {
        return (UserModel) super.clone();
    }

    @Override
    public String toString() {
        return "UserModel{" +
            "id='" + id + '\'' +
            ", idHash='" + idHash + '\'' +
            ", uniqsrc='" + uniqsrc + '\'' +
            ", image='" + image + '\'' +
            ", gender='" + gender + '\'' +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", dob='" + dob + '\'' +
            ", email='" + email + '\'' +
            ", addressField1='" + addressField1 + '\'' +
            ", addressField2='" + addressField2 + '\'' +
            ", postcode='" + postcode + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", landline='" + landline + '\'' +
            ", fax='" + fax + '\'' +
            ", password='" + password + '\'' +
            ", newPassword='" + newPassword + '\'' +
            ", countryId=" + countryId +
            ", maritalStatus='" + maritalStatus + '\'' +
            ", landlineAreaCode='" + landlineAreaCode + '\'' +
            ", landlineCountryCode='" + landlineCountryCode + '\'' +
            ", subscription='" + subscription + '\'' +
            ", associateId=" + associateId +
            ", mobile='" + mobile + '\'' +
            ", mobilePrefix='" + mobilePrefix + '\'' +
            ", check=" + check +
            ", category=" + category +
            ", verify=" + verify +
            ", mergeCart=" + mergeCart +
            ", cartItemCount=" + cartItemCount +
            ", isNewsLetter=" + isNewsLetter +
            '}';
    }

    @JsonProperty("id")
    private String id;

    @JsonProperty("hash")
    private String idHash;

    @JsonProperty("usrc")
    private String uniqsrc;

    @JsonProperty("image")
    private String image;

    @JsonProperty("g")
    private String gender;

    @JsonProperty("fname")
    private String firstname;

    @JsonProperty("lname")
    private String lastname;

    @JsonProperty("dob")
    private String dob;

    @JsonProperty("email")
    private String email;

    @JsonProperty("add1")
    private String addressField1;

    @JsonProperty("add2")
    private String addressField2;

    @JsonIgnore
    private String postcode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("phone")
    private String landline;

    @JsonProperty("fax")
    private String fax;

    @JsonProperty("passwd")
    private String password;

    @JsonProperty("npasswd")
    private String newPassword;

    @JsonProperty("cid")
    private Integer countryId;

    @JsonProperty("mstatus")
    private String maritalStatus;

    @JsonProperty("phcode")
    private String landlineAreaCode;

    @JsonProperty("phccode")
    private String landlineCountryCode;

    @JsonProperty("sub")
    private String subscription;

    @JsonProperty("fkasid")
    private Integer associateId;

    @JsonProperty("mob")
    private String mobile;

    @JsonProperty("mprefix")
    private String mobilePrefix;

    @JsonProperty("chk")
    private Integer check;

    @JsonProperty("cat")
    private Integer category;

    @JsonProperty("v")
    private Integer verify; //using it to send verification details 1->Email, 2->Mobile

    @JsonProperty("mcart")
    private Integer mergeCart;


    private Integer cartItemCount;

    @JsonProperty("newsletter")
    private Boolean isNewsLetter;


    public void setIdHash(String idHash) {
        this.idHash = idHash;
    }

    public void setUniqsrc(String uniqsrc) {
        this.uniqsrc = uniqsrc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddressField1(String addressField1) {
        this.addressField1 = addressField1;
    }

    public void setAddressField2(String addressField2) {
        this.addressField2 = addressField2;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public void setLandlineAreaCode(String landlineAreaCode) {
        this.landlineAreaCode = landlineAreaCode;
    }

    public void setLandlineCountryCode(String landlineCountryCode) {
        this.landlineCountryCode = landlineCountryCode;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public void setAssociateId(Integer associateId) {
        this.associateId = associateId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCheck(Integer check) {
        this.check = check;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public void setMergeCart(Integer mergeCart) {
        this.mergeCart = mergeCart;
    }

    @JsonProperty("cic")
    public Integer getCartItemCount() {
        return cartItemCount;
    }

    @JsonProperty("cic")
    public void setCartItemCount(Integer cartItemCount) {
        this.cartItemCount = cartItemCount;
    }



    public Boolean getIsNewsLetter() {
        return isNewsLetter;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUniqsrc() {
        return uniqsrc;
    }
    public String getImage() {
        return image;
    }

    public String getIdHash() {
        return idHash;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getAddressField1() {
        return addressField1;
    }

    public String getAddressField2() {
        return addressField2;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getLandline() {
        return landline;
    }

    public String getFax() {
        return fax;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() { return newPassword; }

    public Integer getCountryId() {
        return countryId;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getLandlineAreaCode() {
        return landlineAreaCode;
    }

    public String getLandlineCountryCode() {
        return landlineCountryCode;
    }

    public String getSubscription() {
        return subscription;
    }


    public Integer getAssociateId()
    {
        if(associateId==null){
            associateId=5;
        }
        return associateId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMobilePrefix() {
        return mobilePrefix;
    }

    public void setMobilePrefix(String mobilePrefix) {
        this.mobilePrefix = mobilePrefix;
    }

    public Integer getCheck() {
        return check;
    }

    public Integer getCategory() {
        return category;
    }

    public Integer getVerify() {
        return verify;
    }

    public Integer getMergeCart() {
        return mergeCart;
    }

    public Integer getcartItemCount() {
        return cartItemCount;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }


    private UserModel(UserBuilder builder) {
        this.id = builder.id;
        this.idHash = builder.idHash;
        this.uniqsrc = builder.uniqsrc;
        this.image = builder.image;
        this.gender = builder.gender;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.dob = builder.dob;
        this.email = builder.email;
        this.addressField1 = builder.addressField1;
        this.addressField2 = builder.addressField2;
        this.postcode = builder.postcode;
        this.newPassword = builder.newPassword;
        this.city = builder.city;
        this.state = builder.state;
        this.landline = builder.landline;
        this.fax = builder.fax;
        this.password = builder.password;
        this.countryId = builder.countryId;
        this.maritalStatus = builder.maritalStatus;
        this.landlineAreaCode = builder.landlineAreaCode;
        this.landlineCountryCode = builder.landlineCountryCode;
        this.subscription = builder.subscription;
        this.associateId = builder.associateId;
        this.mobile = builder.mobile;
        this.mobilePrefix = builder.mobilePrefix;
        this.check = builder.check;
        this.category = builder.category;
        this.verify = builder.verify;
        this.mergeCart = builder.mergeCart;
        this.cartItemCount = builder.cartItemCount;
        this.isNewsLetter=builder.isNewsLetter;
        this.cartItemCount = builder.cartItemCount;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static class UserBuilder {
        @JsonProperty("id")
        private String id;

        @JsonProperty("hash")
        private String idHash;

        @JsonProperty("usrc")
        private String uniqsrc;

        @JsonProperty("image")
        private String image;

        @JsonProperty("g")
        private String gender;

        @JsonProperty("fname")
        private String firstname;

        @JsonProperty("lname")
        private String lastname;

        @JsonProperty("dob")
        private String dob;

        @JsonProperty("email")
        private String email;

        @JsonProperty("add1")
        private String addressField1;

        @JsonProperty("add2")
        private String addressField2;

        @JsonProperty("pcode")
        private String postcode;

        @JsonProperty("city")
        private String city;

        @JsonProperty("state")
        private String state;

        @JsonProperty("ph")
        private String landline;

        @JsonProperty("fax")
        private String fax;

        @JsonProperty("passwd")
        private String password;

        @JsonProperty("npasswd")
        private String newPassword;

        @JsonProperty("cid")
        private Integer countryId;

        @JsonProperty("mstatus")
        private String maritalStatus;

        @JsonProperty("phcode")
        private String landlineAreaCode;

        @JsonProperty("phccode")
        private String landlineCountryCode;

        @JsonProperty("sub")
        private String subscription;

        @JsonProperty("fkasid")
        private Integer associateId;

        @JsonProperty("mob")
        private String mobile;

        @JsonProperty("mprefix")
        private String mobilePrefix;

        @JsonProperty("chk")
        private Integer check;

        @JsonProperty("cat")
        private Integer category;

        @JsonProperty("v")
        private Integer verify;

        @JsonProperty("mcart")
        private Integer mergeCart;

        @JsonProperty("cic")
        private Integer cartItemCount;

        @JsonProperty("newsletter")
        private Boolean isNewsLetter;

        public UserBuilder isNewsLetter(Boolean isNewsLetter){
            if(isNewsLetter==null){
                this.isNewsLetter=Boolean.TRUE;
            }
            else {
                this.isNewsLetter = isNewsLetter;
            }
            return  this;
        }

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }


        public UserBuilder idHash(String idHash) {
            this.idHash = idHash;
            return this;
        }

        public UserBuilder uniqsrc(String uniqsrc) {
            this.uniqsrc = uniqsrc;
            return this;
        }

        public UserBuilder image(String image) {
            this.image = image;
            return this;
        }

        public UserBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public UserBuilder mobilePrefix(String mobilePrefix) {
            this.mobilePrefix = mobilePrefix;
            return this;
        }

        public UserBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public UserBuilder dob(String dob) {
            this.dob = dob;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder addressField1(String addressField1) {
            this.addressField1 = addressField1;
            return this;
        }

        public UserBuilder addressField2(String addressField2) {
            this.addressField2 = addressField2;
            return this;
        }

        public UserBuilder postcode(String postcode) {
            this.postcode = postcode;
            return this;
        }

        public UserBuilder city(String city) {
            this.city = city;
            return this;
        }

        public UserBuilder state(String state) {
            this.state = state;
            return this;
        }

        public UserBuilder landline(String landline) {
            this.landline = landline;
            return this;
        }

        public UserBuilder fax(String fax) {
            this.fax = fax;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public UserBuilder countryId(Integer countryId) {
            this.countryId = countryId;
            return this;
        }

        public UserBuilder maritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
            return this;
        }

        public UserBuilder landlineAreaCode(String landlineAreaCode) {
            this.landlineAreaCode = landlineAreaCode;
            return this;
        }

        public UserBuilder landlineCountryCode(String landlineCountryCode) {
            this.landlineCountryCode = landlineCountryCode;
            return this;
        }

        public UserBuilder subscription(String subscription) {
            this.subscription = subscription;
            return this;
        }

        public UserBuilder associateId(Integer associateId) {
            this.associateId = associateId;
            return this;
        }

        public UserBuilder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public UserBuilder check(Integer check) {
            this.check = check;
            return this;
        }

        public UserBuilder category(Integer category) {
            this.category = category;
            return this;
        }

        public UserBuilder verify(Integer verify) {
            this.verify = verify;
            return this;
        }

        public UserBuilder mergeCart(Integer mergeCart) {
            this.mergeCart = mergeCart;
            return this;
        }

        public UserBuilder cartItemCount(Integer cartItemCount) {
            this.cartItemCount = cartItemCount;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }
    }
}
