package com.spark_exam.repositories;

import com.spark_exam.models.Activity;
import com.spark_exam.models.Event;
import com.spark_exam.models.GameStat;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventRepository {

    public static final String MULTILINE = "multiline";

    @Autowired
    private SparkSession sparkSession;
    @Value("${app.events.source}")
    private String source;

    public Dataset<Event> getEvents(){
        Encoder<Event> eventEncoder = Encoders.bean(Event.class);
        return sparkSession.read().option(MULTILINE, true).json(source).as(eventEncoder);
    }

    public List<GameStat> getGameStats(Dataset<Row> dataset){
        Encoder<GameStat> gameStatEncoder = Encoders.bean(GameStat.class);
        return dataset.as(gameStatEncoder).collectAsList();
    }

    public List<Activity> getActivity(Dataset<Row> dataset){
        Encoder<Activity> activityEncoder = Encoders.bean(Activity.class);
        return dataset.as(activityEncoder).collectAsList();
    }
}
