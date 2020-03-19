package com.nwt.usercontrol.repos;

import java.util.List;

import com.nwt.usercontrol.model.Korisnik;
import org.springframework.data.repository.CrudRepository;

public interface KorisnikRepository extends CrudRepository<Korisnik, Long> {

    //int dodajKorisnika(String fn, String ln, String ml, String pw, int role);

    Korisnik findById(long id);

}