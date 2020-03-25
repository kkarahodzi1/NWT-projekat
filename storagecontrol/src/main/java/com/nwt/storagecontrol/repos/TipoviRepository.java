package com.nwt.storagecontrol.repos;

import java.util.List;

import com.nwt.storagecontrol.model.Tipovi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "tipovi", path = "tipovi")
public interface TipoviRepository extends CrudRepository<Tipovi, Long> {

    Tipovi findByNaziv(String naziv);
    Tipovi findById(long id);
}