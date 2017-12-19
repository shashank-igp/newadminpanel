package com.igp.handles.models.Report;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by shanky on 21/9/17.
 */
@JsonDeserialize
@JsonSerialize
public class SummaryModel {
    @JsonProperty("label")
    private String label;



    @JsonProperty("value")
    private String value;

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
