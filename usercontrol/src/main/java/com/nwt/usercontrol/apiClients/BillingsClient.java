package com.nwt.usercontrol.apiClients;

import com.nwt.usercontrol.model.Poruka;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name ="billings-service")
public interface BillingsClient {

    @RequestMapping(value ="api/billings/{korisnikId}/korisnik", method = RequestMethod.GET, consumes="application/json")
    ResponseEntity<Object> pregledZakupninaKorisnika(@PathVariable Long korisnikId);
}
