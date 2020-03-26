package com.nwt.billings.controller;

import com.nwt.billings.model.Zakupnina;
import com.nwt.billings.repos.ZakupninaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class BillingsController {

    @Autowired
    private ZakupninaRepo _repo;

    @GetMapping("/billings/{korisnikId}/korisnik")
    List<Zakupnina> pregledZakupninaKorisnika(@PathVariable Long korisnikId) {

        if (korisnikId == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pogresan id korisnika");
        }

        return _repo.findByKorisnikId(korisnikId);
    }

    @DeleteMapping("/billings/{id}")
    void ukloniZakupninu(@PathVariable Long id) {

        if (id == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pogresan id zakupnine");
        }

        _repo.softDeleteById(id, new Date());
    }

    @PostMapping("/billings")
    ResponseEntity<Zakupnina> kreirajZakupninu(@Valid @RequestBody Zakupnina zakupnina,
                                               @RequestHeader("pozivaoc-id") Long korisnikId,
                                               @RequestHeader("pozivaoc-rola") Boolean korisnikRola) {

        // provjeri da li je proslijedjeni id korisnika i id pozivaoca isti ili da li je pozivaoc admin
        if (korisnikId != zakupnina.getKorisnikId() && korisnikRola != Boolean.TRUE) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Korisnik nema pravo na kreiranje zakupnine pod tudjim id");
        }

        zakupnina.setDatumKreiranja(new Date());
        zakupnina.setDatumModificiranja(new Date());
        zakupnina.setObrisan(Boolean.FALSE);
        zakupnina.setPotvrdjeno(Boolean.FALSE);

        var datumSklapanja = zakupnina.getDatumSklapanjaUgovora();
        var datumRaskida = zakupnina.getDatumRaskidaUgovora();

        if (datumRaskida.before(datumSklapanja) || (datumRaskida.getTime() - datumSklapanja.getTime()) / (24 * 3600 * 1000) < 30) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Datum raskida mora biti minimalno 30 dana poslije datuma sklapanja ugovora");
        }

        return new ResponseEntity<Zakupnina>(_repo.save(zakupnina), HttpStatus.OK);
    }

    //region exception handler

    // validacija modela
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>("Model nije validan: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    //endregion
}
