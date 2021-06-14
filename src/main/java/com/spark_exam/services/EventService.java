package com.spark_exam.services;

import com.spark_exam.models.Activity;
import com.spark_exam.models.Event;
import com.spark_exam.models.GameStat;
import com.spark_exam.models.User;
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
    public static final String USA = "USA";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String EVENT_CURRENCY_CODE = "eventCurrencyCode";
    public static final String EUR = "EUR";
    public static final String USD = "USD";
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
    public static final String STATS_TYPE = "StatsType";

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SparkSession sparkSession;

    private Dataset<Row> rows;

    public List<Activity> getSuspiciousActivities(String fromDate, String toDate){
        rows.registerTempTable("Events");

        String cte_SuspiciousUsers = "WITH cte_SuspiciousUsers " +
                                        "AS " +
                                        "(SELECT e." + USER_ID +
                                        " FROM Events e " +
                                        " WHERE e." + WIN_BET_RATIO + " > " + WIN_BET_RATIO_LIMIT +
                                        " AND e." + ONLINE_TIME_SECS + " > " + ONLINE_TIME_SEC_LIMIT +
                                        " AND e." + EVENT_TIME + " BETWEEN '" + fromDate + "' AND '" + toDate + "'" +
                                        " GROUP BY e." + USER_ID +
                                        " HAVING COUNT(DISTINCT(e." + EVENT_COUNTRY + ")) > 1)";

        String suspiciousActivities = cte_SuspiciousUsers +
                                     " SELECT e." + USER_ID +
                                     ", CONCAT(e." + NAME + ",' ', e." + LAST_NAME + ") As userName" +
                                     ", e." + COUNTRY_OF_ORIGIN +
                                     ", e." + EVENT_ID +
                                     ", e." + EVENT_TIME +
                                     ", e." + EVENT_COUNTRY +
                                     ", e." + GAME_NAME +
                                     ", e." + ONLINE_TIME_SECS +
                                     ", e." + CURRENCY_CODE +
                                     ", e." + BET_VALUE +
                                     ", e." + WIN +
                                     ", e." + WIN_BET_RATIO +
                                     ", e." + PROFIT +
                                     " FROM Events e " +
                                     " WHERE e." + WIN_BET_RATIO + " > " + WIN_BET_RATIO_LIMIT +
                                     " AND e." + ONLINE_TIME_SECS + " > " + ONLINE_TIME_SEC_LIMIT +
                                     " AND e." + EVENT_TIME + " BETWEEN '" + fromDate + "' AND '" + toDate + "'" +
                                     " AND e." + USER_ID + " IN (SELECT cte.userId FROM cte_SuspiciousUsers cte) " +
                                     " ORDER BY e." + USER_ID + ", e." + EVENT_TIME;

        Dataset<Row> result = sparkSession.sql(suspiciousActivities);
        return eventRepository.getActivities(result);
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

        return eventRepository.getGameStats(gameStatistics).isEmpty() ?  null : eventRepository.getGameStats(gameStatistics).get(0);
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
                    .filter(not(col(GAME_NAME).contains(DEMO).and(col(COUNTRY_OF_ORIGIN).equalTo(USA))))
                    .withColumn(CURRENCY_CODE, when(col(EVENT_CURRENCY_CODE).equalTo(EUR), lit(USD)).otherwise(col(EVENT_CURRENCY_CODE)))
                    .withColumn((BET_VALUE), when(col(EVENT_CURRENCY_CODE).equalTo(EUR), (col(BET).multiply(CONVERSION_RATE))).otherwise(col(BET)))
                    .withColumn(PROFIT, col(WIN).minus(col(BET_VALUE)))
                    .withColumn(WIN_BET_RATIO, col(WIN).divide(col(BET_VALUE)))
                    .drop(EVENT_CURRENCY_CODE);

            rows.persist();
        }
    }
}