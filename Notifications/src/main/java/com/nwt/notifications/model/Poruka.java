package com.nwt.notifications.model;

public class Poruka {

    private String ime;
    private String prezime;
    private String email;
    private String verifikacijskiKod;

    public Poruka() {
    }

    public String getFirstName() {
        return ime;
    }

    public void setFirstName(String firstName) {
        this.ime = firstName;
    }

    public String getLastName() {
        return prezime;
    }

    public void setLastName(String lastName) {
        this.prezime = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verifikacijskiKod;
    }

    public void setVerificationCode(String verificationCode) {
        this.verifikacijskiKod = verificationCode;
    }
}
