package com.nwt.billings.helper;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

public class ValidacijskiHelper {

    public static void provjeriPristup(Long id, Long headerId, Boolean admin){

        if (id != headerId && admin != Boolean.TRUE)
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Korisnik nema pravo kreirati zakupninu pod tudjim Id.");
    }

    public static void provjeriDatum(Date datumSklapanja, Date datumRaskida){

        if (datumRaskida.before(datumSklapanja) || (datumRaskida.getTime() - datumSklapanja.getTime()) / (24 * 3600 * 1000) < 30) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Datum raskida mora biti minimalno 30 dana poslije datuma sklapanja ugovora");
        }

    }
}
