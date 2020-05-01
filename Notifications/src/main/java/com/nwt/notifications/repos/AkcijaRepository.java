package com.nwt.notifications.repos;

import com.nwt.notifications.model.Akcija;
import com.nwt.notifications.model.Poruka;
import org.nwt.notifications.AkcijaRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AkcijaRepository extends CrudRepository<Akcija, Long> {
}
