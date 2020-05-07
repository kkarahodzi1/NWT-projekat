package com.nwt.billings;

import com.nwt.billings.model.Zakupnina;
import com.nwt.billings.repos.ZakupninaRepo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.nwt.notifications.AkcijaRequest;
import org.nwt.notifications.AkcijaResponse;
import org.nwt.notifications.AkcijaServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Date;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class BillingsApplication {

    private static final Logger log = LoggerFactory.getLogger(BillingsApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(BillingsApplication.class, args);

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8084)
                .usePlaintext()
                .build();

        AkcijaServiceGrpc.AkcijaServiceBlockingStub stub
                = AkcijaServiceGrpc.newBlockingStub(channel);


        AkcijaResponse akcijaResponse = stub.akcija(AkcijaRequest.newBuilder()
                .setMikroservis("Billings")
                .setTip(AkcijaRequest.Tip.CREATE)
                .setResurs("Zakupnine")
                .setOdgovor(AkcijaRequest.Odgovor.SUCCESS)
                .build());

        log.info(akcijaResponse.getOdgovor());

        channel.shutdown();

    }

    @Bean
    public CommandLineRunner demo(ZakupninaRepo repo) {
        return (args) -> {
            // save a few customers
            repo.save(new Zakupnina((long)13, (long)2, (long)123, (long)2, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE, Boolean.FALSE, 100.2));
            repo.save(new Zakupnina((long)14, (long)2, (long)123, (long)2, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE, Boolean.FALSE, 120.5));
            repo.save(new Zakupnina((long)15, (long)1, (long)8, (long)1, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE, Boolean.FALSE, 120.5));
            // fetch all customers
            log.info("Sve zakupnine, findAll():");
            log.info("-------------------------------");
            for (Zakupnina zk : repo.findAll()) {
                log.info(zk.toString());
            }
            log.info("");
        };
    }
}


