package com.spark_exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class GameApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GameApplication.class, args);
        System.out.println();
    }
}
