package com.nwt.notifications.service;

import com.nwt.notifications.model.Akcija;
import com.nwt.notifications.repos.AkcijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AkcijaService {
    @Autowired
    AkcijaRepository akcijaRepository;

    AkcijaService(AkcijaRepository akcijaRepository)
    {
        this.akcijaRepository = akcijaRepository;
    }

    public ResponseEntity<Object> getAll()
    {
        try {
            List<Akcija> akcije = new ArrayList<Akcija>();

            akcijaRepository.findAll().forEach(akcije::add);

            if (akcije.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(akcije, header, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Greška na serveru\", \"original\":\""+e.getMessage()+"\"}",header, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
