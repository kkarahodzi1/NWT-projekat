package com.nwt.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class NotificationsApplication {


    private static final Logger log = LoggerFactory.getLogger(NotificationsApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(NotificationsApplication.class, args);
    }

}
