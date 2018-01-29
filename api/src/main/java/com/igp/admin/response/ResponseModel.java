package com.igp.admin.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.ws.rs.core.Response;

/**
 * Created by suditi on 18/1/18.
 */
public class ResponseModel {
    @JsonIgnore
    private final Response.Status httpStatus;
    private final Status status;
    private final String message;
    private final Object data;

    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    private ResponseModel(ResponseBuilder builder) {
        this.status = builder.status;
        this.httpStatus = builder.httpStatus;
        this.message = builder.message;
        this.data = builder.data;
    }

    public static class ResponseBuilder {
        private final Status status;
        private final Response.Status httpStatus;
        private String message;
        private Object data;

        public ResponseBuilder(Status status, Response.Status httpStatus) {
            this.status = status;
            this.httpStatus = httpStatus;
        }

        public ResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public ResponseModel build() {
            return new ResponseModel(this);
        }
    }
}
