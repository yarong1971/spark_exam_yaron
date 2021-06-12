package com.spark_exam.controllers;

import com.spark_exam.services.CsvUserService;
import com.spark_exam.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csvuser")
public class CsvUserController {
    @Autowired
    private CsvUserService csvUserService;
    @GetMapping("/index")
    public void index(){
        csvUserService.getUsersList();
    }
}
