package com.igp.handles.admin.models.Mail;

/**
 * Created by shanky on 24/8/18.
 */
public class MailTemplateModel {

    private String content;

    private String email_header;

    private String email_footer;

    private String email_body ;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail_header() {
        return email_header;
    }

    public void setEmail_header(String email_header) {
        this.email_header = email_header;
    }

    public String getEmail_footer() {
        return email_footer;
    }

    public void setEmail_footer(String email_footer) {
        this.email_footer = email_footer;
    }

    public String getEmail_body() {
        return email_body;
    }

    public void setEmail_body(String email_body) {
        this.email_body = email_body;
    }
}
