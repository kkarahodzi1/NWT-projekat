package com.nwt.storagecontrol.repos;

import java.util.List;

import com.nwt.storagecontrol.model.Tipovi;
import org.springframework.data.repository.CrudRepository;

public interface TipoviRepository extends CrudRepository<Tipovi, Long> {

    Tipovi findByNaziv(String naziv);
}