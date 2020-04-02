package com.nwt.usercontrol.service;

import com.nwt.usercontrol.model.User;
import com.nwt.usercontrol.repos.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {


    private UserRepository repo;

    UserService(UserRepository r){
        repo = r;
    }

    public ResponseEntity<List<User>>  getAll()
    {
        return new ResponseEntity<List<User>>(repo.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<User> addNew(User newUser)
    {
        return new ResponseEntity<User>(repo.save(newUser), HttpStatus.OK);
    }

    public ResponseEntity<User> getOne(Long id)
    {
        if(!repo.findById(id).isPresent()) return new ResponseEntity<> (HttpStatus.NOT_FOUND);

        return new ResponseEntity<User> (repo.findById(id).get(), HttpStatus.OK);
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

    public ResponseEntity<User> softDelete(Long id)
    {
        if(!repo.findById(id).isPresent()) return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        repo.softDeleteById(id, new Date());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
