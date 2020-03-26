package com.nwt.usercontrol;

import com.nwt.usercontrol.model.User;
import com.nwt.usercontrol.repos.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class UsercontrolApplication {

    private static final Logger log = LoggerFactory.getLogger(UsercontrolApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UsercontrolApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner demo(UserRepository repo) {
        return (args) -> {
            // save a few customers
                repo.save(new User("Mujo", "Mujic", "mujo@gmail.com", "123", 0));
                repo.save(new User("Todd", "Howard", "howard@bethesda.com", "itjustworks", 1));
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
    }*/
}
