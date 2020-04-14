package com.nwt.usercontrol.service;

import com.google.gson.Gson;
import com.nwt.usercontrol.apiClients.BillingsClient;
import com.nwt.usercontrol.apiClients.NotificationsClient;
import com.nwt.usercontrol.model.Poruka;
import com.nwt.usercontrol.model.User;
import com.nwt.usercontrol.repos.UserRepository;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.bouncycastle.asn1.bc.BCObjectIdentifiers.bc;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;
    @Autowired
    private NotificationsClient nc;
    @Autowired
    private BillingsClient bl;


    UserService(UserRepository r, NotificationsClient cl, BillingsClient b)
    {
        repo = r;
        nc = cl;
        bl = b;
    }

    public ResponseEntity<List<User>>  getAll()
    {
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Object> addNew(User newUser)
    {
        String ime = newUser.getIme();
        String prezime = newUser.getPrezime();
        String email = newUser.getMail();
        List<String> errmsgs = new ArrayList<String>();

        Gson gs = new Gson();
        HttpHeaders rsp = new HttpHeaders();
        rsp.set("Content-Type", "application/json");

        if(ime.equals("") || prezime.equals("") || email.equals(""))
        {
            if(ime.equals("")) errmsgs.add("ime");
            if(prezime.equals("")) errmsgs.add("prezime");
            if(email.equals("")) errmsgs.add("email");
            String msg = "{ \"errmsg\": " + gs.toJson(errmsgs) + "}";

            return new ResponseEntity<Object>(msg, rsp, HttpStatus.BAD_REQUEST);
        }

        List<User> lu = repo.findAll();
        for (User user : lu)
            if (email.equals(user.getMail()))
                return new ResponseEntity<Object>("{ \"errmsg\": \"Ova email adresa je zauzeta\"}", rsp, HttpStatus.BAD_REQUEST);

        Poruka p = new Poruka(ime, prezime, email);
        try {
            var res = nc.posaljiUspjesnaRegistracija(p);
        }
        catch(Exception e) {
            // ako se mail nije uspio poslati
            // sa ostatkom tima vidjeti sta raditi u tom slucaju
        }
        return new ResponseEntity<Object>(repo.save(newUser), rsp, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getOne(Long id)
    {
        HttpHeaders rsp = new HttpHeaders();
        rsp.set("Content-Type", "application/json");
        if(!repo.findById(id).isPresent()) return new ResponseEntity<Object> ("{ \"errmsg\": \"Ne postoji\" }", rsp, HttpStatus.NOT_FOUND);

        return new ResponseEntity<Object> (repo.findById(id).get(), HttpStatus.OK);
    }

    public ResponseEntity<Object> modify(User newUser, Long id)
    {
        List<String> errmsgs = new ArrayList<String>();

        Gson gs = new Gson();
        HttpHeaders rsp = new HttpHeaders();
        rsp.set("Content-Type", "application/json");

        if(newUser.getIme().equals("") || newUser.getPrezime().equals("") || newUser.getMail().equals(""))
        {
            if(newUser.getIme().equals("")) errmsgs.add("ime");
            if(newUser.getPrezime().equals("")) errmsgs.add("prezime");
            if(newUser.getMail().equals("")) errmsgs.add("email");
            String msg = "{ \"errmsg\": " + gs.toJson(errmsgs) + "}";

            return new ResponseEntity<Object>(msg, rsp, HttpStatus.BAD_REQUEST);
        }
        List<User> lu = repo.findAll();
        for (User user : lu)
            if (newUser.getMail().equals(user.getMail()))
                return new ResponseEntity<Object>("{ \"errmsg\": \"Ova email adresa je zauzeta\"}", rsp, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<Object>(repo.findById(id)
                .map(user -> {
                    user.setIme(newUser.getIme());
                    user.setPrezime(newUser.getPrezime());
                    user.setMail(newUser.getMail());
                    user.setPassword(newUser.getPassword());
                    user.setRole(newUser.getRole());
                    user.setDatumKreiranja(newUser.getDatumKreiranja());
                    user.setDatumModificiranja(newUser.getDatumModificiranja());
                    user.setDatumBrisanja(newUser.getDatumBrisanja());
                    user.setObrisan(newUser.getObrisan());
                    return repo.save(user);
                })
                .orElseGet(() -> { // ako ne postoji taj id
                    newUser.setId(id);
                    return repo.save(newUser);
                }), HttpStatus.OK);
    }

    public ResponseEntity<Object> softDelete(Long id)
    {
        HttpHeaders rsp = new HttpHeaders();
        rsp.set("Content-Type", "application/json");
        if(!repo.findById(id).isPresent()) return new ResponseEntity<Object> ("{ \"errmsg\": \"Ne postoji\" }", rsp, HttpStatus.NOT_FOUND);
        repo.softDeleteById(id, new Date());
        return new ResponseEntity<Object>("{ \"msg\": \"Korisnik uspjesno obrisan\" }", rsp, HttpStatus.OK);
    }

    public ResponseEntity<Object> getBillings(Long id)
    {
        HttpHeaders rsp = new HttpHeaders();
        rsp.set("Content-Type", "application/json");
        if(!repo.findById(id).isPresent()) return new ResponseEntity<Object> ("{ \"errmsg\": \"Ne postoji\" }", rsp, HttpStatus.NOT_FOUND);
        return bl.pregledZakupninaKorisnika(id);
    }


}
