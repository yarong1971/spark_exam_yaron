package com.spark_exam.controllers;

import com.spark_exam.models.User;
import com.spark_exam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/index")
    public String index(){
        List<User> users = userService.getAllUsers();
        return "Hello " + users.get(1).getName();
    }
}
