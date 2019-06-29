package com.dbzhw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class WeixinApp {
    public static void main(String[] args) {
        SpringApplication.run(WeixinApp.class, args);
    }
}
