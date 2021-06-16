package com.spark_exam.services;

import com.spark_exam.models.*;
import com.spark_exam.repositories.EventRepository;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

import static org.apache.spark.sql.functions.*;

@Service
@Transactional
public class EventService implements Serializable {
    public static final String USER_ID = "userId";
    public static final String ID = "id";
    public static final String EVENT_TIME = "eventTime";
    public static final String WIN = "win";
    public static final String BET_VALUE = "betValue";
    public static final String GAME_NAME = "gameName";
    public static final String DEMO = "-demo";
    public static final String COUNTRY_OF_ORIGIN = "countryOfOrigin";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String EVENT_CURRENCY_CODE = "eventCurrencyCode";
    public static final String BET = "bet";
    public static final double WIN_BET_RATIO_LIMIT = 0.1;
    public static final double CONVERSION_RATE = 1.1;
    public static final String EVENT_COUNTRY = "eventCountry";
    public static final String ONLINE_TIME_SECS = "onlineTimeSecs";
    public static final int ONLINE_TIME_SEC_LIMIT = 18000;
    public static final String WIN_BET_RATIO = "winBetRatio";
    public static final String PROFIT = "profit";
    private static final String EVENT_ID = "eventId";
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserService userService;

    private Dataset<Row> rows;

    public List<Activity> getSuspiciousActivities(String fromDate, String toDate){
        Dataset<Row> filteredActivities =  rows.filter(col(EVENT_TIME).between(fromDate, toDate)
                                            .and(col(PROFIT).gt(WIN_BET_RATIO_LIMIT))
                                            .and(col(ONLINE_TIME_SECS).gt(ONLINE_TIME_SEC_LIMIT)))
                                            .select(USER_ID, NAME, LAST_NAME, COUNTRY_OF_ORIGIN, EVENT_ID,
                                                    EVENT_TIME, EVENT_COUNTRY, GAME_NAME, ONLINE_TIME_SECS,
                                                    CURRENCY_CODE, BET_VALUE, WIN, WIN_BET_RATIO, PROFIT);

        Dataset<Row> users = filteredActivities.select(USER_ID,EVENT_COUNTRY)
                                            .groupBy(USER_ID)
                                            .agg(approx_count_distinct(EVENT_COUNTRY).alias("count"))
                                            .where(col("count").gt(1))
                                            .drop(col("count"))
                                            .withColumnRenamed(USER_ID, ID);

        users.show(false);

        Dataset<Row> suspicoiusActivities = filteredActivities.join(users,filteredActivities.col(USER_ID).equalTo(users.col(ID)),"inner")
                                                        .drop(col(ID))
                                                        .orderBy(USER_ID, EVENT_TIME);

        return eventRepository.getActivities(suspicoiusActivities);
    }

    public GameStat getGameStatistics(String gameName, String fromDate, String toDate) {
        Dataset<Row> gameStatistics = rows.filter(col(EVENT_TIME).between(fromDate, toDate)
                                          .and(col(GAME_NAME).equalTo(gameName)))
                                          .select(GAME_NAME, BET_VALUE, WIN, PROFIT)
                                          .groupBy(GAME_NAME)
                                          .agg(avg(BET_VALUE).alias("betAvgStats"),
                                               max(BET_VALUE).alias("betMaxStats"),
                                               min(BET_VALUE).alias("betMinStats"),
                                               avg(WIN).alias("winAvgStats"),
                                               max(WIN).alias("winMaxStats"),
                                               min(WIN).alias("winMinStats"),
                                               avg(PROFIT).alias("profitAvgStats"),
                                               max(PROFIT).alias("profitMaxStats"),
                                               min(PROFIT).alias("profitMinStats"))
                                          .orderBy(GAME_NAME);

        return eventRepository.getGameStats(gameStatistics).isEmpty() ?  null
                                                                      : eventRepository.getGameStats(gameStatistics).get(0);
    }

    public List<GameStat> getAllGamesStatistics(String fromDate, String toDate) {
        Dataset<Row> allGamesStatistics = rows.filter(col(EVENT_TIME).between(fromDate, toDate))
                                            .select(GAME_NAME, BET_VALUE, WIN, PROFIT)
                                            .groupBy(GAME_NAME)
                                            .agg(avg(BET_VALUE).alias("betAvgStats"),
                                                max(BET_VALUE).alias("betMaxStats"),
                                                min(BET_VALUE).alias("betMinStats"),
                                                avg(WIN).alias("winAvgStats"),
                                                max(WIN).alias("winMaxStats"),
                                                min(WIN).alias("winMinStats"),
                                                avg(PROFIT).alias("profitAvgStats"),
                                                max(PROFIT).alias("profitMaxStats"),
                                                min(PROFIT).alias("profitMinStats"))
                                            .orderBy(GAME_NAME);

        return eventRepository.getGameStats(allGamesStatistics);
    }

    @EventListener(ContextRefreshedEvent.class)
    public void joinEventsAndUsers(){
        if (rows == null){
            Dataset<Event> events = eventRepository.getEvents();
            Dataset<User> users = userService.getDatasetUsers();

            rows = events.join(users, events.col(USER_ID).equalTo(users.col(ID))).drop(col(ID))
                    .filter(not(col(GAME_NAME).contains(DEMO).and(col(COUNTRY_OF_ORIGIN).equalTo(Country.USA.toString()))))
                    .withColumn(CURRENCY_CODE, when(col(EVENT_CURRENCY_CODE).equalTo(Currency.EUR.toString()), Currency.USD.toString()).otherwise(col(EVENT_CURRENCY_CODE)))
                    .withColumn((BET_VALUE), when(col(EVENT_CURRENCY_CODE).equalTo(Currency.EUR.toString()), (col(BET).multiply(CONVERSION_RATE))).otherwise(col(BET)))
                    .withColumn(PROFIT, col(WIN).minus(col(BET_VALUE)))
                    .withColumn(WIN_BET_RATIO, col(WIN).divide(col(BET_VALUE)))
                    .drop(EVENT_CURRENCY_CODE);

            rows.persist();
        }
    }
}