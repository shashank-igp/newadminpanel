package com.igp.admin.marketplace.models;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by suditi on 6/6/18.
 */
public class CurrencyMappingModel {
    String baseCurrency;
    Map<String,BigDecimal> currencies;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Map<String, BigDecimal> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, BigDecimal> currencies) {
        this.currencies = currencies;
    }
}
