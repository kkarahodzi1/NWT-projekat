package com.nwt.billings;

import com.nwt.billings.model.Zakupnina;
import com.nwt.billings.repos.ZakupninaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PopuniRepo {

    private static final Logger log = LoggerFactory.getLogger(BillingsApplication.class);

    @Bean
    CommandLineRunner initDatabase(ZakupninaRepo repository) {
        return args -> {log.info("started");};
    }
}
