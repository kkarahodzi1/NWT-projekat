package com.nwt.storagecontrol.controller;

import java.util.List;

import com.nwt.storagecontrol.service.TipoviService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nwt.storagecontrol.model.Tipovi;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TipoviController
{
    @Autowired
    private TipoviService tipoviService;

    TipoviController(TipoviService tipoviService)
    {
        this.tipoviService = tipoviService;
    }

    @GetMapping("/tipovi")
    public ResponseEntity<List<Tipovi>> getAllTipovi(@RequestParam(required = false) String naziv)
    {
        return tipoviService.getAll(naziv);
    }

    @GetMapping("/tipovi/{id}")
    public ResponseEntity<Tipovi> getTipoviById(@PathVariable("id") long id) {
        return tipoviService.getById(id);
    }

    @PostMapping("/tipovi")
    public ResponseEntity<Tipovi> createTip(@RequestBody Tipovi tipovi) {
        return tipoviService.createTip(tipovi);
    }

    @DeleteMapping("/tipovi")
    public ResponseEntity<HttpStatus> deleteByNaziv(@RequestParam(required = true) String naziv) {
        return tipoviService.deleteByNaziv(naziv);
    }

    @DeleteMapping("/tipovi/{id}")
    public ResponseEntity<String> deleteTip(@PathVariable("id") long id) {
        return tipoviService.deleteTip(id);
    }


}
