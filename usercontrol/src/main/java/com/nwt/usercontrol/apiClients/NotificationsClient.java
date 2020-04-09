package com.nwt.usercontrol.apiClients;

import com.nwt.usercontrol.model.Poruka;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name ="notifications-service")
public interface NotificationsClient {

    @RequestMapping(value ="api/notifications", method = RequestMethod.POST, consumes="application/json")
    void registrujKorisnika(Poruka p);
}
