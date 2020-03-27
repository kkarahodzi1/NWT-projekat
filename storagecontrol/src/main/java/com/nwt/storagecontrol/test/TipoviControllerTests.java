package com.nwt.storagecontrol.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.TipoviRepository;
import com.nwt.storagecontrol.controller.TipoviController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TipoviController.class)
public class TipoviControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TipoviRepository tipoviRepository;

    @Before
    public void init() {

        List<Tipovi> lista = new ArrayList<Tipovi>();
        lista.add(new Tipovi("Veliko", 180f));
        lista.add(new Tipovi("Malo", 90f));
        lista.add(new Tipovi("Deluxe", 300f));

        when(tipoviRepository.findByNaziv("Veliko")).thenReturn(lista.get(0));
        when(tipoviRepository.findById(3L)).thenReturn(Optional.of(new Tipovi("Deluxe", 300f)));
        when(tipoviRepository.findAll()).thenReturn(lista);
    }

    @Test
    public void getAllTipoviAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/tipovi")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].naziv").isNotEmpty())
                .andExpect(jsonPath("$[0].cijena").isNotEmpty());
    }

    @Test
    public void getTip_NazivVeliko() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/tipovi?naziv=Veliko")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].naziv").value("Veliko"));
    }

    @Test
    public void createTipAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .post("/api/tipovi")
                .content(asJsonString(new Tipovi("Srednje", 120f)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void deleteTipAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders.delete("/api/tipovi/{id}", 3) )
                .andExpect(status().is2xxSuccessful());
    }

}