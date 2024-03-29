package com.nwt.storagecontrol.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Entity
public class Skladiste {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NotNull
    private String adresa;

    @NotNull
    @Positive
    private Integer brojJedinica;

    @NotNull
    private Date datumKreiranja;

    private Date datumModificiranja;
    private Date datumBrisanja;

    @NotNull
    private Boolean obrisan;

    protected Skladiste(){

    }

    public Skladiste(String adresa, Integer brojJedinica) {
        this.adresa = adresa;
        this.brojJedinica = brojJedinica;
        this.obrisan = Boolean.FALSE;
        this.datumKreiranja = new Date();
        this.datumModificiranja = new Date();
        this.datumBrisanja = null;
    }


    @Override
    public String toString() {
        return String.format(
                "Skladiste[id=%d, adresa='%s', brojJedinica='%s']",
                id, adresa, brojJedinica);
    }

    public Long getId() {
        return id;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Integer getBrojJedinica() {
        return brojJedinica;
    }

    public void setBrojJedinica(Integer brojJedinica) {
        this.brojJedinica = brojJedinica;
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
}