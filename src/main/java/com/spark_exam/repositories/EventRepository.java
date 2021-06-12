package com.spark_exam.repositories;

import com.spark_exam.models.Country;
import com.spark_exam.models.Event;
import org.apache.spark.sql.*;
import org.apache.spark.sql.functions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventRepository {

    @Autowired
    private SparkSession sparkSession;

    public void displyEvents(){
        Encoder<Event> eventEncoder = Encoders.bean(Event.class);

        String jsonPath = "data/generated_event.json";

        // read JSON file to Dataset
        //Dataset<Event> ds = sparkSession.read().option("multiline", true).json(jsonPath).as(eventEncoder);
        Dataset<Row> ds = sparkSession.read().option("multiline", true).json(jsonPath);
        ds.show();
    }

    public Dataset<Event> getEvents(){
        Encoder<Event> eventEncoder = Encoders.bean(Event.class);
        String jsonPath = "data/generated_event.json";
        return sparkSession.read().option("multiline", true).json(jsonPath).as(eventEncoder);
        //return sparkSession.read().option("multiline", true).json(jsonPath);
    }
}
