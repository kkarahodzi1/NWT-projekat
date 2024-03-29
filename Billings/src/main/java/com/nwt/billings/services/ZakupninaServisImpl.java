package com.nwt.billings.services;

import com.nwt.billings.apiclient.NotificationsKlijent;
import com.nwt.billings.dto.Poruka;
import com.nwt.billings.model.Zakupnina;
import com.nwt.billings.repos.ZakupninaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ZakupninaServisImpl implements ZakupninaServis {

    @Autowired
    private ZakupninaRepo _repo;

    @Autowired
    private NotificationsKlijent notificationsKlijent;

    @Override
    public List<Zakupnina> dobaviZakupnineKorisnika(Long idKorisnika) {
        return _repo.findByKorisnikId(idKorisnika);
    }

    @Override
    public void obrisiZakupninu(Long idKorisnika) {
        _repo.softDeleteById(idKorisnika, new Date());
    }

    @Override
    public Zakupnina kreirajZakupninu(Zakupnina zakupnina) {

        zakupnina.setDatumKreiranja(new Date());
        zakupnina.setDatumModificiranja(new Date());
        zakupnina.setObrisan(Boolean.FALSE);
        zakupnina.setPotvrdjeno(Boolean.FALSE);

        return _repo.save(zakupnina);
    }

    @Override
    public Zakupnina promijeniZakupninu(Zakupnina zakupnina) {

        zakupnina.setDatumModificiranja(new Date());
        zakupnina.setObrisan(Boolean.FALSE);
        zakupnina.setPotvrdjeno(Boolean.FALSE);

        return _repo.save(zakupnina);
    }

    @Override
    public List<Zakupnina> dobaviZakupninePoJedinici(Long jedinicaId) {
        return _repo.findByJedinicaId(jedinicaId);
    }
}
