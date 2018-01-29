package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;

/**
 * Created by suditi on 25/1/18.
 */
public class FileUploadModel {
    @JsonProperty("uploadedFilePath")
    private String uploadedFilePath;

    private File file;

    private Boolean error;

    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
