package com.igp.admin.Voucher.models;

import java.util.List;

public class CategoriesModel{

    private int    categoriesId;
    private String categoriesName;
    private int    topParent;
    private int    parentId;
    private String categoriesUrl;
    private int    categoriesStatus;

    private int                   catType;
    private int                   isHidden;
    private float                 weightage;
    private int                   categoryLevel;
    private int                   addonLevel;
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

    public int getAddonLevel()
    {
        return addonLevel;
    }

    public void setAddonLevel(int addonLevel)
    {
        this.addonLevel = addonLevel;
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

    @Override public String toString()
    {
        return "CategoriesModel{" + "categoriesId=" + categoriesId + ", categoriesName='" + categoriesName + '\''
            + ", topParent=" + topParent + ", parentId=" + parentId + ", categoriesUrl='" + categoriesUrl + '\''
            + ", categoriesStatus=" + categoriesStatus + ", catType=" + catType + ", isHidden=" + isHidden
            + ", weightage=" + weightage + ", categoryLevel=" + categoryLevel + ", addonLevel=" + addonLevel
            + ",\n\n subCategories=" + subCategories + '}';
    }
}
