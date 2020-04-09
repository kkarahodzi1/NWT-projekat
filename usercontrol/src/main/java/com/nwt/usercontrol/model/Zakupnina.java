package com.nwt.usercontrol.model;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.Date;

@Entity
public class Zakupnina {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(1)
    @NotNull
    private Long korisnikId;
    @Min(1)
    @NotNull
    private Long jedinicaId;
    @Min(1)
    @NotNull
    private Long skladisteId;

    @NotNull
    private Date datumSklapanjaUgovora;
    @NotNull
    private Date datumRaskidaUgovora;
    private Date datumKreiranja;
    private Date datumModificiranja;
    private Date datumBrisanja;

    private Boolean obrisan;
    private Boolean potvrdjeno;

    @Min(1)
    @NotNull
    private double ukupnaCijena;

    public Zakupnina() {
    }

    public Zakupnina(Long id, Long korisnikId, Long jedinicaId, Long skladisteId, Date datumSklapanjaUgovora, Date datumRaskidaUgovora, Date datumKreiranja, Date datumBrisanja, Date datumModificiranja, Boolean obrisan, Boolean potvrdjeno, double ukupnaCijena) {
        this.id = id;
        this.korisnikId = korisnikId;
        this.jedinicaId = jedinicaId;
        this.skladisteId = skladisteId;
        this.datumBrisanja = datumBrisanja;
        this.datumKreiranja = datumKreiranja;
        this.datumModificiranja = datumModificiranja;
        this.datumSklapanjaUgovora = datumSklapanjaUgovora;
        this.datumRaskidaUgovora = datumRaskidaUgovora;
        this.potvrdjeno = potvrdjeno;
        this.obrisan = obrisan;
        this.ukupnaCijena = ukupnaCijena;
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

    public double getUkupnaCijena() {
        return ukupnaCijena;
    }

    public void setUkupnaCijena(double ukupnaCijena) {
        this.ukupnaCijena = ukupnaCijena;
    }

    public Boolean getPotvrdjeno() {
        return potvrdjeno;
    }

    public void setPotvrdjeno(Boolean potvrdjeno) {
        this.potvrdjeno = potvrdjeno;
    }

    @Override
    public String toString() {
        return String.format(
                "Zakupnina[id=%d, korisnikId='%s', jedinicaId='%s']",
                id, korisnikId, jedinicaId);
    }
}
