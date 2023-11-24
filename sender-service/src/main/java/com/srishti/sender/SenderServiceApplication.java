package com.srishti.sender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
@EnableFeignClients
public class SenderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenderServiceApplication.class, args);
    }
}
