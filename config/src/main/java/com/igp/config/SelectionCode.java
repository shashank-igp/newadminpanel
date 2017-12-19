package com.igp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by anjana on 21/1/16.
 */
public class SelectionCode {
    private static final Logger logger = LoggerFactory.getLogger(SelectionCode.class);

    public static Properties properties = new Properties();
    private static InputStream inputStream = null;

    static {
        try {
            String propFileName = "codes.properties";
            inputStream = SelectionCode.class.getClassLoader().getResourceAsStream(propFileName);
            properties.load(inputStream);
        } catch (IOException ioException) {
            logger.error("Exception in reading the file: ", ioException);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    logger.error("Couldn't close the stream: ", ioException);
                }
            }
        }
    }

    public static String getValue(String prefix, String key) {
        if (properties != null)
            return properties.getProperty(prefix + "." + key);
        return null;
    }
}
