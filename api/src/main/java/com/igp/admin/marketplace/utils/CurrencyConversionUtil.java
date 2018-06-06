package com.igp.admin.marketplace.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igp.admin.marketplace.models.CurrencyMappingModel;
import com.igp.config.Environment;
import com.igp.config.RedisKeys;
import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suditi on 16/1/18.
 */
public class CurrencyConversionUtil {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionUtil.class);

    public CurrencyMappingModel getCurrencyRatesFromDatastore() {
        Map<String,BigDecimal> currencyRates = new HashMap<>();
        Connection connection = null;
        String statement;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        CurrencyMappingModel currencyMappingModel = new CurrencyMappingModel();
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "select * from currency ";
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String currencyCode = resultSet.getString("currency_iso_code");
                if(resultSet.getInt("base_cur_flag")==1){
                    currencyMappingModel.setBaseCurrency(currencyCode);
                }
                BigDecimal currencyValue = resultSet.getBigDecimal("currency_value");
                //    logger.debug(" currency code : " + currencyCode +" currency value : " +currencyValue);
                currencyRates.put(currencyCode,currencyValue);
            }
            currencyMappingModel.setCurrencies(currencyRates);
        } catch (Exception exception) {
            logger.error("Exception in getting currency values : "+ exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return currencyMappingModel;
    }

    public static BigDecimal getConvertedPrice(BigDecimal mrp, String fromCurrency, String toCurrency) {
        BigDecimal result = new BigDecimal(String.valueOf(mrp));
        try {
            CurrencyConversionUtil currencyConversion = new CurrencyConversionUtil();
            CurrencyMappingModel currencyMappingModel = currencyConversion.getCurrencyRatesFromDatastore();
            Map<String,BigDecimal> currencyRates =  currencyMappingModel.getCurrencies();
            String baseCurrency = currencyMappingModel.getBaseCurrency();
            if(!fromCurrency.equalsIgnoreCase(toCurrency)) {
                // given currency is not equals to required currency
                if (fromCurrency.equalsIgnoreCase(baseCurrency)) {
                    //  directly convert to ToCurrency
                    result = result.multiply(currencyRates.get(toCurrency)).setScale(2, RoundingMode.HALF_UP);
                } else {
                    BigDecimal fromToBase =  result.divide(currencyRates.get(fromCurrency),2, RoundingMode.HALF_UP);
                    result = fromToBase.multiply(currencyRates.get(toCurrency)).setScale(2, RoundingMode.HALF_UP);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug(" converted currency is : "+result);
        return result;
    }
}
