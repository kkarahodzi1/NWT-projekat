package com.nwt.usercontrol.controller;

import java.util.Date;
import java.util.List;

import com.nwt.usercontrol.Exceptions.UserNotFoundException;
import com.nwt.usercontrol.model.User;
import com.nwt.usercontrol.repos.UserRepository;
import com.nwt.usercontrol.service.UserService;
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
    private UserService serv;
    UserController(UserService s)
    {
        serv = s;
    }

    // Dohvati sve korisnike
    @GetMapping("/users")
    ResponseEntity<List<User>> all()
    {
        return serv.getAll();
    }

    // Dodaj novog korisnika
    @PostMapping("/users")
    ResponseEntity<User> newUser(@Valid @RequestBody User newUser)
    {
        return serv.addNew(newUser);
    }

    // Dohvati korisnika sa odgovarajucim ID
    @GetMapping("/users/{id}")
    ResponseEntity<User>  one(@Min(1) @PathVariable Long id)
    {
        return serv.getOne(id);
    }

    // Update korisnika (ili dodavanje novog ako nema)
    @PutMapping("/users/{id}")
    ResponseEntity<User> replaceUser(@Valid @RequestBody User newUser, @Min(1) @PathVariable Long id)
    {
        return serv.modify(newUser, id);
    }

    // Brisanje korisnika
    @DeleteMapping("/users/{id}")
    ResponseEntity<User> deleteUser(@Min(1) @PathVariable Long id)
    {
        return serv.softDelete(id);
    }

    // Da exception ne vrati http error 500 nego 400
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e)
    {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
