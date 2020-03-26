package com.nwt.notifications.model;

public class Poruka {

    private String ime;
    private String prezime;
    private String email;
    private String verifikacijskiKod;
    private String redirektUrl;

    public Poruka() {
    }

    public Poruka(String ime, String prezime, String email, String verifikacijskiKod, String redirektUrl) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.verifikacijskiKod = verifikacijskiKod;
        this.redirektUrl = redirektUrl;
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
}
