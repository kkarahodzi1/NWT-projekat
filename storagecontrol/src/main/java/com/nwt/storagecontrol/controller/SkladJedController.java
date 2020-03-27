package com.nwt.storagecontrol.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.nwt.storagecontrol.StoragecontrolApplication;
import com.nwt.storagecontrol.model.SkladisneJedinice;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.SkladJedRepository;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    SkladJedRepository skladJedRepository;
    @Autowired
    SkladisteRepository skladisteRepository;

    @GetMapping("/skladjed")
    public ResponseEntity<List<SkladisneJedinice>> getAllSkladJedinice(@RequestParam(required = false) Skladiste skladiste, Integer broj) //id skladista
    {
        try {
            List<SkladisneJedinice> skladisneJedinice = new ArrayList<SkladisneJedinice>();

            if (skladiste != null)
                skladJedRepository.findBySkladiste(skladiste).forEach(skladisneJedinice::add);
            else if (broj != null)
                skladisneJedinice.add(skladJedRepository.findByBroj(broj));
            else
                skladJedRepository.findAll().forEach(skladisneJedinice::add);

            if (skladisneJedinice.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(skladisneJedinice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/skladjed/{id}")
    public ResponseEntity<SkladisneJedinice> getSkladJediniceById(@PathVariable("id") long id) {
        Optional<SkladisneJedinice> skladisneJediniceData = skladJedRepository.findById(id);

        if (skladisneJediniceData.isPresent()) {
            return new ResponseEntity<>(skladisneJediniceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/skladjed")
    public ResponseEntity<String> createSkladJedinice(@RequestParam Skladiste skladiste,@RequestParam Tipovi tip , Integer broj) {
        try {
            SkladisneJedinice _skladjed = skladJedRepository
                    .save(new SkladisneJedinice(broj,skladiste,tip));
            return new ResponseEntity<>(_skladjed.toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }


    @PutMapping("/skladjed/{id}")
    public ResponseEntity<SkladisneJedinice> updateSkladJedinice(@PathVariable("id") long id, @RequestParam Tipovi tip)
    {
        Optional<SkladisneJedinice> skladisneJediniceData = skladJedRepository.findById(id);

        if (skladisneJediniceData.isPresent()) {
            SkladisneJedinice _skladjed = skladisneJediniceData.get();
            _skladjed.setTip(tip);
            _skladjed.setDatumModificiranja(new Date());
            return new ResponseEntity<>(skladJedRepository.save(_skladjed), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/skladjed/{id}")
    public ResponseEntity<HttpStatus> deleteSkladJedinice(@PathVariable("id") long id) {
        try {
            skladJedRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


}
