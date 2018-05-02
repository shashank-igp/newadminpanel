package com.igp.admin.marketplace.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;

/**
 * Created by suditi on 25/1/18.
 */
public class FileUploadModel {
    @JsonProperty("uploadedFilePath")
    private String uploadedFilePath;

    private File file;
    private String fileName;
    private String fileExtension;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
