package com.igp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anjana on 31/3/16.
 */
public class RedisProperties {
    private static final Logger logger = LoggerFactory.getLogger(RedisProperties.class);

    private static String REDIS_URI = "";
    private static Integer REDIS_PORT = null;

    public static String getRedisURI() {
        if (REDIS_URI == null || REDIS_URI.equals("")) {
            REDIS_URI = ServerProperties.getPropertyValue("REDIS_URI");
            if (REDIS_URI == null || REDIS_URI.isEmpty()) {
                logger.error("Please set up REDIS_URI in start.ini");
                REDIS_URI = "";
            }
        }
        return REDIS_URI;
    }

    public static Integer getRedisPort() {
        if (REDIS_PORT == null) {
            REDIS_PORT = Integer.parseInt(ServerProperties.getPropertyValue("REDIS_PORT"));
            if (REDIS_PORT == 0) {
                logger.error("Please set up REDIS_PORT in start.ini");
                REDIS_PORT = null;
            }
        }
        return REDIS_PORT;
    }
}
