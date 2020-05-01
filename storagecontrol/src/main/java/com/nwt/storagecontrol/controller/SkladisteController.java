package com.nwt.storagecontrol.controller;


import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.service.SkladistaService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.nwt.notifications.AkcijaRequest;
import org.nwt.notifications.AkcijaServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:8083")
@RestController
@RequestMapping("/api")
public class SkladisteController
{
    @Autowired
    SkladistaService skladistaService;

    SkladisteController(SkladistaService skladistaService)
    {
        this.skladistaService = skladistaService;
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

    @GetMapping("/skladista")
    public ResponseEntity<Object> getAllSkladista(@RequestParam(required = false) String adresa) {
        ResponseEntity<Object> zahtjev = skladistaService.getAllSkladista(adresa);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.GET)
                .setResurs("Skladista")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @GetMapping("/skladista/{id}")
    public ResponseEntity<Object> getSkladisteById(@PathVariable("id") long id) {
        ResponseEntity<Object> zahtjev = skladistaService.getSkladisteById(id);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.GET)
                .setResurs("Skladista")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @PostMapping("/skladista")
    public ResponseEntity<Object> createSkladiste(@RequestBody Skladiste skladista) {
        ResponseEntity<Object> zahtjev = skladistaService.createSkladiste(skladista);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.CREATE)
                .setResurs("Skladista")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @PutMapping("/skladista/{id}")
    public ResponseEntity<Object> updateSkladiste(@PathVariable("id") long id, @RequestBody Skladiste skladiste) {
        ResponseEntity<Object> zahtjev = skladistaService.updateSkladiste(id, skladiste);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.UPDATE)
                .setResurs("Skladista")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @PutMapping("/skladista/obrisi/{id}")
    public ResponseEntity<Object> obrisiSkladiste(@PathVariable("id") long id) {
        ResponseEntity<Object> zahtjev = skladistaService.obrisiSkladiste(id);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.UPDATE)
                .setResurs("Skladista")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @PutMapping("/skladista/vrati/{id}")
    public ResponseEntity<Object> vratiSkladiste(@PathVariable("id") long id) {
        ResponseEntity<Object> zahtjev = skladistaService.vratiSkladiste(id);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.UPDATE)
                .setResurs("Skladista")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }

    @DeleteMapping("/skladista/{id}")
    public ResponseEntity<Object> deleteSkladiste(@PathVariable("id") long id) {
        ResponseEntity<Object> zahtjev = skladistaService.deleteSkladiste(id);
        LogAkcija(AkcijaRequest.newBuilder()
                .setMikroservis("Storage")
                .setTip(AkcijaRequest.Tip.DELETE)
                .setResurs("Skladista")
                .setOdgovor(zahtjev.getStatusCodeValue()/100 == 2 ? AkcijaRequest.Odgovor.SUCCESS : AkcijaRequest.Odgovor.FAILURE)
                .build());
        return zahtjev;
    }


}
