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

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class SkladJedController
{
    SkladJedService skladJedService;
    
    SkladJedController(SkladJedService skladJedService)
    {
        this.skladJedService = skladJedService;
    }

    @GetMapping("/skladjed")
    public ResponseEntity<List<SkladisneJedinice>> getAllSkladJedinice(@RequestParam(required = false) Skladiste skladiste, Integer broj, Tipovi tip) //id skladista
    {
        return skladJedService.getAllSkladJedinice(skladiste,broj,tip);
    }

    @GetMapping("/skladjed/{id}")
    public ResponseEntity<SkladisneJedinice> getSkladJediniceById(@PathVariable("id") long id)
    {
        return skladJedService.getSkladJediniceById(id);
    }


    @PostMapping("/skladjed")
    public ResponseEntity<SkladisneJedinice> createSkladJedinice(@RequestBody String obj)
    {
        return skladJedService.createSkladJedinice(obj);
    }


    @PutMapping("/skladjed/{id}")
    public ResponseEntity<SkladisneJedinice> updateSkladJedinice(@PathVariable("id") long id, @RequestBody String tip)
    {
        return skladJedService.updateSkladJedinice(id,tip);
    }


    @DeleteMapping("/skladjed/{id}")
    public ResponseEntity<HttpStatus> deleteSkladJedinice(@PathVariable("id") long id)
    {
        return skladJedService.deleteSkladJedinice(id);
    }


}
