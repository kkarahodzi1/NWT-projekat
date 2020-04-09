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

    public ResponseEntity<Object> getAll(String naziv)
    {
        try {
            List<Tipovi> tipovi = new ArrayList<Tipovi>();

            if (naziv == null)
                tipoviRepository.findAll().forEach(tipovi::add);
            else
                tipovi.add(tipoviRepository.findByNaziv(naziv));

            if (tipovi.isEmpty()) {
                return new ResponseEntity<>("Traženi tipovi ne postoje",HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tipovi, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getById(long id)
    {
        Optional<Tipovi> tipovilData = tipoviRepository.findById(id);

        if (tipovilData.isPresent()) {
            return new ResponseEntity<>(tipovilData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ne postoji tip sa id = "+id,HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<Object> createTip(Tipovi tipovi) {
        try {
            Tipovi _tip = tipoviRepository
                    .save(new Tipovi(tipovi.getNaziv(), tipovi.getCijena()));
            return new ResponseEntity<>(_tip, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Proslijeđeni podaci nisu ispravni", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<Object> deleteByNaziv(String naziv) {
        try {
            tipoviRepository.deleteById(tipoviRepository.findByNaziv(naziv).getId());
            return new ResponseEntity<>("Uspješno obrisan tip sa nazivom: "+naziv, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Greška pri brisanju: " + e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }


    public ResponseEntity<Object> deleteTip(long id) {
        try {
            tipoviRepository.deleteById(id);
            return new ResponseEntity<>("Uspješno obrisano",HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Greška pri brisanju: " + e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }
}
