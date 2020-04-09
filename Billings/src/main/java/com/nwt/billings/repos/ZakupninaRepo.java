package com.nwt.billings.repos;

import com.nwt.billings.model.Zakupnina;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface ZakupninaRepo extends CrudRepository<Zakupnina, Long> {

    List<Zakupnina> findByKorisnikId(Long korisnikId);

    @Modifying
    @Transactional
    @Query("update Zakupnina z set z.datumBrisanja = ?2, z.obrisan = 1 where z.id = ?1")
    void softDeleteById(Long id, Date datumBrisanja);

    List<Zakupnina> findByJedinicaId(Long jedinicaId);
}
