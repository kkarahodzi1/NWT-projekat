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
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.json.JSONObject;
import org.nwt.notifications.AkcijaRequest;
import org.nwt.notifications.AkcijaServiceGrpc;
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

    @GetMapping("/billings/{korisnikId}/korisnik")
    List<Zakupnina> pregledZakupninaKorisnika(@PathVariable Long korisnikId) {

        if (korisnikId == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pogresan id korisnika");
        }

        var response = servis.dobaviZakupnineKorisnika(korisnikId);

        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Billings")
                .setTip(AkcijaRequest.Tip.GET)
                .setResurs("Zakupnine")
                .setOdgovor(AkcijaRequest.Odgovor.SUCCESS)
                .build());

        return response;
    }

    @GetMapping("/billings/{jedinicaId}/jedinica")
    List<Zakupnina> pregledZakupninaPoJedinici(@PathVariable Long jedinicaId) {

        if (jedinicaId == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pogresan id jedinice.");
        }

        var response =  servis.dobaviZakupninePoJedinici(jedinicaId);

        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Billings")
                .setTip(AkcijaRequest.Tip.GET)
                .setResurs("Zakupnine")
                .setOdgovor(AkcijaRequest.Odgovor.SUCCESS)
                .build());

        return response;
    }

    @DeleteMapping("/billings/{id}")
    void ukloniZakupninu(@PathVariable Long id//,
                        /* @RequestHeader("pozivaoc-id") Long korisnikId,
                         @RequestHeader("pozivaoc-rola") Boolean korisnikRola*/) {

        if (id == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Pogresan id zakupnine");
        }

       // ValidacijskiHelper.provjeriPristup(id, korisnikId, korisnikRola);

        try
        {
            servis.obrisiZakupninu(id);
        }
        catch(Exception e){
            LogAkcija(AkcijaRequest.newBuilder()
                    .setMikroservis("Billings")
                    .setTip(AkcijaRequest.Tip.DELETE)
                    .setResurs("Zakupnine")
                    .setOdgovor(AkcijaRequest.Odgovor.FAILURE)
                    .build());
        }

        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Billings")
                .setTip(AkcijaRequest.Tip.DELETE)
                .setResurs("Zakupnine")
                .setOdgovor(AkcijaRequest.Odgovor.SUCCESS)
                .build());
    }

    @PostMapping("/billings")
    ResponseEntity<Zakupnina> kreirajZakupninu(@Valid @RequestBody Zakupnina zakupnina) {

        ValidacijskiHelper.provjeriDatum(zakupnina.getDatumSklapanjaUgovora(), zakupnina.getDatumRaskidaUgovora());

        // dobavi usera
        var userResponseEntity = userKlijent.getUser(zakupnina.getKorisnikId());
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

            LogAkcija(AkcijaRequest.newBuilder()
                    .setMikroservis("Billings")
                    .setTip(AkcijaRequest.Tip.CREATE)
                    .setResurs("Zakupnine")
                    .setOdgovor(AkcijaRequest.Odgovor.SUCCESS)
                    .build());

            return new ResponseEntity<Zakupnina>(rez,
                    HttpStatus.OK);
        }

        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Billings")
                .setTip(AkcijaRequest.Tip.CREATE)
                .setResurs("Zakupnine")
                .setOdgovor(AkcijaRequest.Odgovor.FAILURE)
                .build());

        throw new ResponseStatusException(
                response.getStatusCode(), response.getBody());
    }

    @PutMapping("/billings")
    ResponseEntity<Zakupnina> promijeniZakupninu(@Valid @RequestBody Zakupnina zakupnina,
                                                 @RequestHeader("pozivaoc-id") Long korisnikId,
                                                 @RequestHeader("pozivaoc-rola") Boolean korisnikRola) {

        ValidacijskiHelper.provjeriPristup(zakupnina.getKorisnikId(), korisnikId, korisnikRola);

        ValidacijskiHelper.provjeriDatum(zakupnina.getDatumSklapanjaUgovora(), zakupnina.getDatumRaskidaUgovora());

        var response = servis.kreirajZakupninu(zakupnina);

        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Billings")
                .setTip(AkcijaRequest.Tip.UPDATE)
                .setResurs("Zakupnine")
                .setOdgovor(AkcijaRequest.Odgovor.SUCCESS)
                .build());

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
