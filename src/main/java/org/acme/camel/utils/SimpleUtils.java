package org.acme.camel.utils;

public class SimpleUtils {
    private static final String EXCHANGE_PROPERTY_BASE_PROPERTY = "${exchangeProperty.";

    public static String getExchangeProperty(String propertyName) {
        return EXCHANGE_PROPERTY_BASE_PROPERTY + propertyName + "}";
    }
}
