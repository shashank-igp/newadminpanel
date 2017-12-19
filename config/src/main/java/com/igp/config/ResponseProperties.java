package com.igp.config;

/**
 * Created by anjana on 31/3/16.
 */
public class ResponseProperties {
    private static String ENTITY_CREATED = "";
    private static String ENTITY_CREATION_FAILED = "";
    private static String ENTITY_FOUND = "";
    private static String ENTITY_NOT_FOUND = "";
    private static String INVALID_AUTH_KEY = "";
    private static String MEDIA_NOT_SUPPORTED = "";
    private static String REGIONLANG_UNSUPPORTED = "";
    private static String VALIDATION_ERROR = "";
    private static String UNSUPPORTED_MEDIA = "";
    private static String RESOURCE_NOT_FOUND = "";
    
    public static String getEntityCreated() {
        if (ENTITY_CREATED == null || ENTITY_CREATED.isEmpty()) {
            ENTITY_CREATED = ServerProperties.getPropertyValue("ENTITY_CREATED");
            if (ENTITY_CREATED == null) {
                ENTITY_CREATED = "";
            }
        }
        return ENTITY_CREATED;
    }
    
    public static String getEntityCreationFailed() {
        if (ENTITY_CREATION_FAILED == null || ENTITY_CREATION_FAILED.isEmpty()) {
            ENTITY_CREATION_FAILED = ServerProperties.getPropertyValue("ENTITY_CREATION_FAILED");
            if (ENTITY_CREATION_FAILED == null) {
                ENTITY_CREATION_FAILED = "";
            }
        }
        return ENTITY_CREATION_FAILED;
    }
    
    public static String getEntityFound() {
        if (ENTITY_FOUND == null || ENTITY_FOUND.isEmpty()) {
            ENTITY_FOUND = ServerProperties.getPropertyValue("ENTITY_FOUND");
            if (ENTITY_FOUND == null) {
                ENTITY_FOUND = "";
            }
        }
        return ENTITY_FOUND;
    }
    
    public static String getEntityNotFound() {
        if (ENTITY_NOT_FOUND == null || ENTITY_NOT_FOUND.isEmpty()) {
            ENTITY_NOT_FOUND = ServerProperties.getPropertyValue("ENTITY_NOT_FOUND");
            if (ENTITY_NOT_FOUND == null) {
                ENTITY_NOT_FOUND = "";
            }
        }
        return ENTITY_NOT_FOUND;
    }
    
    public static String getMediaNotSupported() {
        if (MEDIA_NOT_SUPPORTED == null || MEDIA_NOT_SUPPORTED.isEmpty()) {
            MEDIA_NOT_SUPPORTED = ServerProperties.getPropertyValue("MEDIA_NOT_SUPPORTED");
            if (MEDIA_NOT_SUPPORTED == null) {
                MEDIA_NOT_SUPPORTED = "";
            }
        }
        return MEDIA_NOT_SUPPORTED;
    }
    
    public static String getRegionLangNotSupported() {
        if (REGIONLANG_UNSUPPORTED == null || REGIONLANG_UNSUPPORTED.isEmpty()) {
            REGIONLANG_UNSUPPORTED = ServerProperties.getPropertyValue("REGIONLANG_UNSUPPORTED");
            if (REGIONLANG_UNSUPPORTED == null) {
                REGIONLANG_UNSUPPORTED = "";
            }
        }
        return REGIONLANG_UNSUPPORTED;
    }
    
    public static String getAuthKeyNotSupported() {
        if (INVALID_AUTH_KEY == null || INVALID_AUTH_KEY.isEmpty()) {
            INVALID_AUTH_KEY = ServerProperties.getPropertyValue("INVALID_AUTH_KEY");
            if (INVALID_AUTH_KEY == null) {
                INVALID_AUTH_KEY = "";
            }
        }
        return INVALID_AUTH_KEY;
    }
    
    public static String getValidationError() {
        if (VALIDATION_ERROR == null || VALIDATION_ERROR.isEmpty()) {
            VALIDATION_ERROR = ServerProperties.getPropertyValue("VALIDATION_ERROR");
            if (VALIDATION_ERROR == null) {
                VALIDATION_ERROR = "";
            }
        }
        return VALIDATION_ERROR;
    }
    
    public static String getUnsupportedMedia() {
        if (UNSUPPORTED_MEDIA == null || UNSUPPORTED_MEDIA.isEmpty()) {
            UNSUPPORTED_MEDIA = ServerProperties.getPropertyValue("UNSUPPORTED_MEDIA");
            if (UNSUPPORTED_MEDIA == null) {
                UNSUPPORTED_MEDIA = "";
            }
        }
        return UNSUPPORTED_MEDIA;
    }
    
    public static String getResourceNotFound() {
        if (RESOURCE_NOT_FOUND == null || RESOURCE_NOT_FOUND.isEmpty()) {
            RESOURCE_NOT_FOUND = ServerProperties.getPropertyValue("RESOURCE_NOT_FOUND");
            if (RESOURCE_NOT_FOUND == null) {
                RESOURCE_NOT_FOUND = "";
            }
        }
        return RESOURCE_NOT_FOUND;
    }
}
