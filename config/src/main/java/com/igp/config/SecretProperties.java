package com.igp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by piyush on 22/2/16.
 */
public class SecretProperties {
    private static final Logger logger = LoggerFactory.getLogger(SecretProperties.class);
    
    private static String SBI_KEY = "";
    private static String PAYMENT_KEY = "";
    private static String UPI_KEY = "";
    private static String upiUrlForStatusCheck="";
    private static String upiUrlToInitiateTransaction="";
    private static String upiMerchantId="";
    private static String upiVpaCheckUrl="";
    
    public static String getPaymentKey() {
        if (PAYMENT_KEY.equals("")) {
            PAYMENT_KEY = ServerProperties.getPropertyValue("PAYMENT_KEY");
            if (PAYMENT_KEY == null || PAYMENT_KEY.isEmpty()) {
                logger.error("Please set up payment key for payment encryption");
            }
        }
        return PAYMENT_KEY;
    }
    
    public static String getSbiKey() {
        if (SBI_KEY.equals("")) {
            SBI_KEY = ServerProperties.getPropertyValue("SBI_KEY");
            if (SBI_KEY == null || SBI_KEY.isEmpty()) {
                logger.error("Please set up a valid sbi-encryption-key");
            }
        }
        return SBI_KEY;
    }
    public static String getUpiKey() {
        if (UPI_KEY.equals("")) {
            UPI_KEY = ServerProperties.getPropertyValue("UPI_KEY");
            if (UPI_KEY == null || UPI_KEY.isEmpty()) {
                logger.error("Please set up a valid upi-encryption-key");
            }
        }
        return UPI_KEY;
    }
    public static String getUpiUrlForStatusCheck() {
        if (upiUrlForStatusCheck.equals("")) {
            upiUrlForStatusCheck = ServerProperties.getPropertyValue("UPI_TRANSACTION_CHECK_URL");
            if (upiUrlForStatusCheck == null || upiUrlForStatusCheck.isEmpty()) {
                logger.error("Please set up a valid upi-status-check-url");
            }
        }
        return upiUrlForStatusCheck;
    }
    public static String getUpiUrlToInitiateTransaction() {
        if (upiUrlToInitiateTransaction.equals("")) {
            upiUrlToInitiateTransaction = ServerProperties.getPropertyValue("UPI_TRANSACTION_INITIATE_URL");
            if (upiUrlToInitiateTransaction == null || upiUrlToInitiateTransaction.isEmpty()) {
                logger.error("Please set up a valid upi-transaction-initiate-url");
            }
        }
        return upiUrlToInitiateTransaction;
    }
    public static String getUpiMerchantId() {
        if (upiMerchantId.equals("")) {
            upiMerchantId = ServerProperties.getPropertyValue("UPI_MERCHANT_ID");
            if (upiMerchantId == null || upiMerchantId.isEmpty()) {
                logger.error("Please set up a valid upi-Customer-Id");
            }
        }
        return upiMerchantId;
    }
    public static String getUpiVpaCheckUrl() {
        if (upiVpaCheckUrl.equals("")) {
            upiVpaCheckUrl = ServerProperties.getPropertyValue("UPI_VPA_CHECK_URL");
            if (upiVpaCheckUrl == null || upiVpaCheckUrl.isEmpty()) {
                logger.error("Please set up a valid upi-VPA-Check-url");
            }
        }
        return upiVpaCheckUrl;
    }
}
