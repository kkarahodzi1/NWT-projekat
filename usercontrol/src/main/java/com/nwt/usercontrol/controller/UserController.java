package com.nwt.usercontrol.controller;

import java.util.Date;
import java.util.List;

import com.nwt.usercontrol.Exceptions.UserNotFoundException;
import com.nwt.usercontrol.model.User;
import com.nwt.usercontrol.repos.UserRepository;
import com.nwt.usercontrol.service.UserService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.nwt.notifications.AkcijaRequest;
import org.nwt.notifications.AkcijaServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.Path;

@RestController
@RequestMapping("/api")
public class UserController {

   // @Autowired
    private UserService serv;
    @Autowired
    UserController(UserService s)
    {
        serv = s;
    }

    UserController(){}

    // logging za gRPC
    public void LogAkcija(AkcijaRequest akcijaRequest)
    {
     ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8084)
             .usePlaintext()
             .build();

     AkcijaServiceGrpc.AkcijaServiceBlockingStub stub
             = AkcijaServiceGrpc.newBlockingStub(channel);
     stub.akcija(akcijaRequest);
     channel.shutdown();
    }

    // Dohvati sve korisnike
    @GetMapping("/users")
    ResponseEntity<List<User>> all()
    {
     ResponseEntity<List<User>> zahtjev = serv.getAll();

     LogAkcija(AkcijaRequest.newBuilder()
             .setMikroservis("UserControl")
             .setTip(AkcijaRequest.Tip.GET)
             .setResurs("Korisnici")
             .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
             .build());

        return zahtjev;
    }

    // Dodaj novog korisnika
    @PostMapping("/users")
    ResponseEntity<Object> newUser(@Valid @RequestBody User newUser)
    {
     ResponseEntity<Object> zahtjev = serv.addNew(newUser);

     LogAkcija(AkcijaRequest.newBuilder()
             .setMikroservis("UserControl")
             .setTip(AkcijaRequest.Tip.CREATE)
             .setResurs("Korisnik")
             .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
             .build());

        return zahtjev;
    }

    // Dohvati korisnika sa odgovarajucim ID
    @GetMapping("/users/{id}")
    ResponseEntity<Object>  one(@Min(1) @PathVariable Long id)
    {
     ResponseEntity<Object> zahtjev = serv.getOne(id);

     LogAkcija(AkcijaRequest.newBuilder()
             .setMikroservis("UserControl")
             .setTip(AkcijaRequest.Tip.GET)
             .setResurs("Korisnik")
             .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
             .build());
        return zahtjev;
    }

    // Update korisnika (ili dodavanje novog ako nema)
    @PutMapping("/users/{id}")
    ResponseEntity<Object> replaceUser(@Valid @RequestBody User newUser, @Min(1) @PathVariable Long id)
    {
     ResponseEntity<Object> zahtjev = serv.modify(newUser, id);

     LogAkcija(AkcijaRequest.newBuilder()
             .setMikroservis("UserControl")
             .setTip(AkcijaRequest.Tip.UPDATE)
             .setResurs("Korisnik")
             .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
             .build());

        return zahtjev;
    }

    // Brisanje korisnika
    @DeleteMapping("/users/{id}")
    ResponseEntity<Object> deleteUser(@Min(1) @PathVariable Long id)
    {
     ResponseEntity<Object> zahtjev = serv.softDelete(id);

     LogAkcija(AkcijaRequest.newBuilder()
             .setMikroservis("UserControl")
             .setTip(AkcijaRequest.Tip.DELETE)
             .setResurs("Korisnik")
             .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
             .build());

        return zahtjev;
    }


    @GetMapping("/users/billings/{id}")
    ResponseEntity<Object> getBillingsbyId(@PathVariable Long id)
    {
     ResponseEntity<Object> zahtjev = serv.getBillings(id);

     LogAkcija(AkcijaRequest.newBuilder()
             .setMikroservis("UserControl")
             .setTip(AkcijaRequest.Tip.GET)
             .setResurs("Billings")
             .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
             .build());

     return zahtjev;
    }

    // Da exception ne vrati http error 500 nego 400
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e)
    {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
