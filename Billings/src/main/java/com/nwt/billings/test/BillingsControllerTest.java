package com.nwt.billings.test;

import com.nwt.billings.controller.BillingsController;
import com.nwt.billings.helper.JsonHelper;
import com.nwt.billings.model.Zakupnina;
import com.nwt.billings.repos.ZakupninaRepo;
import com.nwt.billings.services.ZakupninaServis;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BillingsController.class)
public class BillingsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ZakupninaServis repo;

    @Before
    public void init() {
        Zakupnina zakup1 = new Zakupnina((long)13, (long)123, (long)123, (long)2, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE, Boolean.FALSE, 100.2);
        Zakupnina zakup2 = new Zakupnina((long)14, (long)123, (long)123, (long)2, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE, Boolean.FALSE, 120.5);

        List<Zakupnina> lista = new ArrayList<Zakupnina>();
        lista.add(zakup1);
        lista.add(zakup2);

        when(repo.dobaviZakupnineKorisnika((long) 123)).thenReturn(lista);
    }

    @Test
    public void pregledZakupninaKorisnika_vratiOk() throws Exception{
        this.mvc.perform(get("/billings/{id}/korisnik",123))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].skladisteId").isNotEmpty())
                .andExpect(jsonPath("$[0].jedinicaId").isNotEmpty())
                .andExpect(jsonPath("$[0].datumSklapanjaUgovora").isNotEmpty())
                .andExpect(jsonPath("$[0].datumRaskidaUgovora").isNotEmpty())
                .andExpect(jsonPath("$[0].ukupnaCijena").isNotEmpty())
                .andExpect(jsonPath("$[0].potvrdjeno").isNotEmpty());
    }

    @Test
    public void pregledZakupninaKorisnika_vrati400() throws Exception{
        this.mvc.perform(get("/billings/{id}/korisnik","abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void obrisiZakupninu_vrati400() throws Exception{
        this.mvc.perform(delete("/billings/{id}","abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void kreirajZakupninu_vrati400() throws Exception{

        var zakup = new Zakupnina((long)13, (long)123, (long)123, (long)2, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE, Boolean.FALSE, 100.2);

        this.mvc.perform(post("/billings").contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.asJsonString(zakup))
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());

        zakup.setKorisnikId(null);
        this.mvc.perform(post("/billings").contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.asJsonString(zakup))
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void kreirajZakupninu_vrati401() throws Exception{

        var zakup = new Zakupnina((long)13, (long)123, (long)123, (long)2, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE,  Boolean.FALSE, 100.2);
        this.mvc.perform(post("/billings").header("pozivaoc-id",(long)1).header("pozivaoc-rola", Boolean.FALSE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.asJsonString(zakup))
                .characterEncoding("utf-8"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void promijeniZakupninu_vrati400() throws Exception{

        var zakup = new Zakupnina((long)13, (long)123, (long)123, (long)2, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE, Boolean.FALSE, 100.2);

        this.mvc.perform(put("/billings").contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.asJsonString(zakup))
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());

        zakup.setKorisnikId(null);
        this.mvc.perform(put("/billings").contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.asJsonString(zakup))
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void promijeniZakupninu_vrati401() throws Exception{

        var zakup = new Zakupnina((long)13, (long)123, (long)123, (long)2, new Date(), new Date(), new Date(), new Date(), new Date(), Boolean.TRUE,  Boolean.FALSE, 100.2);
        this.mvc.perform(put("/billings").header("pozivaoc-id",(long)1).header("pozivaoc-rola", Boolean.FALSE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.asJsonString(zakup))
                .characterEncoding("utf-8"))
                .andExpect(status().isUnauthorized());
    }
}
