package com.igp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Environment {

    private static final Logger logger = LoggerFactory.getLogger(Environment.class);

    private static Integer SERVER_PORT = null;
    private static List<String> MIME_TYPES = new ArrayList<>();
    private static String ALLOWED_ORIGINS = "";
    private static String ALLOWED_METHODS = "";
    private static String ALLOWED_HEADERS_PREFLIGHTCALL="";
    private static String ALLOWED_HEADERS = "";
    private static List<String> ALLOWED_AUTH_KEYS = new ArrayList<>();
    private static List<String> DEFINED_REGION = new ArrayList<>();
    private static List<String> DEFINED_LANG = new ArrayList<>();
    private static String CURRENCY_CONVERSION = "";
    private static String MESSAGE_BROKER_TYPE = "";
    private static String VENDOR_SERVICE_URL = "";
    private static String CATEGORY_FILE = "";
    private static String FRESHDESK_API_TOKEN = "";
    private static String FRESHDESK_API_PWD = "";
    private static String FRESHDESK_API_ENDPOINT = "";
    private static String FRESHDESK_API_EMAIL = "";
    private static Integer GIFT_BOX_PRICE = null;
    private static Integer SHIPPING_STANDARD = null;
    private static Integer SHIPPING_FIXED = null;
    private static Integer SHIPPING_MIDNIGHT = null;
    private static Integer SHIPPING_SAME_DAY = null;
    private static String ATTR_FIELD_FILE = "";
    private static String CONTACT_SUPPORT = null;
    private static String GREETING_CARD_SUB_CAT = null;

    private static Integer BLUE_DART_SHIP_PRICE = null;
    private static String HOME_DIRECTORY = null;
    private static String CDN_URL= null;
    private static String PREFIX = null ;
    private static String SEARCHORDER = null ;
    private static String MYACCOUNT = null;
    private static String ORDERHISTORY = null;
    private static String BASEURL  = null;
    private static String VENDOR_PANEL_S3UPLOAD_BUCKETNAME=null;
    private static String S3UPLOAD_ACCESS_KEY=null;
    private static String S3UPLOAD_SECRET_KEY=null;
    private static String S3BASE_URL=null;

    
    public static Integer getServerPort() {
        if (SERVER_PORT == null) {
            SERVER_PORT = Integer.parseInt(ServerProperties.getPropertyValue("SERVER_PORT"));
            if (SERVER_PORT == 0) {
                logger.error("Please set up SERVER_PORT in api.ini");
                SERVER_PORT = null;
            }
        }
        return SERVER_PORT;
    }

    public static Integer getBlueDartShipPrice() {
        if (BLUE_DART_SHIP_PRICE == null) {
            BLUE_DART_SHIP_PRICE = Integer.parseInt(ServerProperties.getPropertyValue("BLUE_DART_SHIP_PRICE"));
            if (BLUE_DART_SHIP_PRICE == 0) {
                logger.error("Please set up BLUE_DART_SHIP_PRICE in api.ini");
                BLUE_DART_SHIP_PRICE = null;
            }
        }
        return BLUE_DART_SHIP_PRICE;
    }


    public static Integer getGiftBoxPrice() {
        if (GIFT_BOX_PRICE == null) {
            GIFT_BOX_PRICE = Integer.parseInt(ServerProperties.getPropertyValue("GIFT_BOX_PRICE"));
            if (GIFT_BOX_PRICE == 0) {
                logger.error("Please set up GIFT_BOX_PRICE in api.ini");
                GIFT_BOX_PRICE = null;
            }
        }
        return GIFT_BOX_PRICE;
    }

    public static String getGreetingCardSubCat() {
        if (GREETING_CARD_SUB_CAT == null) {
            GREETING_CARD_SUB_CAT = ServerProperties.getPropertyValue("GREETING_CARD_SUB_CAT");
            if (GREETING_CARD_SUB_CAT == null) {
                GREETING_CARD_SUB_CAT = "";
            }
        }
        return GREETING_CARD_SUB_CAT;
    }

    public static Integer getShippingStandard() {
        if (SHIPPING_STANDARD == null) {
            SHIPPING_STANDARD = Integer.parseInt(ServerProperties.getPropertyValue("SHIPPING_STANDARD"));
            if (SHIPPING_STANDARD < 0) {
                logger.error("Please set up SHIPPING_STANDARD in api.ini");
                SHIPPING_STANDARD = 0;
            }
        }
        return SHIPPING_STANDARD;
    }

    public static Integer getShippingFixed() {
        if (SHIPPING_FIXED == null) {
            SHIPPING_FIXED = Integer.parseInt(ServerProperties.getPropertyValue("SHIPPING_FIXED"));
            if (SHIPPING_FIXED < 0) {
                logger.error("Please set up SHIPPING_FIXED in api.ini");
                SHIPPING_FIXED = 0;
            }
        }
        return SHIPPING_FIXED;
    }

    public static Integer getShippingMidnight() {
        if (SHIPPING_MIDNIGHT == null) {
            SHIPPING_MIDNIGHT = Integer.parseInt(ServerProperties.getPropertyValue("SHIPPING_MIDNIGHT"));
            if (SHIPPING_MIDNIGHT < 0) {
                logger.error("Please set up SHIPPING_MIDNIGHT in api.ini");
                SHIPPING_MIDNIGHT = 0;
            }
        }
        return SHIPPING_MIDNIGHT;
    }

    public static Integer getShippingSameDay() {
        if (SHIPPING_SAME_DAY == null) {
            SHIPPING_SAME_DAY = Integer.parseInt(ServerProperties.getPropertyValue("SHIPPING_SAME_DAY"));
            if (SHIPPING_SAME_DAY < 0) {
                logger.error("Please set up SHIPPING_SAME_DAY in api.ini");
                SHIPPING_SAME_DAY = 0;
            }
        }
        return SHIPPING_SAME_DAY;
    }

    public static String getFreshdeskApiToken() {

        if (FRESHDESK_API_TOKEN == null || FRESHDESK_API_TOKEN.isEmpty()) {
            FRESHDESK_API_TOKEN = ServerProperties.getPropertyValue("FRESHDESK_API_TOKEN");
            if (FRESHDESK_API_TOKEN == null) {
                FRESHDESK_API_TOKEN = "";
            }
        }
        return FRESHDESK_API_TOKEN;
    }

    public static String getFreshdeskApiEndpoint() {

        if (FRESHDESK_API_ENDPOINT == null || FRESHDESK_API_ENDPOINT.isEmpty()) {
            FRESHDESK_API_ENDPOINT = ServerProperties.getPropertyValue("FRESHDESK_API_ENDPOINT");
            if (FRESHDESK_API_ENDPOINT == null) {
                FRESHDESK_API_ENDPOINT = "";
            }
        }
        return FRESHDESK_API_ENDPOINT;
    }

    public static String getFreshdeskApiPwd() {
        if (FRESHDESK_API_PWD == null || FRESHDESK_API_PWD.isEmpty()) {
            FRESHDESK_API_PWD = ServerProperties.getPropertyValue("FRESHDESK_API_PWD");
            if (FRESHDESK_API_PWD == null) {
                FRESHDESK_API_PWD = "";
            }
        }

        return FRESHDESK_API_PWD;
    }

    public static String getFreshdeskApiEmail() {
        if (FRESHDESK_API_EMAIL == null || FRESHDESK_API_EMAIL.isEmpty()) {
            FRESHDESK_API_EMAIL = ServerProperties.getPropertyValue("FRESHDESK_API_EMAIL");
            if (FRESHDESK_API_EMAIL == null) {
                FRESHDESK_API_EMAIL = "";
            }
        }

        return FRESHDESK_API_EMAIL;
    }

    public static List<String> getMimeTypes() {
        if (MIME_TYPES.size() == 0 || MIME_TYPES.isEmpty()) {
            MIME_TYPES = new ArrayList<>(Arrays.asList(ServerProperties.getPropertyValue("MIME_TYPES").split(",")));
            if (MIME_TYPES.size() == 0) {
                MIME_TYPES = new ArrayList<>();
            }
        }
        return MIME_TYPES;
    }

    public static List<String> getSupportedAuthKeys() {
        if (ALLOWED_AUTH_KEYS.size() == 0 || ALLOWED_AUTH_KEYS.isEmpty()) {
            ALLOWED_AUTH_KEYS = new ArrayList<>(Arrays.asList(ServerProperties.getPropertyValue("ALLOWED_AUTH_KEYS").split(",")));
            if (ALLOWED_AUTH_KEYS.size() == 0) {
                ALLOWED_AUTH_KEYS = new ArrayList<>();
            }
        }
        return ALLOWED_AUTH_KEYS;
    }

    public static String getAllowedOrigins() {
        if (ALLOWED_ORIGINS == null || ALLOWED_ORIGINS.isEmpty()) {
            ALLOWED_ORIGINS = ServerProperties.getPropertyValue("ALLOWED_ORIGINS");
            if (ALLOWED_ORIGINS == null) {
                ALLOWED_ORIGINS = "";
            }
        }
        return ALLOWED_ORIGINS;
    }

    public static String getVendorServiceUrl() {
        if (VENDOR_SERVICE_URL == null || VENDOR_SERVICE_URL.isEmpty()) {
            VENDOR_SERVICE_URL = ServerProperties.getPropertyValue("VENDOR_SERVICE_URL");
            if (VENDOR_SERVICE_URL == null) {
                VENDOR_SERVICE_URL = "";
            }
        }
        return VENDOR_SERVICE_URL;
    }

    public static String getAllowedMethods() {
        if (ALLOWED_METHODS == null || ALLOWED_METHODS.isEmpty()) {
            ALLOWED_METHODS = ServerProperties.getPropertyValue("ALLOWED_METHODS");
            if (ALLOWED_METHODS == null) {
                ALLOWED_METHODS = "";
            }
        }
        return ALLOWED_METHODS;
    }

    public static String getAllowedHeaders() {
        if (ALLOWED_HEADERS == null || ALLOWED_HEADERS.isEmpty()) {
            ALLOWED_HEADERS = ServerProperties.getPropertyValue("ALLOWED_HEADERS");
            if (ALLOWED_HEADERS == null) {
                ALLOWED_HEADERS = "";
            }
        }
        return ALLOWED_HEADERS;
    }

    public static String getAllowedHeadersForPreFlightCall() {
        if (ALLOWED_HEADERS_PREFLIGHTCALL == null || ALLOWED_HEADERS_PREFLIGHTCALL.isEmpty()) {
            ALLOWED_HEADERS_PREFLIGHTCALL = ServerProperties.getPropertyValue("ALLOWED_HEADERS_PREFLIGHTCALL");
            if (ALLOWED_HEADERS_PREFLIGHTCALL == null) {
                ALLOWED_HEADERS_PREFLIGHTCALL = "";
            }
        }
        return ALLOWED_HEADERS_PREFLIGHTCALL;
    }

    public static List<String> getDefinedRegion() {
        if (DEFINED_REGION.size() == 0 || DEFINED_REGION.isEmpty()) {
            DEFINED_REGION = new ArrayList<>(Arrays.asList(ServerProperties.getPropertyValue("DEFINED_REGION").split(",")));
            if (DEFINED_REGION.size() == 0) {
                DEFINED_REGION = new ArrayList<>();
            }
        }
        return DEFINED_REGION;
    }

    public static List<String> getDefinedLang() {
        if (DEFINED_LANG.size() == 0 || DEFINED_LANG.isEmpty()) {
            DEFINED_LANG = new ArrayList<>(Arrays.asList(ServerProperties.getPropertyValue("DEFINED_LANG").split(",")));
            if (DEFINED_LANG.size() == 0) {
                DEFINED_LANG = new ArrayList<>();
            }
        }
        return DEFINED_LANG;
    }

    public static String getCurrencyConversionFile() {
        if (CURRENCY_CONVERSION == null || CURRENCY_CONVERSION.isEmpty()) {
            CURRENCY_CONVERSION = ServerProperties.getPropertyValue("CURRENCY_CONVERSION");
            if (CURRENCY_CONVERSION == null) {
                CURRENCY_CONVERSION = "";
            }
        }
        return CURRENCY_CONVERSION;
    }

    public static String getMessageBrokerType() {
        if (MESSAGE_BROKER_TYPE == null || MESSAGE_BROKER_TYPE.isEmpty()) {
            MESSAGE_BROKER_TYPE = ServerProperties.getPropertyValue("MESSAGE_BROKER_TYPE");
            if (MESSAGE_BROKER_TYPE == null) {
                MESSAGE_BROKER_TYPE = "";
            }
        }
        return MESSAGE_BROKER_TYPE;
    }

    public static String getCategoryFile() {
        if (CATEGORY_FILE == null || CATEGORY_FILE.isEmpty()) {
            CATEGORY_FILE = ServerProperties.getPropertyValue("CATEGORY_FILE");
            if (CATEGORY_FILE == null) {
                CATEGORY_FILE = "";
            }
        }
        return CATEGORY_FILE;
    }

    public static String getAttrFieldFile() {
        if (ATTR_FIELD_FILE == null || ATTR_FIELD_FILE.isEmpty()) {
            ATTR_FIELD_FILE = ServerProperties.getPropertyValue("ATTR_FIELD_FILE");
            if (ATTR_FIELD_FILE == null) {
                ATTR_FIELD_FILE = "";
            }
        }
        return ATTR_FIELD_FILE;
    }

    public static String getContactSupport() {
        if (CONTACT_SUPPORT == null) {
            CONTACT_SUPPORT = ServerProperties.getPropertyValue("CONTACT_SUPPORT");
            if (CONTACT_SUPPORT == null) {
                CONTACT_SUPPORT = "";
            }
        }
        return CONTACT_SUPPORT;
    }
    
    public static String getHomeDirectory() {
        if (HOME_DIRECTORY == null) {
            HOME_DIRECTORY = ServerProperties.getPropertyValue("HOME_DIRECTORY");
            if (HOME_DIRECTORY == null) {
                HOME_DIRECTORY = "";
            }
        }
        return HOME_DIRECTORY;
    }
    
    public static String getCdnUrl() {
        if (CDN_URL == null) {
            CDN_URL = ServerProperties.getPropertyValue("CDN_URL");
            if (CDN_URL == null) {
                CDN_URL = "";
            }
        }
        return CDN_URL;
    }
    public static String getPrefix() {
        if (PREFIX == null) {
            PREFIX = ServerProperties.getPropertyValue("PREFIX");
            if (PREFIX == null) {
                PREFIX = "";
            }
        }
        return PREFIX;
    }
    public static String getSearchOrder() {
        if (SEARCHORDER == null) {
            SEARCHORDER = ServerProperties.getPropertyValue("SEARCHORDER");
            if (SEARCHORDER == null) {
                SEARCHORDER = "";
            }
        }
        return SEARCHORDER;
    }
    public static String getMyAccount() {
        if (MYACCOUNT == null) {
            MYACCOUNT = ServerProperties.getPropertyValue("MYACCOUNT");
            if (MYACCOUNT == null) {
                MYACCOUNT = "";
            }
        }
        return MYACCOUNT;
    }
    public static String getOrderHistory() {
        if (ORDERHISTORY == null) {
            ORDERHISTORY = ServerProperties.getPropertyValue("ORDERHISTORY");
            if (ORDERHISTORY == null) {
                ORDERHISTORY = "";
            }
        }
        return ORDERHISTORY;
    }
    public static String getBaseUrl() {
        if (BASEURL == null) {
            BASEURL = ServerProperties.getPropertyValue("BASEURL");
            if (BASEURL == null) {
                BASEURL = "";
            }
        }
        return BASEURL;
    }
    public static String getVendorPanelS3uploadBucketname() {
        if (VENDOR_PANEL_S3UPLOAD_BUCKETNAME == null) {
            VENDOR_PANEL_S3UPLOAD_BUCKETNAME = ServerProperties.getPropertyValue("VENDOR_PANEL_S3UPLOAD_BUCKETNAME");
            if (VENDOR_PANEL_S3UPLOAD_BUCKETNAME == null) {
                VENDOR_PANEL_S3UPLOAD_BUCKETNAME = "";
            }
        }
        return VENDOR_PANEL_S3UPLOAD_BUCKETNAME;
    }
    public static String getS3uploadAccessKey() {
        if (S3UPLOAD_ACCESS_KEY == null) {
            S3UPLOAD_ACCESS_KEY = ServerProperties.getPropertyValue("S3UPLOAD_ACCESS_KEY");
            if (S3UPLOAD_ACCESS_KEY == null) {
                S3UPLOAD_ACCESS_KEY = "";
            }
        }
        return S3UPLOAD_ACCESS_KEY;
    }
    public static String getS3uploadSecretKey() {
        if (S3UPLOAD_SECRET_KEY == null) {
            S3UPLOAD_SECRET_KEY = ServerProperties.getPropertyValue("S3UPLOAD_SECRET_KEY");
            if (S3UPLOAD_SECRET_KEY == null) {
                S3UPLOAD_SECRET_KEY = "";
            }
        }
        return S3UPLOAD_SECRET_KEY;
    }
    public static String getS3baseUrl() {
        if (S3BASE_URL == null) {
            S3BASE_URL = ServerProperties.getPropertyValue("S3BASE_URL");
            if (S3BASE_URL == null) {
                S3BASE_URL = "";
            }
        }
        return S3BASE_URL;
    }
}
