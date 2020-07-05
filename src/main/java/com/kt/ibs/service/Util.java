package com.kt.ibs.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class Util {

    public static String formatMoney(final String currency, final BigDecimal amount) {
        if ("ZAR".equals(currency)) {
            return NumberFormat.getCurrencyInstance(new Locale("en", "ZA"))
                    .format(amount);
        }
        return NumberFormat.getCurrencyInstance(new Locale("en", "US"))
                .format(amount);
    }
}
