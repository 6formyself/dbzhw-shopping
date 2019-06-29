package com.dbzhw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PayServer {
    public static void main(String[] args) {
        SpringApplication.run(PayServer.class, args);
    }
}
