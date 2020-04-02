package com.nwt.storagecontrol.service;

import com.nwt.storagecontrol.model.SkladisneJedinice;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.SkladJedRepository;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import com.nwt.storagecontrol.repos.TipoviRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Service
public class SkladJedService
{
    @Autowired
    SkladJedRepository skladJedRepository;
    @Autowired
    SkladisteRepository skladisteRepository;
    @Autowired
    TipoviRepository tipoviRepository;

    SkladJedService(SkladJedRepository skladJedRepository)
    {
        this.skladJedRepository = skladJedRepository;
    }

    public ResponseEntity<List<SkladisneJedinice>> getAllSkladJedinice(Skladiste skladiste, Integer broj, Tipovi tip) //id skladista
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

    public ResponseEntity<SkladisneJedinice> getSkladJediniceById(long id) {
        Optional<SkladisneJedinice> skladisneJediniceData = skladJedRepository.findById(id);

        if (skladisneJediniceData.isPresent()) {
            return new ResponseEntity<>(skladisneJediniceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<SkladisneJedinice> createSkladJedinice(String obj) {
        try
        {
            JsonParser springParser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = springParser.parseMap(obj);

            SkladisneJedinice _skladjed = skladJedRepository
                    .save(new SkladisneJedinice((Integer) map.get("broj"), skladisteRepository.findById(((Integer) map.get("skladiste")).longValue()).get(), tipoviRepository.findByNaziv(map.get("tip").toString())));
            return new ResponseEntity<>(_skladjed, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


    public ResponseEntity<SkladisneJedinice> updateSkladJedinice(long id, String tip)
    {
        Optional<SkladisneJedinice> skladisneJediniceData = skladJedRepository.findById(id);
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = springParser.parseMap(tip);

        if (skladisneJediniceData.isPresent()) {
            SkladisneJedinice _skladjed = skladisneJediniceData.get();
            _skladjed.setTip(tipoviRepository.findByNaziv(map.get("tip").toString()));
            _skladjed.setDatumModificiranja(new Date());
            return new ResponseEntity<>(skladJedRepository.save(_skladjed), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<HttpStatus> deleteSkladJedinice(long id) {
        try {
            skladJedRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

}
