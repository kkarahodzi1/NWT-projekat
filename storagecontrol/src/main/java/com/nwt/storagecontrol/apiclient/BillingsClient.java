package com.nwt.storagecontrol.apiclient;

import com.nwt.storagecontrol.model.Poruka;
import com.nwt.storagecontrol.model.Zakupnina;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name ="billings-service")
public interface BillingsClient {

    @RequestMapping(value ="api/billings/{id}/jedinica", method = RequestMethod.GET, consumes="application/json")
    ResponseEntity<List<Zakupnina>> dohvatiZakupninu(@PathVariable Long id);
}