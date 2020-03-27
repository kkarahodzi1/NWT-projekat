package com.nwt.storagecontrol.repos;

import java.util.List;

import com.nwt.storagecontrol.model.SkladisneJedinice;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.model.Tipovi;
import org.springframework.data.repository.CrudRepository;

public interface SkladJedRepository extends CrudRepository<SkladisneJedinice, Long> {

    SkladisneJedinice findByBroj(Integer broj);
    List<SkladisneJedinice> findBySkladiste(Skladiste skladiste);
    List<SkladisneJedinice> findByTip(Tipovi tip);
}
