package com.spark_exam.controllers;

import com.spark_exam.models.Activity;
import com.spark_exam.models.GameStat;
import com.spark_exam.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/suspicious/{fromDate}/{toDate}")
    public ResponseEntity<List<Activity>> suspiciousActivity(@PathVariable String fromDate, @PathVariable String toDate){
        List<Activity> activities = eventService.getSuspiciousActivities(fromDate, toDate);
        if(activities.size() > 0) {
            return ResponseEntity.ok(activities);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/gamestat/bet/{gameName}/{fromDate}/{toDate}")
    public ResponseEntity<List<GameStat>> gameBetStatistics(@PathVariable String gameName, @PathVariable String fromDate, @PathVariable String toDate){
        List<GameStat> gameStats = eventService.getGameBetStatistics(gameName, fromDate, toDate);
        if(gameStats.size() > 0) {
            return ResponseEntity.ok(gameStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/gamestat/win/{gameName}/{fromDate}/{toDate}")
    public ResponseEntity<List<GameStat>> gameWinStatistics(@PathVariable String gameName, @PathVariable String fromDate, @PathVariable String toDate){
        List<GameStat> gameStats = eventService.getGameWinStatistics(gameName, fromDate, toDate);
        if(gameStats.size() > 0) {
            return ResponseEntity.ok(gameStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/gamestat/profit/{gameName}/{fromDate}/{toDate}")
    public ResponseEntity<List<GameStat>> gameProfitStatistics(@PathVariable String gameName, @PathVariable String fromDate, @PathVariable String toDate){
        List<GameStat> gameStats = eventService.getGameProfitStatistics(gameName, fromDate, toDate);
        if(gameStats.size() > 0) {
            return ResponseEntity.ok(gameStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allgamesstat/bet/{fromDate}/{toDate}")
    public ResponseEntity<List<GameStat>> allGamesBetStatistics(@PathVariable String fromDate, @PathVariable String toDate){
        List<GameStat> allGamesStats = eventService.getAllGamesBetStatistics(fromDate, toDate);
        if(allGamesStats.size() > 0) {
            return ResponseEntity.ok(allGamesStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allgamesstat/win/{fromDate}/{toDate}")
    public ResponseEntity<List<GameStat>> allGamesWinStatistics(@PathVariable String fromDate, @PathVariable String toDate){
        List<GameStat> gameStats = eventService.getAllGamesWinStatistics(fromDate, toDate);
        if(gameStats.size() > 0) {
            return ResponseEntity.ok(gameStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allgamesstat/profit/{fromDate}/{toDate}")
    public ResponseEntity<List<GameStat>> allGamesProfitStatistics(@PathVariable String fromDate, @PathVariable String toDate){
        List<GameStat> gameStats = eventService.getAllGamesProfitStatistics(fromDate, toDate);
        if(gameStats.size() > 0) {
            return ResponseEntity.ok(gameStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}