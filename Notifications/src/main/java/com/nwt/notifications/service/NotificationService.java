package com.nwt.notifications.service;

import com.nwt.notifications.model.Poruka;

public interface NotificationService {

    public void posaljiUspjesnaRegistracija(Poruka poruka);

    public void posaljiZakupninaZahtjev(Poruka poruka, Long id);

    public void posaljiCustom(Poruka poruka);
}
