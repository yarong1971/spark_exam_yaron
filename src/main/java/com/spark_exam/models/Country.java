package com.spark_exam.models;

public enum Country {
    USA("USA"),
    PL("PL"),
    DE("DE");

    public final String eventCountry;

    private Country(String eventCountry) {
        this.eventCountry = eventCountry;
    }
}
