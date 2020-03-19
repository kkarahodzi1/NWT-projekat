package com.nwt.usercontrol.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Korisnik
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long korisnikId;
    private String ime;
    private String prezime;
    private String mail;
    private String password;
    private int role;
    private Date datumKreiranja;
    private Date datumModificiranja;
    private Date datumBrisanja;
    private int obrisan;
    protected Korisnik() {}

    public Korisnik(String fn, String ln, String ml, String pw, int role) {
        this.ime = fn;
        this.prezime = ln;
        this.mail = ml;
        this.password = pw;
        this.role = role;
        this.datumKreiranja = new Date();
        this.datumModificiranja = new Date();
        this.datumBrisanja = null;
        this.obrisan = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "%d %s %s",
                korisnikId, ime, prezime);
    }

    public Long getId() {
        return korisnikId;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(Date datum_kreiranja) {
        this.datumKreiranja = datum_kreiranja;
    }

    public Date getDatumModificiranja() {
        return datumModificiranja;
    }

    public void setDatumModificiranja(Date datum_modificiranja) {
        this.datumModificiranja = datum_modificiranja;
    }

    public Date getDatumBrisanja() {
        return datumBrisanja;
    }

    public void setDatumBrisanja(Date datum_brisanja) {
        this.datumBrisanja = datum_brisanja;
    }

    public int getObrisan() {
        return obrisan;
    }

    public void setObrisan(int obrisan) {
        this.obrisan = obrisan;
    }
}
