package com.srishti.subscriber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.swing.*;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SubscriberServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(SubscriberServiceApplication.class, args);
    }
}
