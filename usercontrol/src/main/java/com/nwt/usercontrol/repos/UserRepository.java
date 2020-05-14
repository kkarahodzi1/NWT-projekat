package com.nwt.usercontrol.repos;

import com.nwt.usercontrol.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    //int dodajKorisnika(String fn, String ln, String ml, String pw, int role);

    User findById(long id);

    List<User> findAll();

    // hard delete
    void deleteById(long id);

    // soft delete
    @Modifying
    @Transactional
    @Query("update User u set u.datumBrisanja = ?2, u.obrisan = 1 where u.korisnikId = ?1")
    void softDeleteById(Long id, Date datumBrisanja);

    User findByMail(String mail);

}