package com.spark_exam.controllers;

import com.spark_exam.models.User;
import com.spark_exam.services.EventService;
import com.spark_exam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;
    @GetMapping("/index")
    public void index(){
        eventService.displayEvents();
        eventService.displayUsers();
    }
    @GetMapping("/join")
    public void join(){
        eventService.joinUsersAndEvents();
    }
}
