package com.nwt.storagecontrol.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SkladisneJedinice {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer broj;

    @ManyToOne(optional=false)
    private Skladiste skladiste;

    @ManyToOne(optional=false)
    private Tipovi tip;

    private Date datumKreiranja;
    private Date datumModificiranja;
    private Date datumBrisanja;
    private Boolean obrisan;

    protected SkladisneJedinice(){

    }

    public SkladisneJedinice(Integer broj, Skladiste skladiste, Tipovi tip) {
        this.broj = broj;
        this.skladiste = skladiste;
        this.tip = tip;
        this.obrisan = Boolean.FALSE;
        this.datumKreiranja = new Date();
    }


    @Override
    public String toString() {
        return String.format(
                "SkladisneJedinice[id=%d, broj='%s']",
                id, broj);
    }

    public Long getId() {
        return id;
    }

    public Skladiste getSkladiste() {
        return skladiste;
    }

    public void setSkladiste(Skladiste skladiste) {
        this.skladiste = skladiste;
    }

    public Integer getBroj() {
        return broj;
    }

    public void setBroj(Integer broj) {
        this.broj = broj;
    }

    public Boolean getObrisan() {
        return obrisan;
    }

    public void setObrisan(Boolean obrisan) {
        this.obrisan = obrisan;
    }

    public Date getDatumBrisanja() {
        return datumBrisanja;
    }

    public void setDatumBrisanja(Date datumBrisanja) {
        this.datumBrisanja = datumBrisanja;
    }

    public Date getDatumModificiranja() {
        return datumModificiranja;
    }

    public void setDatumModificiranja(Date datumModificiranja) {
        this.datumModificiranja = datumModificiranja;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(Date datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }
}
