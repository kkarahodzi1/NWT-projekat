package com.nwt.storagecontrol.service;


import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.TipoviRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TipoviService
{
    private TipoviRepository tipoviRepository;

    TipoviService(TipoviRepository tipoviRepository)
    {
        this.tipoviRepository = tipoviRepository;
    }

    public ResponseEntity<List<Tipovi>> getAll(String naziv)
    {
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

    public ResponseEntity<Tipovi> getById(long id)
    {
        Optional<Tipovi> tipovilData = tipoviRepository.findById(id);

        if (tipovilData.isPresent()) {
            return new ResponseEntity<>(tipovilData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<Tipovi> createTip(Tipovi tipovi) {
        try {
            Tipovi _tip = tipoviRepository
                    .save(new Tipovi(tipovi.getNaziv(), tipovi.getCijena()));
            return new ResponseEntity<>(_tip, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<HttpStatus> deleteByNaziv(String naziv) {
        try {
            tipoviRepository.deleteById(tipoviRepository.findByNaziv(naziv).getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


    public ResponseEntity<String> deleteTip(long id) {
        try {
            tipoviRepository.deleteById(id);
            return new ResponseEntity<>("Succesfully deleted",HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
}
