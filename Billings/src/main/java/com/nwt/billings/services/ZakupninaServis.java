package com.nwt.billings.services;

import com.nwt.billings.model.Zakupnina;

import java.util.List;

public interface ZakupninaServis {

    List<Zakupnina> dobaviZakupnineKorisnika(Long idKorisnika);

    void obrisiZakupninu(Long idKorisnika);

    Zakupnina kreirajZakupninu(Zakupnina zakupnina);

    Zakupnina promijeniZakupninu(Zakupnina zakupnina);

    List<Zakupnina> dobaviZakupninePoJedinici(Long jedinicaId);
}
