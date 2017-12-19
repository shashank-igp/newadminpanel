package com.igp.handles.models.FileUpload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

/**
 * Created by shanky on 28/9/17.
 */
@JsonSerialize
@JsonDeserialize
public class FileUploadModel {

    @JsonProperty("uploadedFilePath")
    private Map<String,List<String>> uploadedFilePath;

    public Map<String, List<String>> getUploadedFilePath()
    {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(Map<String, List<String>> uploadedFilePath)
    {
        this.uploadedFilePath = uploadedFilePath;
    }
}
