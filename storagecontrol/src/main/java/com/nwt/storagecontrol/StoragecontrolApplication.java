package com.nwt.storagecontrol;

import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StoragecontrolApplication {

    private static final Logger log = LoggerFactory.getLogger(StoragecontrolApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(StoragecontrolApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(SkladisteRepository repository) {
        return (args) -> {
            // save a few customers
            repository.save(new Skladiste("Prva avenija 1", 50));
            repository.save(new Skladiste("Druga avenija 2", 70));
            repository.save(new Skladiste("Treca avenija 1", 25));
            repository.save(new Skladiste("Cetvrta avenija 4", 18));

            // fetch all customers
            log.info("Skladiste poradjeno sa findAll():");
            log.info("-------------------------------");
            for (Skladiste skladiste : repository.findAll()) {
                log.info(skladiste.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            Skladiste skladiste = repository.findById(1L);
            log.info("Skladiste poradjeno sa findById(1L):");
            log.info("--------------------------------");
            log.info(skladiste.toString());
            log.info("");

            // fetch customers by last name
            log.info("Skladiste poradjeno sa  findByAdresa('Druga avenija 2'):");
            log.info("--------------------------------------------");
            repository.findByAdresa("Druga avenija 2").forEach(druga -> {
                log.info(druga.toString());
            });
            log.info("");
        };
    }

}
