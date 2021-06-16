package com.spark_exam.services

import com.spark_exam.helpers.DatasetHelper.DatasetExtension
import com.spark_exam.models.{ActivityScala, Country, Currency, Event, GameStatScala, User}
import com.spark_exam.repositories.EventRepository
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, Encoder, Encoders, Row, SparkSession}
import org.springframework.context.event.{ContextRefreshedEvent, EventListener}
import org.springframework.stereotype.Service


@Service
case class EventServiceScala(@transient userService: UserService, @transient eventRepository: EventRepository, @transient sparkSession: SparkSession) extends Serializable {
  final val USER_ID: String = "userId"
  final val ID: String = "id"
  final val EVENT_TIME: String = "eventTime"
  final val WIN: String = "win"
  final val BET_VALUE: String = "betValue"
  final val GAME_NAME: String = "gameName"
  final val DEMO: String = "-demo"
  final val COUNTRY_OF_ORIGIN: String = "countryOfOrigin"
  final val CURRENCY_CODE: String = "currencyCode"
  final val EVENT_CURRENCY_CODE: String = "eventCurrencyCode"
  final val BET: String = "bet"
  final val WIN_BET_RATIO_LIMIT: Double = 0.1
  final val CONVERSION_RATE: Double = 1.1
  final val EVENT_COUNTRY: String = "eventCountry"
  final val ONLINE_TIME_SECS: String = "onlineTimeSecs"
  final val ONLINE_TIME_SEC_LIMIT: Int = 18000
  final val WIN_BET_RATIO: String = "winBetRatio"
  final val PROFIT: String = "profit"
  final val EVENT_ID: String = "eventId"
  final val NAME: String = "name"
  final val LAST_NAME: String = "lastName"

  private var rows: Dataset[Row] = null

  def getSuspiciousActivities(fromDate: String, toDate: String): java.util.List[ActivityScala] = {
    val filteredActivities: Dataset[Row] = rows.filter(col(EVENT_TIME).between(fromDate, toDate)
                                               .and(col(PROFIT).gt(WIN_BET_RATIO_LIMIT))
                                               .and(col(ONLINE_TIME_SECS).gt(ONLINE_TIME_SEC_LIMIT)))
                                               .select(USER_ID, NAME, LAST_NAME, COUNTRY_OF_ORIGIN, EVENT_ID, EVENT_TIME, EVENT_COUNTRY,
                                                       GAME_NAME, ONLINE_TIME_SECS, CURRENCY_CODE, BET_VALUE, WIN, WIN_BET_RATIO, PROFIT)

    val users: Dataset[Row] = filteredActivities.select(USER_ID, EVENT_COUNTRY)
                                                .groupBy(USER_ID)
                                                .agg(approx_count_distinct(EVENT_COUNTRY).alias("count"))
                                                .where(col("count").gt(1))
                                                .drop(col("count"))
                                                .withColumnRenamed(USER_ID, ID)

    val suspicoiusActivities: Dataset[Row] = filteredActivities.join(users, filteredActivities.col(USER_ID).equalTo(users.col(ID)), "inner")
                                                               .drop(col(ID)).orderBy(USER_ID, EVENT_TIME)

    return suspicoiusActivities.toList[ActivityScala]()
  }

  def getGameStatistics(gameName: String, fromDate: String, toDate: String): GameStatScala = {
    val gameStatistics: Dataset[Row] = rows.filter(col(EVENT_TIME).between(fromDate, toDate)
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
                                                  .orderBy(GAME_NAME)

    return if (gameStatistics.isEmpty) null else return gameStatistics.toList[GameStatScala]().get(0)
  }

  def getAllGamesStatistics(fromDate: String, toDate: String): java.util.List[GameStatScala] = {
    val allGamesStatistics: Dataset[Row] = rows.filter(col(EVENT_TIME).between(fromDate, toDate))
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
                                               .orderBy(GAME_NAME)
    return allGamesStatistics.toList[GameStatScala]()
  }

  @EventListener(Array(classOf[ContextRefreshedEvent]))
  def joinEventsAndUsers(): Unit = {
    if (rows == null) {
      val events: Dataset[Event] = eventRepository.getEvents
      val users: Dataset[User] = userService.getDatasetUsers
      rows = events.join(users, events.col(USER_ID).equalTo(users.col(ID))).drop(col(ID))
                   .filter(not(col(GAME_NAME).contains(DEMO).and(col(COUNTRY_OF_ORIGIN).equalTo(Country.USA.toString))))
                   .withColumn(CURRENCY_CODE, when(col(EVENT_CURRENCY_CODE).equalTo(Currency.EUR.toString), Currency.USD.toString).otherwise(col(EVENT_CURRENCY_CODE)))
                   .withColumn((BET_VALUE), when(col(EVENT_CURRENCY_CODE).equalTo(Currency.EUR.toString), (col(BET).multiply(CONVERSION_RATE))).otherwise(col(BET)))
                   .withColumn(PROFIT, col(WIN).minus(col(BET_VALUE))).withColumn(WIN_BET_RATIO, col(WIN).divide(col(BET_VALUE))).drop(EVENT_CURRENCY_CODE)
      rows.persist
    }
  }
}
