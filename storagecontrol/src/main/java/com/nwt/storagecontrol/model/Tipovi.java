package com.nwt.storagecontrol.model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

@Entity
public class Tipovi {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    @NotNull
    @NotEmpty
    private String naziv;

    @NotNull
    @Positive
    private Float cijena;

    protected Tipovi(){

    }

    public Tipovi(String naziv, Float cijena) {
        this.naziv = naziv;
        this.cijena = cijena;
    }


    @Override
    public String toString() {
        return String.format(
                "Tip[id=%d, naziv='%s', cijena='%s']",
                id, naziv, cijena);
    }

    public Long getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Float getCijena() {
        return cijena;
    }

    public void setCijena(Float cijena) {
        this.cijena = cijena;
    }
}
