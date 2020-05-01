package com.nwt.storagecontrol.controller;

import java.util.List;

import com.nwt.storagecontrol.service.TipoviService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.nwt.notifications.AkcijaRequest;
import org.nwt.notifications.AkcijaResponse;
import org.nwt.notifications.AkcijaServiceGrpc;
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

@CrossOrigin(origins = "http://localhost:8083")
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

    public void LogAkcija(AkcijaRequest akcijaRequest)
    {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8084)
                .usePlaintext()
                .build();

        AkcijaServiceGrpc.AkcijaServiceBlockingStub stub
                = AkcijaServiceGrpc.newBlockingStub(channel);
        stub.akcija(akcijaRequest);
        channel.shutdown();
    }

    @GetMapping("/tipovi")
    public ResponseEntity<Object> getAllTipovi(@RequestParam(required = false) String naziv)
    {
        ResponseEntity<Object> zahtjev = tipoviService.getAll(naziv);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.GET)
                .setResurs("Tipovi")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @GetMapping("/tipovi/{id}")
    public ResponseEntity<Object> getTipoviById(@PathVariable("id") long id) {

        ResponseEntity<Object> zahtjev = tipoviService.getById(id);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.GET)
                .setResurs("Tipovi")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @PostMapping("/tipovi")
    public ResponseEntity<Object> createTip(@RequestBody Tipovi tipovi) {
        ResponseEntity<Object> zahtjev = tipoviService.createTip(tipovi);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.CREATE)
                .setResurs("Tipovi")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @DeleteMapping("/tipovi")
    public ResponseEntity<Object> deleteByNaziv(@RequestParam(required = true) String naziv) {
        ResponseEntity<Object> zahtjev = tipoviService.deleteByNaziv(naziv);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.DELETE)
                .setResurs("Tipovi")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @DeleteMapping("/tipovi/{id}")
    public ResponseEntity<Object> deleteTip(@PathVariable("id") long id) {
        ResponseEntity<Object> zahtjev =  tipoviService.deleteTip(id);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.DELETE)
                .setResurs("Tipovi")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }


}
