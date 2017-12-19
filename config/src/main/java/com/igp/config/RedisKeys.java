package com.igp.config;

/**
 * Created by anjana on 31/3/16.
 */
public class RedisKeys {

    private static String PRODUCT_KEY = "";
    private static String PERSONALIZATION_THEME_KEY = "";
    private static String PERSONALIZATION_KEY = "";
    private static String CITIES_KEY = "";
    private static String COUNTRIES_KEY = "";
    private static String EDITORS_PICK = "";
    private static String BEST_UNDER_999 = "";
    private static String ADDON_KEY = "";
    private static String BANNER_KEY = "";
    private static String RRO_URL_KEY = "";
    private static String BEST_SELLERS= "";

    public static String getProductKey() {
        if (PRODUCT_KEY == null || PRODUCT_KEY.isEmpty()) {
            PRODUCT_KEY = ServerProperties.getPropertyValue("PRODUCT_KEY");
            if (PRODUCT_KEY == null) {
                PRODUCT_KEY = "";
            }
        }
        return PRODUCT_KEY;
    }

    public static String getRroUrlKey() {
        if (RRO_URL_KEY == null || RRO_URL_KEY.isEmpty()) {
            RRO_URL_KEY = ServerProperties.getPropertyValue("RRO_URL_KEY");
            if (RRO_URL_KEY == null) {
                RRO_URL_KEY = "";
            }
        }
        return RRO_URL_KEY;
    }

    public static String getAddonKey() {
        if (ADDON_KEY == null || ADDON_KEY.isEmpty()) {
            ADDON_KEY = ServerProperties.getPropertyValue("ADDON_KEY");
            if (ADDON_KEY == null) {
                ADDON_KEY = "";
            }
        }
        return ADDON_KEY;
    }

    public static String getBannerKey() {
        if (BANNER_KEY == null || BANNER_KEY.isEmpty()) {
            BANNER_KEY = ServerProperties.getPropertyValue("BANNER_KEY");
            if (BANNER_KEY == null) {
                BANNER_KEY = "";
            }
        }
        return BANNER_KEY;
    }

    public static String getPersonalizationKey() {
        if (PERSONALIZATION_KEY == null || PERSONALIZATION_KEY.isEmpty()) {
            PERSONALIZATION_KEY = ServerProperties.getPropertyValue("PERSONALIZATION_KEY");
            if (PERSONALIZATION_KEY == null) {
                PERSONALIZATION_KEY = "";
            }
        }
        return PERSONALIZATION_KEY;
    }

    public static String getPersonalizationThemeKey() {
        if (PERSONALIZATION_THEME_KEY == null || PERSONALIZATION_THEME_KEY.isEmpty()) {
            PERSONALIZATION_THEME_KEY = ServerProperties.getPropertyValue("PERSONALIZATION_THEME_KEY");
            if (PERSONALIZATION_THEME_KEY == null) {
                PERSONALIZATION_THEME_KEY = "";
            }
        }
        return PERSONALIZATION_THEME_KEY;
    }

    public static String getCitiesKey() {
        if (CITIES_KEY == null || CITIES_KEY.isEmpty()) {
            CITIES_KEY = ServerProperties.getPropertyValue("CITIES_KEY");
            if (CITIES_KEY == null) {
                CITIES_KEY = "";
            }
        }
        return CITIES_KEY;
    }

    public static String getCountriesKey() {
        if (COUNTRIES_KEY == null || COUNTRIES_KEY.isEmpty()) {
            COUNTRIES_KEY = ServerProperties.getPropertyValue("COUNTRIES_KEY");
            if (COUNTRIES_KEY == null) {
                COUNTRIES_KEY = "";
            }
        }
        return COUNTRIES_KEY;
    }

    public static String getEditorsPick() {
        if (EDITORS_PICK == null || EDITORS_PICK.isEmpty()) {
            EDITORS_PICK = ServerProperties.getPropertyValue("EDITORS_PICK");
            if (EDITORS_PICK == null) {
                EDITORS_PICK = "";
            }
        }
        return EDITORS_PICK;
    }

    public static String getBestUnder999() {
        if (BEST_UNDER_999 == null || BEST_UNDER_999.isEmpty()) {
            BEST_UNDER_999 = ServerProperties.getPropertyValue("BEST_UNDER_999");
            if (BEST_UNDER_999 == null) {
                BEST_UNDER_999 = "";
            }
        }
        return BEST_UNDER_999;
    }
    public static String getBestSellers() {
        if (BEST_SELLERS == null || BEST_SELLERS.isEmpty()) {
            BEST_SELLERS = ServerProperties.getPropertyValue("BEST_SELLERS");
            if (BEST_SELLERS == null) {
                BEST_SELLERS = "";
            }
        }
        return BEST_SELLERS;
    }


}
