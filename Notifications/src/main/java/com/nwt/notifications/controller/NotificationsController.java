package com.nwt.notifications.controller;

import com.nwt.notifications.model.Poruka;
import com.nwt.notifications.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.SendFailedException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

@RestController
public class NotificationsController {

    @Autowired
    public NotificationService servis;

    @PostMapping("/notifications")
    public ResponseEntity<String> posaljiCustom(@Valid @RequestBody Poruka poruka) {

        if (poruka.getPoruka() == null || poruka.getPoruka().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nedostaje tekst poruke.");
        }

        servis.posaljiCustom(poruka);

        return new ResponseEntity<String>("Poslano", HttpStatus.OK);
    }

    @PostMapping("/notifications/registration-res")
    public ResponseEntity<String> posaljiUspjesnaRegistracija(@Valid @RequestBody Poruka poruka) {

        servis.posaljiUspjesnaRegistracija(poruka);

        return new ResponseEntity<String>("Poslano", HttpStatus.OK);
    }

    @PostMapping("/notifications/zakupnina-req")
    public ResponseEntity<String> posaljiZakupninaZahtjev(@Valid @RequestBody Poruka poruka, @RequestParam Long zakupninaId) {

        if (zakupninaId == null || zakupninaId == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nedostaje id zakupnine.");
        }

        servis.posaljiZakupninaZahtjev(poruka, zakupninaId);

        return new ResponseEntity<String>("Poslano", HttpStatus.OK);
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
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("Nije validno zbog: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SendFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<String> handleSendFailedException(SendFailedException e) {
        return new ResponseEntity<>("Slanje maila nije uspjelo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //endregion
}
