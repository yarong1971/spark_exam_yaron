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
    private double betAvgStats;
    private double betMaxStats;
    private double betMinStats;
    private double winAvgStats;
    private double winMaxStats;
    private double winMinStats;
    private double profitAvgStats;
    private double profitMaxStats;
    private double profitMinStats;
}
