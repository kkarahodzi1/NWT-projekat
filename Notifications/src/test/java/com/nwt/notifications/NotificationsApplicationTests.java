package com.nwt.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.notifications.model.Poruka;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationsApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    @Order(1)
    public void posaljiZakupninaZahtjev_VALID() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post("/notifications/zakupnina-req")
                .param("zakupninaId","123")
                .content(asJsonString(new Poruka("nefis","velic","nefis@test.com","","","")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @org.junit.jupiter.api.Test
    @Order(2)
    public void posaljiZakupninaZahtjev_INVALID() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post("/notifications/zakupnina-req")
                .param("zakupninaId","0")
                .content(asJsonString(new Poruka("nefis","velic","nefis@test.com","","","")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @org.junit.jupiter.api.Test
    @Order(3)
    public void posaljiUspjesnaRegistracija_VALID() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post("/notifications/registration-res")
                .content(asJsonString(new Poruka("nefis","velic","nefis@test.com","","","")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @org.junit.jupiter.api.Test
    @Order(4)
    public void posaljiCustom_VALID() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post("/notifications")
                .content(asJsonString(new Poruka("nefis","velic","nefis@test.com","","","halo test")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @org.junit.jupiter.api.Test
    @Order(5)
    public void posaljiCustom_INVALID() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders
                .post("/notifications")
                .content(asJsonString(new Poruka("nefis","velic","nefis@test.com","","","")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}
