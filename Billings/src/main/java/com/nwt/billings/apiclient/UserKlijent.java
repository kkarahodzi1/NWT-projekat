package com.nwt.billings.apiclient;

import com.nwt.billings.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-control-service")
public interface UserKlijent {

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<User> getUser(@PathVariable Long id);
}
