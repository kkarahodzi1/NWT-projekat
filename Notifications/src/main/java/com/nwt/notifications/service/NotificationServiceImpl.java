package com.nwt.notifications.service;

import com.nwt.notifications.model.Poruka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    public JavaMailSender mailServis;

    @Override
    public void posaljiUspjesnaRegistracija(Poruka poruka) {
        poruka.setPoruka("Postovani/a " + poruka.getIme() + " " + poruka.getPrezime() + ",\n\nObavjestavamo vas da ste uspjesno registrovani na Storage Wars. Hvala vam na koristenju nasih usluga.");
        posaljiPoruku(poruka, "Uspjesna registracija");
    }

    @Override
    public void posaljiZakupninaZahtjev(Poruka poruka, Long id) {
        poruka.setPoruka("Postovani/a " + poruka.getIme() + " " + poruka.getPrezime() + ",\n\nObavjestavamo vas da ste uspjesno poslali zahtjev za zakupninu (id = " + id + "). Dobit cete mail o potvrdjenoj zakupnini. Hvala vam na koristenju nasih usluga.");
        posaljiPoruku(poruka, "Zahtjev za zakupninu kreiran");
    }

    @Override
    public void posaljiCustom(Poruka poruka) {
        posaljiPoruku(poruka, "Obavjestenje");
    }

    private void posaljiPoruku(Poruka poruka, String naslov) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(poruka.getEmail());
        email.setSubject(naslov);
        email.setText(poruka.getPoruka());
        mailServis.send(email);
    }
}
