package com.nwt.storagecontrol.apiclient;

import com.nwt.storagecontrol.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name ="usercontrol-service")
public interface UsersClient {

    @RequestMapping(value ="api/users/{id}", method = RequestMethod.GET, consumes="application/json")
    ResponseEntity<User>  dohvatiKorisnika(@PathVariable Long id);
}