package com.nwt.usercontrol.service;

import com.nwt.usercontrol.apiClients.BillingsClient;
import com.nwt.usercontrol.apiClients.NotificationsClient;
import com.nwt.usercontrol.model.Poruka;
import com.nwt.usercontrol.model.User;
import com.nwt.usercontrol.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.bouncycastle.asn1.bc.BCObjectIdentifiers.bc;

@Service
public class UserService {


    private UserRepository repo;
    private NotificationsClient nc;
    private BillingsClient bl;
    UserService(UserRepository r, NotificationsClient cl, BillingsClient b)
    {
        repo = r;
        nc = cl;
        bl = b;
    }

    public ResponseEntity<List<User>>  getAll()
    {
        return new ResponseEntity<List<User>>(repo.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<User> addNew(User newUser)
    {
        String ime = newUser.getIme();
        String prezime = newUser.getPrezime();
        String email = newUser.getMail();
        Poruka p = new Poruka(ime, prezime, email);
        var res = nc.posaljiUspjesnaRegistracija(p);

        return new ResponseEntity<User>(repo.save(newUser), HttpStatus.OK);
    }

    public ResponseEntity<Object> getOne(Long id)
    {
        if(!repo.findById(id).isPresent()) return new ResponseEntity<Object> ("Ne postoji", HttpStatus.NOT_FOUND);

        return new ResponseEntity<Object> (repo.findById(id).get(), HttpStatus.CREATED);
    }

    public ResponseEntity<User> modify(User newUser, Long id)
    {
        return new ResponseEntity<User>(repo.findById(id)
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
        if(!repo.findById(id).isPresent()) return new ResponseEntity<Object> ("Korisnik sa ovim ID ne postoji", HttpStatus.NOT_FOUND);
        repo.softDeleteById(id, new Date());
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    public ResponseEntity<Object> getBillings(Long id)
    {
        if(!repo.findById(id).isPresent()) return new ResponseEntity<Object> ("Korisnik sa ovim ID ne postoji", HttpStatus.NOT_FOUND);
        return bl.pregledZakupninaKorisnika(id);
    }


}
