package com.nwt.storagecontrol.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.TipoviRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TipoviController
{
    @Autowired
    TipoviRepository tipoviRepository;


    @GetMapping("/tipovi")
    public ResponseEntity<List<Tipovi>> getAllTipovi(@RequestParam(required = false) String naziv) {
        try {
            List<Tipovi> tipovi = new ArrayList<Tipovi>();

            if (naziv == null)
                tipoviRepository.findAll().forEach(tipovi::add);
            else
                tipovi.add(tipoviRepository.findByNaziv(naziv));

            if (tipovi.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tipovi, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tipovi/{id}")
    public ResponseEntity<Tipovi> getTipoviById(@PathVariable("id") long id) {
        Optional<Tipovi> tipovilData = tipoviRepository.findById(id);

        if (tipovilData.isPresent()) {
            return new ResponseEntity<>(tipovilData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tipovi")
    public ResponseEntity<Tipovi> createTip(@RequestBody Tipovi tipovi) {
        try {
            Tipovi _tip = tipoviRepository
                    .save(new Tipovi(tipovi.getNaziv(), tipovi.getCijena()));
            return new ResponseEntity<>(_tip, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/tipovi")
    public ResponseEntity<HttpStatus> deleteByNaziv(@RequestParam(required = true) String naziv) {
        try {
            tipoviRepository.deleteById(tipoviRepository.findByNaziv(naziv).getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


    //Dodatni response pored HTTP statusa
    @DeleteMapping("/tipovi/{id}")
    public ResponseEntity<String> deleteTip(@PathVariable("id") long id) {
        try {
            tipoviRepository.deleteById(id);
            return new ResponseEntity<>("Succesfully deleted",HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }


}
