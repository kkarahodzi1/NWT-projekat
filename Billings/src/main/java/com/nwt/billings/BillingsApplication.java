package com.nwt.billings;

import com.nwt.billings.model.Zakupnina;
import com.nwt.billings.repos.ZakupninaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class BillingsApplication {


    private static final Logger log = LoggerFactory.getLogger(BillingsApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(BillingsApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(ZakupninaRepo repo) {
        return (args) -> {
            // save a few customers

            repo.save(new Zakupnina((long) 231));
            repo.save(new Zakupnina((long) 3124));
            repo.save(new Zakupnina((long) 3243));


            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Zakupnina zakupnina : repo.findAll()) {
                log.info(zakupnina.toString());
            }
            log.info("");
        };
    }

}
