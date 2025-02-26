package com.project1.ms_credit_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MsCreditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsCreditServiceApplication.class, args);
    }

}
