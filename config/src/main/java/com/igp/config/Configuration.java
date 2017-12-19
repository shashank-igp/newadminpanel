package com.igp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by anjana on 31/3/16.
 */
public class Configuration {
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    private Properties properties = new Properties();
    private InputStream inputStream = null;

    public void initialize(String propertyName) throws IOException {
        try {
            String property = System.getProperty(propertyName);
            if (property != null) {
                inputStream = new FileInputStream(property);
                properties.load(inputStream);
            }
        } catch (IOException ioException) {
            logger.error("Exception in reading the file: ", ioException);
            throw ioException;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    logger.error("Couldn't close the stream: ", ioException);
                    throw ioException;
                }
            }
        }
    }

    public String getPropertyValue(String property) {
        String value = properties.getProperty(property);
        if (value == null || value.isEmpty()) {
            logger.error("Please set up value of " + property + ".");
            value = "";
        }
        return value;
    }
}
