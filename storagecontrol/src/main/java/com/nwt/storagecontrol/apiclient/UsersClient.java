package com.nwt.storagecontrol.apiclient;

import com.nwt.storagecontrol.model.Poruka;
import com.nwt.storagecontrol.model.User;
import com.nwt.storagecontrol.model.Zakupnina;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name ="usercontrol-service")
public interface UsersClient {

    @RequestMapping(value ="api/users/{id}", method = RequestMethod.GET, consumes="application/json")
    ResponseEntity<User>  dohvatiKorisnika(@PathVariable Long id);
}