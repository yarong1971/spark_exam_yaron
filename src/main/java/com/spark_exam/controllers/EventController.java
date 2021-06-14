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

    @GetMapping("/gamestat/{gameName}/{fromDate}/{toDate}")
    public ResponseEntity<GameStat> gameBetStatistics(@PathVariable String gameName, @PathVariable String fromDate, @PathVariable String toDate){
        GameStat gameStats = eventService.getGameStatistics(gameName, fromDate, toDate);
        if(gameStats == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(gameStats);
        }
    }

    @GetMapping("/allgamesstat/{fromDate}/{toDate}")
    public ResponseEntity<List<GameStat>> allGamesBetStatistics(@PathVariable String fromDate, @PathVariable String toDate){
        List<GameStat> allGamesStats = eventService.getAllGamesStatistics(fromDate, toDate);
        if(allGamesStats.size() > 0) {
            return ResponseEntity.ok(allGamesStats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}