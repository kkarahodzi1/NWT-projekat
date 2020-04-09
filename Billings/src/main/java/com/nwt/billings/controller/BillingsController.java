package com.nwt.billings.controller;

import com.nwt.billings.apiclient.NotificationsKlijent;
import com.nwt.billings.apiclient.UserKlijent;
import com.nwt.billings.dto.Poruka;
import com.nwt.billings.dto.User;
import com.nwt.billings.helper.ValidacijskiHelper;
import com.nwt.billings.model.Zakupnina;
import com.nwt.billings.repos.ZakupninaRepo;
import com.nwt.billings.services.ZakupninaServis;
import feign.FeignException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class BillingsController {

    private ZakupninaServis servis;
    private NotificationsKlijent notificationsKlijent;
    private UserKlijent userKlijent;

    @Autowired
    public BillingsController(ZakupninaServis servis, NotificationsKlijent notificationsKlijent, UserKlijent userKlijent) {
        this.servis = servis;
        this.notificationsKlijent = notificationsKlijent;
        this.userKlijent = userKlijent;
    }

    @GetMapping("/billings/{korisnikId}/korisnik")
    List<Zakupnina> pregledZakupninaKorisnika(@PathVariable Long korisnikId) {

        if (korisnikId == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pogresan id korisnika");
        }

        return servis.dobaviZakupnineKorisnika(korisnikId);
    }

    @DeleteMapping("/billings/{id}")
    void ukloniZakupninu(@PathVariable Long id,
                         @RequestHeader("pozivaoc-id") Long korisnikId,
                         @RequestHeader("pozivaoc-rola") Boolean korisnikRola) {

        if (id == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pogresan id zakupnine");
        }

        ValidacijskiHelper.provjeriPristup(id, korisnikId, korisnikRola);

        servis.obrisiKorisnika(id);
    }

    @PostMapping("/billings")
    ResponseEntity<Zakupnina> kreirajZakupninu(@Valid @RequestBody Zakupnina zakupnina,
                                               @RequestHeader("pozivaoc-id") Long korisnikId,
                                               @RequestHeader("pozivaoc-rola") Boolean korisnikRola) {

        // provjeri da li je proslijedjeni id korisnika i id pozivaoca isti ili da li je pozivaoc admin
        ValidacijskiHelper.provjeriPristup(zakupnina.getKorisnikId(), korisnikId, korisnikRola);

        ValidacijskiHelper.provjeriDatum(zakupnina.getDatumSklapanjaUgovora(), zakupnina.getDatumRaskidaUgovora());

        // dobavi usera
        var userResponseEntity = userKlijent.getUser(korisnikId);
        if (userResponseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(
                    userResponseEntity.getStatusCode(), "Greska na user klijentu.");
        }
        User user = userResponseEntity.getBody();

        //kreiranje zakupnine
        var rez = servis.kreirajZakupninu(zakupnina);

        // posalji notifikaciju o kreiranju zakupnine
        var response = notificationsKlijent.posaljiZakupninaZahtjev(new Poruka(user.getIme(), user.getPrezime(), user.getMail()), rez.getId());

        if (response.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<Zakupnina>(rez,
                    HttpStatus.OK);
        }

        throw new ResponseStatusException(
                response.getStatusCode(), response.getBody());
    }

    @PutMapping("/billings")
    ResponseEntity<Zakupnina> promijeniZakupninu(@Valid @RequestBody Zakupnina zakupnina,
                                                 @RequestHeader("pozivaoc-id") Long korisnikId,
                                                 @RequestHeader("pozivaoc-rola") Boolean korisnikRola) {

        ValidacijskiHelper.provjeriPristup(zakupnina.getKorisnikId(), korisnikId, korisnikRola);

        ValidacijskiHelper.provjeriDatum(zakupnina.getDatumSklapanjaUgovora(), zakupnina.getDatumRaskidaUgovora());

        return new ResponseEntity<Zakupnina>(servis.kreirajZakupninu(zakupnina),
                HttpStatus.OK);
    }

    //region Exception handler

    // validacija modela
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("Model nije validan: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleResponseStatusExceptions(ResponseStatusException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Nije validno zbog: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    ResponseEntity<String> handleFeignStatusException(FeignException e) {
        return new ResponseEntity<>(e.contentUTF8(), HttpStatus.BAD_REQUEST);
    }
    //endregion
}
