package com.nwt.storagecontrol.service;

import com.nwt.storagecontrol.apiclient.BillingsClient;
import com.nwt.storagecontrol.apiclient.NotificationsClient;
import com.nwt.storagecontrol.apiclient.UsersClient;
import com.nwt.storagecontrol.dto.Poruka;
import com.nwt.storagecontrol.dto.User;
import com.nwt.storagecontrol.dto.Zakupnina;
import com.nwt.storagecontrol.model.*;
import com.nwt.storagecontrol.repos.SkladJedRepository;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import com.nwt.storagecontrol.repos.TipoviRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkladJedService
{
    @Autowired
    private AmqpTemplate amqpTemplate;

    private String EXCHANGE = "storage-exchange";

    private String ROUTING_KEY = "storage";

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
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(skladisneJedinice, header, HttpStatus.OK);
        } catch (Exception e) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Greška na serveru\", \"original\":\""+e.getMessage()+"\"}",header, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getSkladJediniceById(long id) {
        Optional<SkladisneJedinice> skladisneJediniceData = skladJedRepository.findById(id);

        if (skladisneJediniceData.isPresent()) {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(skladisneJediniceData.get(), header, HttpStatus.OK);
        } else {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Ne postoji skladišna jedinica sa tim id\", \"id\": "+id+"}", header ,HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<Object> createSkladJedinice(String obj) {
        try
        {
            JsonParser springParser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = springParser.parseMap(obj);

            SkladisneJedinice _skladjed = skladJedRepository
                    .save(new SkladisneJedinice((Integer) map.get("broj"), skladisteRepository.findById(((Integer) map.get("skladiste")).longValue()).get(), tipoviRepository.findByNaziv(map.get("tip").toString())));
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(_skladjed, header, HttpStatus.CREATED);
        } catch (Exception e)  {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Proslijeđeni podaci nisu ispravni\", \"original\":\""+e.getMessage().replace("\"", "")+"\"}", header,HttpStatus.EXPECTATION_FAILED);
        }
    }


    public ResponseEntity<Object> updateSkladJedinice(long id, String tip)
    {
        try
        {
            Optional<SkladisneJedinice> skladisneJediniceData = skladJedRepository.findById(id);
            JsonParser springParser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = springParser.parseMap(tip);

            if (skladisneJediniceData.isPresent())
            {
                SkladisneJedinice _skladjed = skladisneJediniceData.get();
                _skladjed.setTip(tipoviRepository.findByNaziv(map.get("tip").toString()));
                _skladjed.setDatumModificiranja(new Date());

                List<Zakupnina> zakupnina = billingsClient.dohvatiZakupninu(id).getBody();
                User korisnik = usersClient.dohvatiKorisnika(zakupnina.get(0).getKorisnikId()).getBody();

                Poruka p = new Poruka(korisnik.getIme(), korisnik.getPrezime(), korisnik.getMail());
                p.setPoruka("Poštovani " + korisnik.getIme() + " " + korisnik.getPrezime() + ",\n\nObavještavamo Vas da se cijena skladišne jedinice " + _skladjed.getBroj() + " promjenila, i da sada iznosi " + _skladjed.getTip().getCijena() + "KM mjesečno počevši od idućeg mjeseca. Za sve dodatne obavijesti javite se na telefon 033/123-456\n\nLijep pozdrav,\nStorageWars Lockers");

                notificationsClient.posaljiObavijest(p);

                HttpHeaders header = new HttpHeaders();
                header.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>(skladJedRepository.save(_skladjed), header, HttpStatus.OK);
            } else
            {
                HttpHeaders header = new HttpHeaders();
                header.setContentType(MediaType.APPLICATION_JSON);
                return new ResponseEntity<>("{\"errmsg\" : \"Ne postoji skladišna jedinica sa tim id\", \"id\": " + id + "}", header, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e)
        {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Serverska greška\"}", header, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteSkladJedinice(long id) {
        try {

            SkladisneJedinice skladjed = skladJedRepository.findById(id).get();
            skladjed.setDatumBrisanja(new Date());
            skladjed.setObrisan(Boolean.TRUE);
            skladJedRepository.save(skladjed);
            String delBilling = "B" + id;
            amqpTemplate.convertAndSend(EXCHANGE, ROUTING_KEY,delBilling);
            return new ResponseEntity<>(HttpStatus.PROCESSING);
        } catch (Exception e)
        {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>("{\"errmsg\" : \"Greška pri brisanju\", \"original\":\"" + e.getMessage().replace("\"", "") + "\"}", header, HttpStatus.EXPECTATION_FAILED);
        }
    }

}
