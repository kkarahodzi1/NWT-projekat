package com.nwt.notifications;

import com.google.protobuf.Timestamp;
import com.nwt.notifications.model.Akcija;
import com.nwt.notifications.repos.AkcijaRepository;
import com.nwt.notifications.server.AkcijaServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.nwt.notifications.AkcijaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.io.IOException;


@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class NotificationsApplication {


    private static final Logger log = LoggerFactory.getLogger(NotificationsApplication.class);
    public static void main(String[] args) throws InterruptedException
    {
        SpringApplication.run(NotificationsApplication.class, args);
    }


    @Bean
    public CommandLineRunner demo(AkcijaRepository repo)
    {
        return (args) -> {
            Server server = ServerBuilder
                    .forPort(8084)
                    .addService(new AkcijaServiceImpl(repo)).build();

            try
            {
                server.start();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            server.awaitTermination();

        };
    }

}