package com.spark_exam.models;

public enum Currency {
    USD("USD"),
    EUR("EUR");

    public final String eventCurrencyCode;

    private Currency(String eventCurrencyCode) {
        this.eventCurrencyCode = eventCurrencyCode;
    }
}
