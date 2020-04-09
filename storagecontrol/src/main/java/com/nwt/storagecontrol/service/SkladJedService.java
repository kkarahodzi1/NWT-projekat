package com.nwt.storagecontrol.service;

import com.nwt.storagecontrol.apiclient.BillingsClient;
import com.nwt.storagecontrol.apiclient.NotificationsClient;
import com.nwt.storagecontrol.apiclient.UsersClient;
import com.nwt.storagecontrol.model.*;
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

    NotificationsClient notificationsClient;
    BillingsClient billingsClient;
    UsersClient usersClient;

    SkladJedService(SkladJedRepository skladJedRepository, NotificationsClient notificationsClient,BillingsClient billingsClient, UsersClient usersClient)
    {
        this.skladJedRepository = skladJedRepository;
        this.notificationsClient = notificationsClient;
        this.billingsClient = billingsClient;
        this.usersClient = usersClient;
    }

    public ResponseEntity<Object> getAllSkladJedinice(Skladiste skladiste, Integer broj, Tipovi tip) //id skladista
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
                return new ResponseEntity<>("Tražene skladišne jedinice ne postoje",HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(skladisneJedinice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Greška pri dohvaćanju: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getSkladJediniceById(long id) {
        Optional<SkladisneJedinice> skladisneJediniceData = skladJedRepository.findById(id);

        if (skladisneJediniceData.isPresent()) {
            return new ResponseEntity<>(skladisneJediniceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ne postoji skladišna jedinica sa id = " + id,HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<Object> createSkladJedinice(String obj) {
        try
        {
            JsonParser springParser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = springParser.parseMap(obj);

            SkladisneJedinice _skladjed = skladJedRepository
                    .save(new SkladisneJedinice((Integer) map.get("broj"), skladisteRepository.findById(((Integer) map.get("skladiste")).longValue()).get(), tipoviRepository.findByNaziv(map.get("tip").toString())));
            return new ResponseEntity<>(_skladjed, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Neispravni podaci",HttpStatus.EXPECTATION_FAILED);
        }
    }


    public ResponseEntity<Object> updateSkladJedinice(long id, String tip)
    {
        Optional<SkladisneJedinice> skladisneJediniceData = skladJedRepository.findById(id);
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = springParser.parseMap(tip);

        if (skladisneJediniceData.isPresent()) {
            SkladisneJedinice _skladjed = skladisneJediniceData.get();
            _skladjed.setTip(tipoviRepository.findByNaziv(map.get("tip").toString()));
            _skladjed.setDatumModificiranja(new Date());

            List<Zakupnina> zakupnina = billingsClient.dohvatiZakupninu(id).getBody();
            User korisnik = usersClient.dohvatiKorisnika(zakupnina.get(0).getKorisnikId()).getBody();

            Poruka p = new Poruka(korisnik.getIme(), korisnik.getPrezime(), korisnik.getMail());
            p.setPoruka("Poštovani "+korisnik.getIme()+ " " + korisnik.getPrezime() +",\n\nObavještavamo Vas da se cijena skladišne jedinice " + _skladjed.getBroj() + " promjenila, i da sada iznosi " + _skladjed.getTip().getCijena() + "KM mjesečno počevši od idućeg mjeseca. Za sve dodatne obavijesti javite se na telefon 033/123-456\n\nLijep pozdrav,\nStorageWars Lockers");

            notificationsClient.posaljiObavijest(p);

            return new ResponseEntity<>(skladJedRepository.save(_skladjed), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ne postoji skladišna jedinica sa id = " + id,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> deleteSkladJedinice(long id) {
        try {
            skladJedRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Nemoguće je obrisati datu skladišnu jedinicu", HttpStatus.EXPECTATION_FAILED);
        }
    }

}
