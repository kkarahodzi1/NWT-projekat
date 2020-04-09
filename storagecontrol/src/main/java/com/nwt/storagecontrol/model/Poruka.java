package com.nwt.storagecontrol.model;

import javax.validation.constraints.NotNull;

public class Poruka {

    @NotNull
    private String ime;
    @NotNull
    private String prezime;
    @NotNull
    private String email;

    private String verifikacijskiKod;
    private String redirektUrl;
    private String poruka;

    public Poruka() {
    }

    public Poruka(String ime, String prezime, String email) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getVerifikacijskiKod() {
        return verifikacijskiKod;
    }

    public void setVerifikacijskiKod(String verifikacijskiKod) {
        this.verifikacijskiKod = verifikacijskiKod;
    }

    public String getRedirektUrl() {
        return redirektUrl;
    }

    public void setRedirektUrl(String redirektUrl) {
        this.redirektUrl = redirektUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

