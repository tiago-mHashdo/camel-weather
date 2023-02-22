package org.acme.camel.utils;

public class AppUtils {
    private static final String EXCHANGE_PROPERTY_BASE_PROPERTY = "${exchangeProperty.";

    public static String getExchangeProperty(String propertyName) {
        return EXCHANGE_PROPERTY_BASE_PROPERTY + propertyName + "}";
    }

    public static String direct(String endpoint) {
        return "direct:" + endpoint;
    }
}
