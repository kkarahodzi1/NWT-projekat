package com.nwt.storagecontrol.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.repos.SkladisteRepository;
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
public class SkladisteController
{
    @Autowired
    SkladisteRepository skladisteRepository;

    @GetMapping("/skladista")
    public ResponseEntity<List<Skladiste>> getAllSkladista(@RequestParam(required = false) String adresa) {
        try {
            List<Skladiste> skladista = new ArrayList<Skladiste>();

            if (adresa == null)
                skladisteRepository.findAll().forEach(skladista::add);
            else
                skladista.add(skladisteRepository.findByAdresa(adresa));

            if (skladista.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(skladista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/skladista/{id}")
    public ResponseEntity<Skladiste> getSkladisteById(@PathVariable("id") long id) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        if (skladisteData.isPresent()) {
            return new ResponseEntity<>(skladisteData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/skladista")
    public ResponseEntity<Skladiste> createSkladiste(@RequestBody Skladiste skladista) {
        try {
            Skladiste _skladiste = skladisteRepository
                    .save(new Skladiste(skladista.getAdresa(), skladista.getBrojJedinica()));
            return new ResponseEntity<>(_skladiste, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping("/skladista/{id}")
    public ResponseEntity<Skladiste> updateSkladiste(@PathVariable("id") long id, @RequestBody Skladiste skladiste) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        if (skladisteData.isPresent()) {
            Skladiste _skladiste = skladisteData.get();
            _skladiste.setAdresa(skladiste.getAdresa());
            _skladiste.setBrojJedinica(skladiste.getBrojJedinica());
            _skladiste.setDatumModificiranja(new Date());
            return new ResponseEntity<>(skladisteRepository.save(_skladiste), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/skladista/{id}")
    public ResponseEntity<HttpStatus> deleteSkladiste(@PathVariable("id") long id) {
        try {
            skladisteRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


}
