package com.igp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.igp.config.categories.json.CategoriesJsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class Categories {

    private static final Logger logger = LoggerFactory.getLogger(Categories.class);
    private static List<CategoriesJsonObject> categories;

    public List<CategoriesJsonObject> getRecords() {
        try {
            String propFileName = Environment.getCategoryFile();
            if (propFileName != null) {
                Reader bufferedReader = new BufferedReader(new FileReader(propFileName));
                ObjectMapper objectMapper = new ObjectMapper();
                CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, CategoriesJsonObject.class);
                categories = objectMapper.readValue(bufferedReader, collectionType);
                bufferedReader.close();
            }
        } catch (IOException ioException) {
            logger.error("Exception in reading the file: ", ioException);
        }
        return categories;
    }
}
