package com.nwt.storagecontrol.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.service.SkladistaService;
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


@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api")
public class SkladisteController
{
    @Autowired
    SkladistaService skladistaService;

    SkladisteController(SkladistaService skladistaService)
    {
        this.skladistaService = skladistaService;
    }

    @GetMapping("/skladista")
    public ResponseEntity<Object> getAllSkladista(@RequestParam(required = false) String adresa) {
        return skladistaService.getAllSkladista(adresa);
    }

    @GetMapping("/skladista/{id}")
    public ResponseEntity<Object> getSkladisteById(@PathVariable("id") long id) {
        return skladistaService.getSkladisteById(id);
    }

    @PostMapping("/skladista")
    public ResponseEntity<Object> createSkladiste(@RequestBody Skladiste skladista) {
        return skladistaService.createSkladiste(skladista);
    }

    @PutMapping("/skladista/{id}")
    public ResponseEntity<Object> updateSkladiste(@PathVariable("id") long id, @RequestBody Skladiste skladiste) {
        return skladistaService.updateSkladiste(id, skladiste);
    }

    @PutMapping("/skladista/obrisi/{id}")
    public ResponseEntity<Object> obrisiSkladiste(@PathVariable("id") long id) {
        return skladistaService.obrisiSkladiste(id);
    }

    @PutMapping("/skladista/vrati/{id}")
    public ResponseEntity<Object> vratiSkladiste(@PathVariable("id") long id) {
        return vratiSkladiste(id);
    }

    @DeleteMapping("/skladista/{id}")
    public ResponseEntity<Object> deleteSkladiste(@PathVariable("id") long id) {
        return skladistaService.deleteSkladiste(id);
    }


}
