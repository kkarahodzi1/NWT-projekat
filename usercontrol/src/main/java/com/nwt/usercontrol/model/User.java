package com.nwt.usercontrol.model;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
public class User
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Min(1)
    private Long korisnikId;

    @NotBlank
    private String ime;

    @NotBlank
    private String prezime;

    @NotBlank
    @Column(unique=true)
    @Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")
    private String mail;

    @NotBlank
    private String password;

    @NotNull
    private int role;


    private Date datumKreiranja;


    private Date datumModificiranja;
    private Date datumBrisanja;

    @NotNull
    private int obrisan;
    protected User() {}

    public User(String fn, String ln, String ml, String pw, int role) {
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

    public Long getId() { return korisnikId; }
    public void setId(Long id) { this.korisnikId = id; }


    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }


    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) { this.prezime = prezime; }

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
