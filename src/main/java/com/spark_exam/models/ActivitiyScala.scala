package com.spark_exam.models

case class ActivityScala(userId: Long,
                          name: String,
                          lastName: String,
                          countryOfOrigin: String,
                          eventId: Long,
                          eventTime: String,
                          eventCountry: String,
                          gameName: String,
                          onlineTimeSecs: Long,
                          currencyCode: String,
                          betValue: Double,
                          win: Double,
                          winBetRatio: Double,
                          profit: Double)
