package com.igp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pranaya on 10/08/16.
 */
public class AttrFields {
    private static final Logger logger = LoggerFactory.getLogger(AttrFields.class);
    private static Set<String> attrFields = new HashSet<>();

    public Set<String> getAttrFields() {
        try {
            String file = Environment.getAttrFieldFile();
            String line = null;
            if (file != null) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    attrFields.add(line);
                }
                bufferedReader.close();
            }
        } catch (Exception e) {
            logger.error("Error in processing field file. {}", e);
        }
        return attrFields;
    }
}
