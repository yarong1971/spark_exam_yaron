package com.spark_exam.config;

import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {
    @Bean
    public SparkSession sparkSession(){
        return SparkSession.builder().master("local[*]").appName("sparkExam").getOrCreate();
    }
}
