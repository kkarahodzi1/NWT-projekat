package com.nwt.storagecontrol.service;

import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(skladista, header, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Greška na serveru\", \"original\":\""+e.getMessage()+"\"}",header, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getSkladisteById(long id) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        if (skladisteData.isPresent()) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(skladisteData.get(), header, HttpStatus.OK);
        } else {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Ne postoji skladište sa tim id\", \"id\": "+id+"}", header ,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> createSkladiste(Skladiste skladista) {
        try {
            Skladiste _skladiste = skladisteRepository
                    .save(new Skladiste(skladista.getAdresa(), skladista.getBrojJedinica()));
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(_skladiste, header, HttpStatus.CREATED);
        } catch (Exception e) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Proslijeđeni podaci nisu ispravni\", \"original\":\""+e.getMessage()+"\"}", header,HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<Object> updateSkladiste(long id, Skladiste skladiste) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        if (skladisteData.isPresent()) {
            Skladiste _skladiste = skladisteData.get();
            _skladiste.setAdresa(skladiste.getAdresa());
            _skladiste.setBrojJedinica(skladiste.getBrojJedinica());
            _skladiste.setDatumModificiranja(new Date());
            return new ResponseEntity<>(skladisteRepository.save(_skladiste), header, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"errmsg\" : \"Ne postoji skladište sa tim id\", \"id\": "+id+"}", header ,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> obrisiSkladiste(long id) {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        if (skladisteData.isPresent()) {
            Skladiste _skladiste = skladisteData.get();
            if(!_skladiste.getObrisan())
            {
                _skladiste.setDatumBrisanja(new Date());
                _skladiste.setObrisan(Boolean.TRUE);
                _skladiste.setDatumModificiranja(new Date());
                return new ResponseEntity<>(skladisteRepository.save(_skladiste), header, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("{\"errmsg\" : \"Skladište je već obrisano\", \"id\": "+id+"}", header ,HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("{\"errmsg\" : \"Ne postoji skladište sa tim id\", \"id\": "+id+"}", header ,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> vratiSkladiste(long id)
    {
        Optional<Skladiste> skladisteData = skladisteRepository.findById(id);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        if (skladisteData.isPresent()) {
            Skladiste _skladiste = skladisteData.get();
            if(_skladiste.getObrisan())
            {
                _skladiste.setObrisan(Boolean.FALSE);
                _skladiste.setDatumModificiranja(new Date());
                return new ResponseEntity<>(skladisteRepository.save(_skladiste), header, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("{\"errmsg\" : \"Skladište nije obrisano\", \"id\": "+id+"}", header ,HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("{\"errmsg\" : \"Ne postoji skladište sa tim id\", \"id\": "+id+"}", header ,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> deleteSkladiste(long id) {
        try
        {
            skladisteRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e)
        {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Greška pri brisanju\", \"original\":\"" + e.getMessage().replace("\"", "") + "\"}", header, HttpStatus.EXPECTATION_FAILED);
        }
    }

}
