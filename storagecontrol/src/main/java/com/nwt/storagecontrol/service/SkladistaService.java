package com.nwt.storagecontrol.service;

import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SkladistaService
{
    @Autowired
    SkladisteRepository skladisteRepository;

    SkladistaService(SkladisteRepository skladisteRepository)
    {
        this.skladisteRepository = skladisteRepository;
    }


    public ResponseEntity<List<Skladiste>> getAllSkladista(String adresa) {
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

    public ResponseEntity<Skladiste> getSkladisteById(long id) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        if (skladisteData.isPresent()) {
            return new ResponseEntity<>(skladisteData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Skladiste> createSkladiste(Skladiste skladista) {
        try {
            Skladiste _skladiste = skladisteRepository
                    .save(new Skladiste(skladista.getAdresa(), skladista.getBrojJedinica()));
            return new ResponseEntity<>(_skladiste, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<Skladiste> updateSkladiste(long id, Skladiste skladiste) {
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

    public ResponseEntity<Skladiste> obrisiSkladiste(long id) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        if (skladisteData.isPresent()) {
            Skladiste _skladiste = skladisteData.get();
            if(!_skladiste.getObrisan())
            {
                _skladiste.setDatumBrisanja(new Date());
                _skladiste.setObrisan(Boolean.TRUE);
                _skladiste.setDatumModificiranja(new Date());
                return new ResponseEntity<>(skladisteRepository.save(_skladiste), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Skladiste> vratiSkladiste(long id)
    {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        if (skladisteData.isPresent()) {
            Skladiste _skladiste = skladisteData.get();
            if(_skladiste.getObrisan())
            {
                _skladiste.setObrisan(Boolean.FALSE);
                _skladiste.setDatumModificiranja(new Date());
                return new ResponseEntity<>(skladisteRepository.save(_skladiste), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<HttpStatus> deleteSkladiste(long id) {
        try {
            skladisteRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

}
