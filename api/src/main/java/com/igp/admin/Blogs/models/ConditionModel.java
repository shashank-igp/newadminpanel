package com.igp.admin.Blogs.models;

import java.util.ArrayList;
import java.util.List;

public class ConditionModel {

    private String value;
    private String operator;


    public ConditionModel() {
    }
    public ConditionModel(String value, String operator) {
        this.value = value;
        this.operator = operator;
    }
    public ConditionModel(String value) {
        this(value, "AND");
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "ConditionModel{" +
            "value='" + value + '\'' +
            ", operator='" + operator + '\'' +
            '}';
    }
}
