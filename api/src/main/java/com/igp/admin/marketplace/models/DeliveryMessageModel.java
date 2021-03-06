package com.igp.admin.marketplace.models;

/**
 * Created by suditi on 11/1/18.
 */
public class DeliveryMessageModel {
    private String to;
    private String from;
    private String message;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DeliveryMessageModel{" +
            "to='" + to + '\'' +
            ", from='" + from + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
