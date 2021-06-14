package com.spark_exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity
{
    private long userId;
    private String userName;
    private String countryOfOrigin;
    private long eventId;
    private String eventTime;
    private String eventCountry;
    private String gameName;
    private long onlineTimeSecs;
    private String currencyCode;
    private double betValue;
    private double win;
    private double winBetRatio;
    private double profit;
}
