package com.igp.admin.Voucher.models;

import java.util.List;

public class CategoriesModel{

    private int    categoriesId;
    private String categoriesName;
    private String categoriesNameAlt;
    private int    parentId;
    private int    sortOrder;
    private String categoriesUrl;
    private String categoriesAltUrl;
    private String categoriesDescription;
    private String categoriesNameForUrl;
    private String categoriesMetaTile;
    private String categoriesMetaKeywords;
    private String categoriesMetaDescription;
    private String categoriesIfNotDisplay;
    private int    categoriesStatus;


    private int                   topParent;
    private int                   catType;
    private int                   isHidden;
    private float                 weightage;
    private int                   categoryLevel;
    private int                   addonType;
    private List<CategoriesModel> subCategories;

    public int getCategoriesId()
    {
        return categoriesId;
    }

    public void setCategoriesId(int categoriesId)
    {
        this.categoriesId = categoriesId;
    }

    public String getCategoriesName()
    {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName)
    {
        this.categoriesName = categoriesName;
    }

    public int getTopParent()
    {
        return topParent;
    }

    public void setTopParent(int topParent)
    {
        this.topParent = topParent;
    }

    public String getCategoriesUrl()
    {
        return categoriesUrl;
    }

    public void setCategoriesUrl(String categoriesUrl)
    {
        this.categoriesUrl = categoriesUrl;
    }

    public int getCategoriesStatus()
    {
        return categoriesStatus;
    }

    public void setCategoriesStatus(int categoriesStatus)
    {
        this.categoriesStatus = categoriesStatus;
    }

    public int getCatType()
    {
        return catType;
    }

    public void setCatType(int catType)
    {
        this.catType = catType;
    }

    public int getIsHidden()
    {
        return isHidden;
    }

    public void setIsHidden(int isHidden)
    {
        this.isHidden = isHidden;
    }

    public float getWeightage()
    {
        return weightage;
    }

    public void setWeightage(float weightage)
    {
        this.weightage = weightage;
    }

    public int getCategoryLevel()
    {
        return categoryLevel;
    }

    public void setCategoryLevel(int categoryLevel)
    {
        this.categoryLevel = categoryLevel;
    }

    public int getAddonType()
    {
        return addonType;
    }

    public void setAddonType(int addonType)
    {
        this.addonType = addonType;
    }

    public List<CategoriesModel> getSubCategories()
    {
        return subCategories;
    }

    public void setSubCategories(List<CategoriesModel> subCategories)
    {
        this.subCategories = subCategories;
    }

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public String getCategoriesNameAlt()
    {
        return categoriesNameAlt;
    }

    public void setCategoriesNameAlt(String categoriesNameAlt)
    {
        this.categoriesNameAlt = categoriesNameAlt;
    }

    public int getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public String getCategoriesAltUrl()
    {
        return categoriesAltUrl;
    }

    public void setCategoriesAltUrl(String categoriesAltUrl)
    {
        this.categoriesAltUrl = categoriesAltUrl;
    }

    public String getCategoriesDescription()
    {
        return categoriesDescription;
    }

    public void setCategoriesDescription(String categoriesDescription)
    {
        this.categoriesDescription = categoriesDescription;
    }

    public String getCategoriesNameForUrl()
    {
        return categoriesNameForUrl;
    }

    public void setCategoriesNameForUrl(String categoriesNameForUrl)
    {
        this.categoriesNameForUrl = categoriesNameForUrl;
    }

    public String getCategoriesMetaTile()
    {
        return categoriesMetaTile;
    }

    public void setCategoriesMetaTile(String categoriesMetaTile)
    {
        this.categoriesMetaTile = categoriesMetaTile;
    }

    public String getCategoriesMetaKeywords()
    {
        return categoriesMetaKeywords;
    }

    public void setCategoriesMetaKeywords(String categoriesMetaKeywords)
    {
        this.categoriesMetaKeywords = categoriesMetaKeywords;
    }

    public String getCategoriesMetaDescription()
    {
        return categoriesMetaDescription;
    }

    public void setCategoriesMetaDescription(String categoriesMetaDescription)
    {
        this.categoriesMetaDescription = categoriesMetaDescription;
    }

    public String getCategoriesIfNotDisplay()
    {
        return categoriesIfNotDisplay;
    }

    public void setCategoriesIfNotDisplay(String categoriesIfNotDisplay)
    {
        this.categoriesIfNotDisplay = categoriesIfNotDisplay;
    }

    @Override
    public String toString() {
        return "CategoriesModel{" + "categoriesId=" + categoriesId + ", categoriesName='" + categoriesName + '\''
            + ", categoriesNameAlt='" + categoriesNameAlt + '\'' + ", parentId=" + parentId + ", sortOrder=" + sortOrder
            + ", categoriesUrl='" + categoriesUrl + '\'' + ", categoriesAltUrl='" + categoriesAltUrl + '\''
            + ", categoriesDescription='" + categoriesDescription + '\'' + ", categoriesNameForUrl='"
            + categoriesNameForUrl + '\'' + ", categoriesMetaTile='" + categoriesMetaTile + '\''
            + ", categoriesMetaKeywords='" + categoriesMetaKeywords + '\'' + ", categoriesMetaDescription='"
            + categoriesMetaDescription + '\'' + ", categoriesIfNotDisplay='" + categoriesIfNotDisplay + '\''
            + ", categoriesStatus=" + categoriesStatus + ", topParent=" + topParent + ", catType=" + catType
            + ", isHidden=" + isHidden + ", weightage=" + weightage + ", categoryLevel=" + categoryLevel
            + ", addonType=" + addonType + ", subCategories=" + subCategories + '}';
    }
}
