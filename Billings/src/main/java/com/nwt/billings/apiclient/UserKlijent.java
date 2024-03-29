package com.nwt.billings.apiclient;

import com.nwt.billings.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "usercontrol-service")
public interface UserKlijent {

    String AUTH_TOKEN = "Authorization";

    @RequestMapping(value = "api/secure/users/{id}", method = RequestMethod.GET, consumes = "application/json")
    ResponseEntity<User> getUser(@PathVariable Long id,@RequestHeader(AUTH_TOKEN) String bearerToken);
}
