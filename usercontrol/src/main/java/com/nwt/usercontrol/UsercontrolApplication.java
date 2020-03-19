package com.nwt.usercontrol;

import com.nwt.usercontrol.model.Korisnik;
import com.nwt.usercontrol.repos.KorisnikRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class UsercontrolApplication {

    private static final Logger log = LoggerFactory.getLogger(UsercontrolApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UsercontrolApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(KorisnikRepository repository) {
        return (args) -> {
            // save a few customers
            repository.save(new Korisnik("Mujo", "Mujic", "mujo@gmail.com", "123", 0));
            repository.save(new Korisnik("Todd", "Howard", "howard@bethesda.com", "itjustworks", 1));
            // fetch all customers
            log.info("Svi korisnici, findAll():");
            log.info("-------------------------------");
            for (Korisnik customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            Korisnik customer = repository.findById(1L);
            log.info("Korisnik sa ID 1:");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");


            log.info("");
        };
    }
}
