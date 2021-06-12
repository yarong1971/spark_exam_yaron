package com.spark_exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private long eventId;
    private LocalDateTime eventTime;
    private Country eventCountry;
    private Currency eventCurrencyCode;
    private long userId;
    private double bet;
    private Game gameName;
    private double win;
    private long onlineTimeSecs;
}
