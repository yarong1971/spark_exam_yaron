package com.spark_exam.services;

import com.spark_exam.models.CsvUser;
import com.spark_exam.models.Currency;
import com.spark_exam.models.Event;
import com.spark_exam.repositories.CsvUserRepository;
import com.spark_exam.repositories.EventRepository;
import org.apache.spark.sql.Dataset;
import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class EventService implements Serializable {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CsvUserRepository csvUserRepository;
    @Autowired
    private SparkSession sparkSession;

    public void displayEvents(){
        eventRepository.getEvents();
    }
    public void displayUsers(){
        csvUserRepository.getCsvUsers().show();
    }

    public void joinUsersAndEvents() {
        Dataset<Event> events = eventRepository.getEvents();
        Dataset<CsvUser> users = csvUserRepository.getCsvUsers();

        Dataset<Row> rows = events.join(users, events.col("userId").equalTo(users.col("id"))).drop("id"); //.show(false);

        rows.filter(not(col("gameName").contains("-demo").and(col("countryOfOrigin").equalTo("USA"))));

        Dataset<Row> query1 = rows.except(rows.where(rows.col("gameName").contains("-demo")
                                              .and(rows.col("countryOfOrigin").equalTo("USA"))));

        Dataset<Row> query2 = rows.filter(col("eventCurrencyCode").notEqual("EUR"))
                                            .withColumnRenamed("eventCurrencyCode", "currencyCode")
                                            .withColumnRenamed("bet", "betValue")
                                            .select("eventId",
                                                    "eventTime",
                                                    "eventCountry",
                                                    "gameName",
                                                    "userId",
                                                    "name",
                                                    "lastName",
                                                    "countryOfOrigin",
                                                    "email",
                                                    "betValue",
                                                    "currencyCode",
                                                    "win")

        .unionAll(
         rows.where(col("eventCurrencyCode").equalTo("EUR"))
                //.withColumn("currencyCode", rows.col(functions.typedLit("USD"))
                .withColumn("eventCurrencyCode", lit("USD"))
                .withColumn("betValue", col("bet").divide(1.1))
                .drop("bet")
                .select("eventId",
                        "eventTime",
                        "eventCountry",
                        "gameName",
                        "userId",
                        "name",
                        "lastName",
                        "countryOfOrigin",
                        "email",
                        "betValue",
                        "currencyCode",
                        "win"));

                    //.withColumn("currencyCode", functions.typedLit("USD"))


        //betValue|eventCountry|currencyCode|eventId|           eventTime|      gameName|onlineTimeSecs|userId|  win|countryOfOrigin|               email| lastName|    name

        //query1.show();
        query2.show();
        //query3.show();
        //query2.union(query3).show();
    }
}
