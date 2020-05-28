package com.nwt.usercontrol;

import com.nwt.usercontrol.model.User;
import com.nwt.usercontrol.repos.UserRepository;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.nwt.notifications.AkcijaRequest;
import org.nwt.notifications.AkcijaResponse;
import org.nwt.notifications.AkcijaServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.Principal;
import java.util.Arrays;


@EnableAuthorizationServer
@EnableResourceServer
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class UsercontrolApplication {

    private static final Logger log = LoggerFactory.getLogger(UsercontrolApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(UsercontrolApplication.class, args);

        log.info("RADI");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8084)
                .usePlaintext()
                .build();

        AkcijaServiceGrpc.AkcijaServiceBlockingStub stub
                = AkcijaServiceGrpc.newBlockingStub(channel);


        AkcijaResponse akcijaResponse = stub.akcija(AkcijaRequest.newBuilder()
                .setMikroservis("Usercontrol")
                .setTip(AkcijaRequest.Tip.CREATE)
                .setResurs("Korisnik")
                .setOdgovor(AkcijaRequest.Odgovor.SUCCESS)
                .build());

        log.info(akcijaResponse.getOdgovor());

        channel.shutdown();

    }

    @Bean
    public CommandLineRunner demo(UserRepository repo) {
        return (args) -> {
            // save a few customers
                repo.save(new User("Mujo", "Mujic", "mujo@gmail.com", "123", 1));
                repo.save(new User("Todd", "Howard", "howard@bethesda.com", "itjustworks", 0));
            // fetch all customers
            log.info("Svi korisnici, findAll():");
            log.info("-------------------------------");
            for (User customer : repo.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            User customer = repo.findById(1L);
            log.info("Korisnik sa ID 1:");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");


            log.info("");
        };
    }


}
