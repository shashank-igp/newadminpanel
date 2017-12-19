package com.igp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServerProperties {
    
    private static final Logger logger = LoggerFactory.getLogger(ServerProperties.class);
    
    private static Configuration configuration = null;
    
    static {
        configuration = new Configuration();
        try {
            configuration.initialize("serverProperty");
        } catch (IOException e) {
            logger.error("IOException in reading service property");
        }
    }
    
    public static String getPropertyValue(String property) {
        return configuration.getPropertyValue(property);
    }
}
