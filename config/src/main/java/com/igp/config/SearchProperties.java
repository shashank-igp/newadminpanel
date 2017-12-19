package com.igp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lalchand Mali on 15/3/16.
 * TODO: Remove this file as it is not being used anywhere.
 */
public class SearchProperties {
    private static final Logger logger = LoggerFactory.getLogger(SearchProperties.class);

    private static String SEARCH_SORT = "";
    private static String SEARCH_FL = "";

    public static String getSearchFl() {
        if (SEARCH_FL.equals("")) {
            SEARCH_FL = ServerProperties.getPropertyValue("SEARCH_FL");
            if (SEARCH_FL == null || SEARCH_FL.isEmpty()) {
                logger.error("Please Setup Search Sort FL");
            }
        }
        return SEARCH_FL;
    }

    public static String getSearchSort() {
        if (SEARCH_SORT.equals("")) {
            SEARCH_SORT = ServerProperties.getPropertyValue("SEARCH_SORT");
            if (SEARCH_SORT == null || SEARCH_SORT.isEmpty()) {
                logger.error("Please Setup Search Sort Param");
            }
        }
        return SEARCH_SORT;
    }
}
