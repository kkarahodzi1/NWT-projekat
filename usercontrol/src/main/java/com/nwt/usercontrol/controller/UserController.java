package com.nwt.usercontrol.controller;

import java.util.Date;
import java.util.List;

import com.nwt.usercontrol.Exceptions.UserNotFoundException;
import com.nwt.usercontrol.model.User;
import com.nwt.usercontrol.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api")
public class UserController {

   // @Autowired
    private UserRepository repo;

    UserController(UserRepository r){
        repo = r;
    }

    // Dohvati sve korisnike
    @GetMapping("/users")
    List<User> all() {
        return repo.findAll();
    }

    // Dodaj novog korisnika
    @PostMapping("/users")
    ResponseEntity<User> newUser(@Valid @RequestBody User newUser) {
        return new ResponseEntity<User>(repo.save(newUser), HttpStatus.OK);
    }

    // Dohvati korisnika sa odgovarajucim ID
    @GetMapping("/users/{id}")
    User one(@Min(1) @PathVariable Long id)
    {
        return repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // Update korisnika (ili dodavanje novog ako nema)
    @PutMapping("/users/{id}")
    ResponseEntity<User> replaceUser(@Valid @RequestBody User newUser, @Min(1) @PathVariable Long id) {

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

    // Brisanje korisnika
    @DeleteMapping("/users/{id}")
    void deleteUser(@Min(1) @PathVariable Long id) {
        //repo.deleteById(id); // ako je hard delete
        repo.softDeleteById(id, new Date());
    }

    // Da exception ne vrati http error 500 nego 400
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
