package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by suditi on 22/1/18.
 */
public class CountModel {
    @JsonProperty("correct")
    private Integer correct;

    @JsonProperty("fail")
    private Integer fail;

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public Integer getFail() {
        return fail;
    }

    public void setFail(Integer fail) {
        this.fail = fail;
    }

    @Override
    public String toString() {
        return "CountModel{" +
            "correct=" + correct +
            ", fail=" + fail +
            '}';
    }
}
