package com.nwt.billings.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Zakupnina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long korisnikId;
    private Long jedinicaId;
    private Long skladisteId;

    private Date datumSklapanjaUgovora;
    private Date datumRaskidaUgovora;

    private Date datumKreiranja;
    private Date datumModificiranja;
    private Date datumBrisanja;

    private Boolean obrisan;

    public Zakupnina() {
    }

    public Zakupnina(Long korisnikId){
        this.korisnikId = korisnikId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Long korisnikId) {
        this.korisnikId = korisnikId;
    }

    public Long getJedinicaId() {
        return jedinicaId;
    }

    public void setJedinicaId(Long jedinicaId) {
        this.jedinicaId = jedinicaId;
    }

    public Long getSkladisteId() {
        return skladisteId;
    }

    public void setSkladisteId(Long skladisteId) {
        this.skladisteId = skladisteId;
    }

    public Date getDatumSklapanjaUgovora() {
        return datumSklapanjaUgovora;
    }

    public void setDatumSklapanjaUgovora(Date datumSklapanjaUgovora) {
        this.datumSklapanjaUgovora = datumSklapanjaUgovora;
    }

    public Date getDatumRaskidaUgovora() {
        return datumRaskidaUgovora;
    }

    public void setDatumRaskidaUgovora(Date datumRaskidaUgovora) {
        this.datumRaskidaUgovora = datumRaskidaUgovora;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(Date datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public Date getDatumModificiranja() {
        return datumModificiranja;
    }

    public void setDatumModificiranja(Date datumModificiranja) {
        this.datumModificiranja = datumModificiranja;
    }

    public Date getDatumBrisanja() {
        return datumBrisanja;
    }

    public void setDatumBrisanja(Date datumBrisanja) {
        this.datumBrisanja = datumBrisanja;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }

    @Override
    public String toString() {
        return String.format(
                "Zakupnina[id=%d, korisnikId='%s', jedinicaId='%s']",
                id, korisnikId, jedinicaId);
    }
}
