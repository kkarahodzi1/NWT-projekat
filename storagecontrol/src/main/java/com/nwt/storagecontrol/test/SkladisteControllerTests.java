package com.nwt.storagecontrol.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import com.nwt.storagecontrol.controller.SkladisteController;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SkladisteController.class)
public class SkladisteControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SkladisteRepository skladisteRepository;

    @Before
    public void init() {

        List<Skladiste> lista = new ArrayList<Skladiste>();

        lista.add(new Skladiste("Prva avenija 1", 50));
        lista.add(new Skladiste("Druga avenija 2", 70));
        lista.add(new Skladiste("Treca avenija 1", 25));
        lista.add(new Skladiste("Cetvrta avenija 4", 18));

        when(skladisteRepository.findByAdresa("Treca avenija 1")).thenReturn(lista.get(2));
        when(skladisteRepository.findById(1L)).thenReturn(Optional.of(lista.get(0)));
        when(skladisteRepository.findAll()).thenReturn(lista);
    }

    @Test
    public void getAllSkladistaAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/skladista")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].adresa").isNotEmpty())
                .andExpect(jsonPath("$[0].brojJedinica").isNotEmpty());
    }

    @Test
    public void getSkladista_AdresaTA1() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/skladista?adresa=Treca avenija 1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].adresa").value("Treca avenija 1"));
    }



    @Test
    public void createSkladisteAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .post("/api/skladista")
                .content(asJsonString(new Skladiste("Osma avenija 8", 12)))
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
    public void updateSkladisteAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .put("/api/skladista/{id}", 1)
                .content(asJsonString(new Skladiste("Deveta avenija 9", 80)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSkladisteAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders.delete("/api/skladista/{id}", 1) )
                .andExpect(status().is2xxSuccessful());
    }

}