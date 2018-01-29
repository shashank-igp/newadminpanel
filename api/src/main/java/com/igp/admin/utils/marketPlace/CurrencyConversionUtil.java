package com.igp.admin.utils.marketPlace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igp.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by suditi on 16/1/18.
 */
public class CurrencyConversionUtil {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionUtil.class);

    private static CurrencyRates currencyRates;

    public CurrencyRates getCurrencyRates() throws IOException {
        if (currencyRates == null) {
            currencyRates = new ObjectMapper().readValue(new File(Environment.getCurrencyConversionFile()), CurrencyRates.class);
        }
        return currencyRates;
    }

    public static BigDecimal getUsdPrice(BigDecimal mrp, int baseCurrency) {
        CurrencyConversionUtil currencyConversion = new CurrencyConversionUtil();
        BigDecimal usd = new BigDecimal(String.valueOf(mrp));
        try {
            CurrencyRates currencyRates = currencyConversion.getCurrencyRates();
            if (baseCurrency == 2)
                usd = usd.divide(BigDecimal.valueOf(currencyRates.getCurrencyRatesINR().getInr()), 2, RoundingMode.HALF_UP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usd;
    }



    public static BigDecimal getInrPrice(BigDecimal mrp, int baseCurrency) {
        CurrencyConversionUtil currencyConversion = new CurrencyConversionUtil();
        BigDecimal inr = new BigDecimal(String.valueOf(mrp));
        try {
            CurrencyRates currencyRates = currencyConversion.getCurrencyRates();
            if (baseCurrency == 1)
                inr = inr.multiply(BigDecimal.valueOf(currencyRates.getCurrencyRatesINR().getInr()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inr;
    }
}
