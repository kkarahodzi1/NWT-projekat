package com.nwt.notifications.controller;

import com.nwt.notifications.model.Akcija;
import com.nwt.notifications.repos.AkcijaRepository;
import com.nwt.notifications.service.AkcijaService;
import org.nwt.notifications.AkcijaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AkcijaController
{
    @Autowired
    private AkcijaService akcijaService;

    AkcijaController(AkcijaService akcijaService)
    {
        this.akcijaService = akcijaService;
    }

    @GetMapping("/akcije")
    public ResponseEntity<Object> getAllTipovi()
    {
        ResponseEntity<Object> zahtjev = akcijaService.getAll();
        return zahtjev;
    }
}
