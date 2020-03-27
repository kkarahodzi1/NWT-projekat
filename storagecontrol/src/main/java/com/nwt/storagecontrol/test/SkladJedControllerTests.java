package com.nwt.storagecontrol.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.storagecontrol.controller.SkladJedController;
import com.nwt.storagecontrol.model.SkladisneJedinice;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.SkladJedRepository;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import com.nwt.storagecontrol.repos.TipoviRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(SkladJedController.class)
public class SkladJedControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SkladJedRepository skladJedRepository;

    @MockBean
    private SkladisteRepository skladisteRepository;

    @MockBean
    private TipoviRepository tipoviRepository;

    @Before
    public void init() {

        List<SkladisneJedinice> lista = new ArrayList<SkladisneJedinice>();

        Skladiste skladiste1 = new Skladiste("Prva avenija 1", 50);
        Skladiste skladiste2 = new Skladiste("Druga avenija 2", 70);

        Tipovi tip1 = new Tipovi("Veliko", 180f);
        Tipovi tip2 = new Tipovi("Malo", 90f);

        lista.add(new SkladisneJedinice(154, skladiste1, tip1));
        lista.add(new SkladisneJedinice(158, skladiste1, tip2));
        lista.add(new SkladisneJedinice(128, skladiste2, tip1));

        when(skladJedRepository.findBySkladiste(skladiste1)).thenReturn(lista);
        when(skladJedRepository.findById(1L)).thenReturn(Optional.of(lista.get(0)));
        when(skladJedRepository.findAll()).thenReturn(lista);
        when(skladisteRepository.findById(1L)).thenReturn(Optional.of(skladiste1));
    }

    @Test
    public void getAllSkladisneJediniceAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders
                .get("/api/skladjed")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].broj").isNotEmpty())
                .andExpect(jsonPath("$[0].skladiste").isNotEmpty())
                .andExpect(jsonPath("$[0].tip").isNotEmpty());
    }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void deleteSkladisneJediniceAPI() throws Exception
    {
        mvc.perform( MockMvcRequestBuilders.delete("/api/skladjed/{id}", 1) )
                .andExpect(status().is2xxSuccessful());
    }

}
