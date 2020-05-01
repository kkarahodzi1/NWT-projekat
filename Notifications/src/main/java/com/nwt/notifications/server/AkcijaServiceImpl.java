package com.nwt.notifications.server;

import com.google.protobuf.Timestamp;
import com.nwt.notifications.model.Akcija;
import com.nwt.notifications.repos.AkcijaRepository;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.nwt.notifications.AkcijaRequest;
import org.nwt.notifications.AkcijaResponse;
import org.nwt.notifications.AkcijaServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;


public class AkcijaServiceImpl extends AkcijaServiceGrpc.AkcijaServiceImplBase {

    AkcijaRepository akcijaRepository;

    public AkcijaServiceImpl(AkcijaRepository akcijaRepository)
    {
        this.akcijaRepository = akcijaRepository;
    }

    @Override
    public void akcija(AkcijaRequest request, StreamObserver<AkcijaResponse> responseObserver) {

        akcijaRepository.save(new Akcija(new Date(), request.getMikroservis(), request.getTip(), request.getResurs(),request.getOdgovor()));

        String greeting = new StringBuilder()
                .append("Uspjesno prihvacen zahtjev servisa ")
                .append(request.getMikroservis())
                .append(", broj: ")
                .append(akcijaRepository.count())
                .toString();

        AkcijaResponse response = AkcijaResponse.newBuilder()
                .setOdgovor(greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}