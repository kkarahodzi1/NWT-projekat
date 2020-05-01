package com.nwt.notifications.model;

import com.google.protobuf.Timestamp;
import org.nwt.notifications.AkcijaRequest.Tip;
import org.nwt.notifications.AkcijaRequest.Odgovor;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Akcija{

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getMikroservis()
    {
        return mikroservis;
    }

    public void setMikroservis(String mikroservis)
    {
        this.mikroservis = mikroservis;
    }

    public Tip getTip()
    {
        return tip;
    }

    public void setTip(Tip tip)
    {
        this.tip = tip;
    }

    public String getResurs()
    {
        return resurs;
    }

    public void setResurs(String resurs)
    {
        this.resurs = resurs;
    }

    public Odgovor getOdgovor()
    {
        return odgovor;
    }

    public void setOdgovor(Odgovor odgovor)
    {
        this.odgovor = odgovor;
    }

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private Date timestamp;
    @NotNull
    private String mikroservis;
    @NotNull
    private Tip tip;
    @NotNull
    private String resurs;
    @NotNull
    private Odgovor odgovor;

    public Akcija(){}

    public Akcija(Date timestamp, String mikroservis, Tip tip, String resurs, Odgovor odgovor)
    {
        this.timestamp = timestamp;
        this.mikroservis = mikroservis;
        this.tip = tip;
        this.resurs = resurs;
        this.odgovor = odgovor;
    }

}
