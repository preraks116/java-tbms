package com.tbms.util;

import java.util.Currency;

public class CurrencyUtils {

    public static Currency validateCurrencyCode(String currencyCode) {
        if (currencyCode == null || currencyCode.isEmpty()) {
            throw new IllegalArgumentException("Currency code cannot be null or empty");
        }
        try {
            return Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + currencyCode);
        }
    }

    public static void validateExchangeRateInput(String fromCode, String toCode, double rate) {
        validateCurrencyCode(fromCode);
        validateCurrencyCode(toCode);
        if (fromCode.equals(toCode)) {
            throw new IllegalArgumentException("Source and target currencies must be different");
        }
        if (rate <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }
    }

    public static String formatCurrencyPair(String fromCode, String toCode) {
        return fromCode + "/" + toCode;
    }

    public static String formatExchangeRate(double rate) {
        return String.format("%.4f", rate);
    }
}