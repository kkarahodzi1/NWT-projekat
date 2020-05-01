package com.nwt.storagecontrol.controller;

import java.util.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.nwt.storagecontrol.StoragecontrolApplication;
import com.nwt.storagecontrol.model.SkladisneJedinice;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.SkladJedRepository;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import com.nwt.storagecontrol.repos.TipoviRepository;
import com.nwt.storagecontrol.service.SkladJedService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.nwt.notifications.AkcijaRequest;
import org.nwt.notifications.AkcijaServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api")
public class SkladJedController
{
    SkladJedService skladJedService;

    SkladJedController(SkladJedService skladJedService)
    {
        this.skladJedService = skladJedService;
    }

    public void LogAkcija(AkcijaRequest akcijaRequest)
    {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8084)
                .usePlaintext()
                .build();

        AkcijaServiceGrpc.AkcijaServiceBlockingStub stub
                = AkcijaServiceGrpc.newBlockingStub(channel);
        stub.akcija(akcijaRequest);
        channel.shutdown();
    }

    @GetMapping("/skladjed")
    public ResponseEntity<Object> getAllSkladJedinice(@RequestParam(required = false) Skladiste skladiste, Integer broj, Tipovi tip) //id skladista
    {
        ResponseEntity<Object> zahtjev = skladJedService.getAllSkladJedinice(skladiste,broj,tip);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.GET)
                .setResurs("Skladisne jedinice")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @GetMapping("/skladjed/{id}")
    public ResponseEntity<Object> getSkladJediniceById(@PathVariable("id") long id)
    {
        ResponseEntity<Object> zahtjev = skladJedService.getSkladJediniceById(id);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.GET)
                .setResurs("Skladisne jedinice")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }


    @PostMapping("/skladjed")
    public ResponseEntity<Object> createSkladJedinice(@RequestBody String obj)
    {
        ResponseEntity<Object> zahtjev = skladJedService.createSkladJedinice(obj);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.CREATE)
                .setResurs("Skladisne jedinice")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }


    @PutMapping("/skladjed/{id}")
    public ResponseEntity<Object> updateSkladJedinice(@PathVariable("id") long id, @RequestBody String tip)
    {
        ResponseEntity<Object> zahtjev = skladJedService.updateSkladJedinice(id,tip);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.UPDATE)
                .setResurs("Skladisne jedinice")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }


    @DeleteMapping("/skladjed/{id}")
    public ResponseEntity<Object> deleteSkladJedinice(@PathVariable("id") long id)
    {
        ResponseEntity<Object> zahtjev = skladJedService.deleteSkladJedinice(id);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.DELETE)
                .setResurs("Skladisne jedinice")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }


}
