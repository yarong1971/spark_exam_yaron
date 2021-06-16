Name:
GameApplication

Description:
"GameApplication" is a REST API service performs data processing by request.
It processes events of users/players bets in various games.
The REST API is built on the Spring Framework, an application framework and inversion of control container for the Java platform
The business logic for this application is written both in Java and Scala

Data and Storage:
User/Player data is The data is loaded by a SQL script file named "users.sql", which resides in the resources folder, and stored in H2 database.
it is an open-source lightweight Java SQL database, which is configured to run as inmemory database.
The name of the database is "game_platform".

The H2 Database has a browser based console application, which enables you to view the data
to enter the H2 console go to: http://localhost:8080/h2-console
please make sure that the JDBC URL is set to jdbc:h2:mem:game_platform

Events data is stored in a json file named generted_event.json, which resides in the data folder
the events data is used as a big data platform to work with Spark SQL

Services:
The REST API exposes the following services (in Java and Scala):

Service 1: Suspicious activities of users/players:
a suspicious activity is defined by the following rules:
    a. User/Player made bets from different countries in the provided time period
    b. Win/Bet ratio is higher than 1/10
    c. User/Player online time is higher than 5 hours

The Http Request URL is:
Java: http://localhost:8080/event/suspiciousActivities/java/{fromDate}/{toDate}
Java Example: http://localhost:8080/event/suspiciousActivities/java/2018-01-01/2018-01-15

Scala: http://localhost:8080/event/suspiciousActivities/scala/{fromDate}/{toDate}
ScalaExample: http://localhost:8080/event/suspiciousActivities/scala/2018-01-01/2018-01-15

The Output (Http Response) is in a json format and its content looks as follow:

[
  {
    "userId": 1,
    "name": "Alex",
    "lastName": "Popov",
    "countryOfOrigin": "USA",
    "eventId": 6469,
    "eventTime": "2018-01-05T08:51:17 -02:00",
    "eventCountry": "USA",
    "gameName": "poker",
    "onlineTimeSecs": 2467488,
    "currencyCode": "USD",
    "betValue": 23.58,
    "win": 25.37,
    "winBetRatio": 1.0759117896522479,
    "profit": 1.7900000000000027
  },
  {
    "userId": 1,
    "name": "Alex",
    "lastName": "Popov",
    "countryOfOrigin": "USA",
    "eventId": 19237,
    "eventTime": "2018-01-13T08:56:16 -02:00",
    "eventCountry": "PL",
    "gameName": "poker",
    "onlineTimeSecs": 9297407,
    "currencyCode": "USD",
    "betValue": 11.748000000000001,
    "win": 34.31,
    "winBetRatio": 2.920497105890364,
    "profit": 22.562
  },
  ....
]

Service 2: Statistics for specific game by game name and time period
This service calculates the following statistics measurements
    a. Average, Maximum and Minimum bet for requested game in time period
    b. Average, Maximum and Minimum win for requested game in time period
    c. Average, Maximum and Minimum profit (win - bet) for requested game in time period

The Http Request URL is:
Java: http://localhost:8080/event/gamestat/java/{gameName}/{fromDate}/{toDate}
Java Example: http://localhost:8080/event/gamestat/java/poker/2018-01-01/2018-01-15

Scala: http://localhost:8080/event/gamestat/scala/{fromDate}/{toDate}
Scala Example: http://localhost:8080/event/gamestat/scala/poker/2018-01-01/2018-01-15

The Output (Http Response) is in a json format and its content looks as follow:

{
  "gameName": "poker",
  "betAvgStats": 50.29189787234042,
  "betMaxStats": 108.185,
  "betMinStats": 1.23,
  "winAvgStats": 29.867914893617044,
  "winMaxStats": 40.39,
  "winMinStats": 20.2,
  "profitAvgStats": -20.423982978723398,
  "profitMaxStats": 38.1,
  "profitMinStats": -83.164
}


Service 3: Statistics for all games by time period
This service calculates the following statistics measurements
    a. Average, Maximum and Minimum bet for all games in time period
    b. Average, Maximum and Minimum win for all games in time period
    c. Average, Maximum and Minimum profit (win - bet) for all games in time period

The Http Request URL is:
Java: http://localhost:8080/event/gamestat/all/java/{fromDate}/{toDate}
Java Example: http://localhost:8080/event/gamestat/all/java/2018-01-01/2018-01-15

Scala: http://localhost:8080/event/gamestat/all/scala/{fromDate}/{toDate}
Scala Example: http://localhost:8080/event/gamestat/all/scala/2018-01-01/2018-01-15

The Output (Http Response) is in a json format and its content looks as follow:

[
  {
    "gameName": "baccarat",
    "betAvgStats": 40.1528,
    "betMaxStats": 95.45800000000001,
    "betMinStats": 11.39,
    "winAvgStats": 25.121999999999996,
    "winMaxStats": 28.21,
    "winMinStats": 21.38,
    "profitAvgStats": -15.030800000000003,
    "profitMaxStats": 12.969999999999999,
    "profitMinStats": -74.07800000000002
  },
  {
    "gameName": "baccarat-demo",
    "betAvgStats": 46.5274,
    "betMaxStats": 90.76100000000001,
    "betMinStats": 26.39,
    "winAvgStats": 29.666000000000004,
    "winMaxStats": 34.2,
    "winMinStats": 22.22,
    "profitAvgStats": -16.861400000000003,
    "profitMaxStats": 2.9800000000000004,
    "profitMinStats": -68.54100000000001
  },
  ....
]

Web UI Tools:
The Game Application supports Swagger UI Documentation Tool for more interactive requests
To enter to the Swagger UI, go to: http://localhost:8080/swagger-ui.html#/



