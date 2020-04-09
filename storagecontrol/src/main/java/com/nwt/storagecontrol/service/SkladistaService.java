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


    public ResponseEntity<Object> getAllSkladista(String adresa) {
        try {
            List<Skladiste> skladista = new ArrayList<Skladiste>();

            if (adresa == null)
                skladisteRepository.findAll().forEach(skladista::add);
            else
                skladista.add(skladisteRepository.findByAdresa(adresa));

            if (skladista.isEmpty()) {
                return new ResponseEntity<>("Ne postoje tražena skladišta", HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(skladista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Serverska greška: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getSkladisteById(long id) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        if (skladisteData.isPresent()) {
            return new ResponseEntity<>(skladisteData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ne postoji skladište sa id = "+id,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> createSkladiste(Skladiste skladista) {
        try {
            Skladiste _skladiste = skladisteRepository
                    .save(new Skladiste(skladista.getAdresa(), skladista.getBrojJedinica()));
            return new ResponseEntity<>(_skladiste, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Neispravni podaci", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<Object> updateSkladiste(long id, Skladiste skladiste) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        if (skladisteData.isPresent()) {
            Skladiste _skladiste = skladisteData.get();
            _skladiste.setAdresa(skladiste.getAdresa());
            _skladiste.setBrojJedinica(skladiste.getBrojJedinica());
            _skladiste.setDatumModificiranja(new Date());
            return new ResponseEntity<>(skladisteRepository.save(_skladiste), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ne postoji skladište sa id = "+id,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> obrisiSkladiste(long id) {
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
                return new ResponseEntity<>("Nije moguće obrisati skladište",HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>("Ne postoji skladište sa id = "+id,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> vratiSkladiste(long id)
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
                return new ResponseEntity<>("Nije moguće vratiti skladište",HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>("Ne postoji skladište sa id = "+id,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> deleteSkladiste(long id) {
        try {
            skladisteRepository.deleteById(id);
            return new ResponseEntity<>("Skladište je uspješno obrisano",HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Greška pri brisanju: " + e.getMessage(),HttpStatus.EXPECTATION_FAILED);
        }
    }

}
