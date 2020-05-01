package com.nwt.storagecontrol;

import com.google.protobuf.Timestamp;
import com.nwt.storagecontrol.model.SkladisneJedinice;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.SkladJedRepository;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import com.nwt.storagecontrol.repos.TipoviRepository;
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
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class StoragecontrolApplication {


    private static final Logger log = LoggerFactory.getLogger(StoragecontrolApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(StoragecontrolApplication.class, args);

        log.info("RADI");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8084)
                .usePlaintext()
                .build();

        AkcijaServiceGrpc.AkcijaServiceBlockingStub stub
                = AkcijaServiceGrpc.newBlockingStub(channel);


        AkcijaResponse akcijaResponse = stub.akcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.CREATE)
                .setResurs("Skladista")
                .setOdgovor(AkcijaRequest.Odgovor.SUCCESS)
                .build());

        log.info(akcijaResponse.getOdgovor());

        channel.shutdown();
    }

    @Bean
    public CommandLineRunner demo(SkladisteRepository repository, SkladJedRepository repository2, TipoviRepository repository3) {
        return (args) -> {
            repository.save(new Skladiste("Prva avenija 1", 50));
            repository.save(new Skladiste("Druga avenija 2", 70));
            repository.save(new Skladiste("Treca avenija 1", 25));
            repository.save(new Skladiste("Cetvrta avenija 4", 18));

            repository3.save(new Tipovi("Veliko", 180f));
            repository3.save(new Tipovi("Malo", 90f));
            repository3.save(new Tipovi("Deluxe", 300f));

            Skladiste skladiste1 = repository.findById(1L).get();
            Skladiste skladiste2 = repository.findById(2L).get();
            Tipovi tip1 = repository3.findByNaziv("Veliko");
            Tipovi tip2 = repository3.findByNaziv("Malo");

            repository2.save(new SkladisneJedinice(154, skladiste1, tip1));
            repository2.save(new SkladisneJedinice(158, skladiste1, tip2));
            repository2.save(new SkladisneJedinice(128, skladiste2, tip1));

            log.info("Skladiste poradjeno sa findAll():");
            log.info("-------------------------------");
            for (Skladiste skladiste : repository.findAll()) {
                log.info(skladiste.toString());
            }
            log.info("");

            Skladiste skladiste = repository.findById(1L).get();
            log.info("Skladiste poradjeno sa findById(1L):");
            log.info("--------------------------------");
            log.info(skladiste.toString());
            log.info("");

            log.info("Skladiste poradjeno sa  findByAdresa('Druga avenija 2'):");
            log.info("--------------------------------------------");
            log.info(repository.findByAdresa("Druga avenija 2").toString());
            log.info("");


            log.info("Tip poradjen sa  findByNaziv('Veliko'):");
            log.info("--------------------------------------------");
            log.info(repository3.findByNaziv("Veliko").toString());
            log.info("");

            log.info("Tip poradjen sa findById(5L):");
            log.info("--------------------------------");
            log.info(repository3.findById(5L).toString());
            log.info("");

            log.info("Skladisna jedinica poradjena sa findBySkladiste(skladiste1):");
            log.info("--------------------------------------------");
            repository2.findBySkladiste(skladiste1).forEach(druga -> {
                log.info(druga.toString());
            });
            log.info("");


            log.info("Skladisna jedinica poradjena sa findByTip(tip1):");
            log.info("--------------------------------------------");
            repository2.findByTip(tip1).forEach(druga -> {
                log.info(druga.toString());
            });
            log.info("");



            log.info("Skladisna jedinica poradjena sa findByBroj(158):");
            log.info("--------------------------------------------");
            log.info(repository2.findByBroj(158).toString());
            log.info("");

            log.info("Skladisna jedinica poradjena sasa findById(8L):");
            log.info("--------------------------------");
            log.info(repository2.findById(8L).toString());
            log.info("");

        };
    }

}
