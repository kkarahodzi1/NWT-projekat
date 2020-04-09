package com.nwt.billings.apiclient;

import com.nwt.billings.dto.Poruka;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notifications-service")
public interface NotificationsKlijent {

    @RequestMapping(value = "api/notifications/zakupnina-req", method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<String> posaljiZakupninaZahtjev(Poruka poruka, @RequestParam Long zakupninaId);
}

