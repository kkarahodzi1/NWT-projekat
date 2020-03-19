package com.nwt.storagecontrol.repos;

import java.util.List;

import com.nwt.storagecontrol.model.Skladiste;
import org.springframework.data.repository.CrudRepository;

public interface SkladisteRepository extends CrudRepository<Skladiste, Long> {

    List<Skladiste> findByAdresa(String adresa);

    Skladiste findById(long id);
}
