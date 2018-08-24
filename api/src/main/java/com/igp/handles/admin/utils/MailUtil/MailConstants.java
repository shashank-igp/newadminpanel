package com.igp.handles.admin.utils.MailUtil;

/**
 * Created by shanky on 24/8/18.
 */
public class MailConstants {
    public static final String SUBJECT = "subject";
    public static final String CONTENT = "content";
    public static final String OTP = "otp";
    public static final String NAME = "name";
    public static final Integer GENERIC_MAIL = 3;
    public static final Integer FORGOT_PASSWORD_SMS = 4;

    /*
    The below new Id represents the table newigp_mail_template
     */
    public static final Integer PASSWORD_CHANGE_SUCCESS = 1;
    public static final Integer PASSWORD_REQUEST_CHANGE = 2;
    public static final Integer ORDER_CONFIRMATION = 3;
    public static final Integer ODER_PARTIAL_CANCELLATION = 4;
    public static final Integer ORDER_FULLY_CANCELLATION = 5;
    public static final Integer ORDER_PARTIALLY_DISPATCHED = 6;
    public static final Integer ORDER_FULLY_DISPATCHED = 7;
    public static final Integer ORDER_PARTIALLY_SHIPPED = 8;
    public static final Integer ORDER_FULLY_SHIPPED = 9;
    public static final Integer WELCOME_MAIL_IGP = 10;
    public static final Integer SHARE_MAIL_FRIENDS =11;
    public static final Integer WELCOME_MAIL_INTERFLORA = 12;
    public static final Integer FOLLOW_UP_MAIL_ADDRESS_RELATED = 13;
    public static final Integer FOLLOW_UP_MAIL_CUSTOMMER_NOT_FOUND = 14;
}
