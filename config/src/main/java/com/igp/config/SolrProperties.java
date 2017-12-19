package com.igp.config;

/**
 * Created by anjana on 31/3/16.
 */
public class SolrProperties {
    private static String ZOOKEEPER_URI = "";
    private static String SOLR_CARD_COLLECTION = "";
    private static String SOLR_PRODUCT_COLLECTION = "";
    private static String SOLR_PRODUCT_INSTANCE = "";
    private static String SOLR_CARD_INSTANCE = "";

    public static String getZookeeperURI() {
        if (ZOOKEEPER_URI == null || ZOOKEEPER_URI.isEmpty()) {
            ZOOKEEPER_URI = ServerProperties.getPropertyValue("ZOOKEEPER_URI");
            if (ZOOKEEPER_URI == null) {
                ZOOKEEPER_URI = "";
            }
        }
        return ZOOKEEPER_URI;
    }

    public static String getSolrCardCollection() {
        if (SOLR_CARD_COLLECTION == null || SOLR_CARD_COLLECTION.isEmpty()) {
            SOLR_CARD_COLLECTION = ServerProperties.getPropertyValue("SOLR_CARD_COLLECTION");
            if (SOLR_CARD_COLLECTION == null) {
                SOLR_CARD_COLLECTION = "";
            }
        }
        return SOLR_CARD_COLLECTION;
    }

    public static String getSolrProductCollection() {
        if (SOLR_PRODUCT_COLLECTION == null || SOLR_PRODUCT_COLLECTION.isEmpty()) {
            SOLR_PRODUCT_COLLECTION = ServerProperties.getPropertyValue("SOLR_PRODUCT_COLLECTION");
            if (SOLR_PRODUCT_COLLECTION == null) {
                SOLR_PRODUCT_COLLECTION = "";
            }
        }
        return SOLR_PRODUCT_COLLECTION;
    }

    public static String getSolrProductInstance() {
        if (SOLR_PRODUCT_INSTANCE == null || SOLR_PRODUCT_INSTANCE.isEmpty()) {
            SOLR_PRODUCT_INSTANCE = ServerProperties.getPropertyValue("SOLR_PRODUCT_INSTANCE");
            if (SOLR_PRODUCT_INSTANCE == null) {
                System.out.print("Please set up file path.");
                SOLR_PRODUCT_INSTANCE = null;
            }
        }
        return SOLR_PRODUCT_INSTANCE;
    }

    public static String getSolrCardInstance() {
        if (SOLR_CARD_INSTANCE == null || SOLR_CARD_INSTANCE.isEmpty()) {
            SOLR_CARD_INSTANCE = ServerProperties.getPropertyValue("SOLR_CARD_INSTANCE");
            if (SOLR_CARD_INSTANCE == null) {
                System.out.print("Please set up file path.");
                SOLR_CARD_INSTANCE = null;
            }
        }
        return SOLR_CARD_INSTANCE;
    }

}
