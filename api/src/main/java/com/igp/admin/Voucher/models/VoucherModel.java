package com.igp.admin.Voucher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by suditi on 8/6/18.
 */
public class VoucherModel {
    @JsonProperty("id")
    private int id;

    @JsonProperty("vouchercode")
    private String voucherCode;

    @JsonProperty("createdby")
    private String createdBy = "admin";

    @JsonProperty("modifiedby")
    private String modifiedBy = "admin";

    @JsonProperty("vouchervalue")
    private int voucherValue;

    @JsonProperty("fkasid")
    private int fkAssociateId;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("multipleusage")
    private int multipleUsage;

    @JsonProperty("enabled")
    private int enabled;

    @JsonProperty("usedcount")
    private int usedCount;

    @JsonProperty("applicablecategory")
    private String applicableCategory;

    @JsonProperty("vouchertype")
    private int voucherType; // 0 - percentageBased, 1 - giftVoucher{has a specific value}

    @JsonProperty("generatedate")
    private String generateDate;

    @JsonProperty("expirydate")
    private String expiryDate;

    @JsonProperty("createddate")
    private String createdDate;

    @JsonProperty("modifieddate")
    private String modifiedDate;

    @JsonProperty("applicableemail")
    private List<String> applicableEmail;

    @JsonProperty("ordervaluecheck")
    private int orderValueCheck;

    @JsonProperty("ordervalue")
    private Integer orderValue;   // voucher valid only if orderValue greater than this

    @JsonProperty("blackListPts")
    private List<Integer> blackListPts;// In Valid Cat iDs

    @JsonProperty("whiteListPts")
    private List<Integer> whiteListPts; // valid cat iDs

    @JsonProperty("shippingwaivertype")
    private int shippingWaiverType;

    @JsonProperty("applicablePid")
    private String applicablePid;

    @JsonProperty("productQuant")
    private String productQuant;

    @JsonProperty("applicablevouchertype")
    private int applicableVoucherType; // 0-noEmail, 1-emailList, 2-domainBased

    public String getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(String createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getModifiedDate()
    {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate)
    {
        this.modifiedDate = modifiedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getVoucherValue() {
        return voucherValue;
    }

    public void setVoucherValue(int voucherValue) {
        this.voucherValue = voucherValue;
    }

    public int getFkAssociateId() {
        return fkAssociateId;
    }

    public void setFkAssociateId(int fkAssociateId) {
        this.fkAssociateId = fkAssociateId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getMultipleUsage() {
        return multipleUsage;
    }

    public void setMultipleUsage(int multipleUsage) {
        this.multipleUsage = multipleUsage;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public String getApplicableCategory() {
        if(applicableCategory==null){
            applicableCategory="";
        }
        return applicableCategory;
    }

    public void setApplicableCategory(String applicableCategory) {

            this.applicableCategory = applicableCategory;
    }

    public int getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(int voucherType) {
        this.voucherType = voucherType;
    }

    public String getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(String generateDate) {
        this.generateDate = generateDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }


    public List<String> getApplicableEmail() {
        return applicableEmail;
    }

    public void setApplicableEmail(List<String> applicableEmail) {
        this.applicableEmail = applicableEmail;
    }

    public int getOrderValueCheck() {
        return orderValueCheck;
    }

    public void setOrderValueCheck(int orderValueCheck) {
        this.orderValueCheck = orderValueCheck;
    }

    public Integer getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Integer orderValue) {
        this.orderValue = orderValue;
    }

    public List<Integer> getBlackListPts() {
        return blackListPts;
    }

    public void setBlackListPts(List<Integer> blackListPts) {
        this.blackListPts = blackListPts;
    }

    public List<Integer> getWhiteListPts() {
        return whiteListPts;
    }

    public void setWhiteListPts(List<Integer> whiteListPts) {
        this.whiteListPts = whiteListPts;
    }

    public int getShippingWaiverType() {
        return shippingWaiverType;
    }

    public void setShippingWaiverType(int shippingWaiverType) {
        this.shippingWaiverType = shippingWaiverType;
    }

    public String getApplicablePid() {
        return applicablePid;
    }

    public void setApplicablePid(String applicablePid) {
        this.applicablePid = applicablePid;
    }

    public String getProductQuant() {
        return productQuant;
    }

    public void setProductQuant(String productQuant) {
        this.productQuant = productQuant;
    }

    public int getApplicableVoucherType() {
        return applicableVoucherType;
    }

    public void setApplicableVoucherType(int applicableVoucherType) {
        this.applicableVoucherType = applicableVoucherType;
    }
}
