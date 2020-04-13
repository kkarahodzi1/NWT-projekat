package com.nwt.storagecontrol.service;


import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.TipoviRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
            if (tipovi.isEmpty() || tipovi.get(0) == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(tipovi, header, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Greška na serveru\", \"original\":\""+e.getMessage()+"\"}",header, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getById(long id)
    {
        Optional<Tipovi> tipovilData = tipoviRepository.findById(id);

        if (tipovilData.isPresent()) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(tipovilData.get(), header, HttpStatus.OK);
        } else {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Ne postoji tip sa tim id\", \"id\": "+id+"}", header ,HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<Object> createTip(Tipovi tipovi) {
        try {
            Tipovi _tip = tipoviRepository
                    .save(new Tipovi(tipovi.getNaziv(), tipovi.getCijena()));
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(_tip, header, HttpStatus.CREATED);
        } catch (Exception e) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Proslijeđeni podaci nisu ispravni\", \"original\":\""+e.getMessage()+"\"}",header, HttpStatus.EXPECTATION_FAILED);
        }
    }

    public ResponseEntity<Object> deleteByNaziv(String naziv) {
        try {
            Tipovi tip = tipoviRepository.findByNaziv(naziv);
            if(tip == null)
            {
                HttpHeaders header = new HttpHeaders();
                header.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>("{\"errmsg\" : \"Ne postoji tip sa tim nazivom\", \"naziv\":\"" + naziv + "\"}", header, HttpStatus.EXPECTATION_FAILED);
            }

            tipoviRepository.deleteById(tip.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Greška pri brisanju\", \"original\":\""+e.getMessage()+"\"}",header,HttpStatus.EXPECTATION_FAILED);
        }
    }


    public ResponseEntity<Object> deleteTip(long id) {
            try
            {
                tipoviRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception e)
            {
                HttpHeaders header = new HttpHeaders();
                header.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>("{\"errmsg\" : \"Greška pri brisanju\", \"original\":\"" + e.getMessage().replace("\"", "") + "\"}", header, HttpStatus.EXPECTATION_FAILED);
            }
    }
}
