package com.spark_exam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameStat {
    private String gameName;
    private String StatsType;
    private double avgStats;
    private double maxStats;
    private double minStats;
}
