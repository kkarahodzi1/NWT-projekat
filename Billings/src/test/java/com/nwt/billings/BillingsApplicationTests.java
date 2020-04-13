package com.nwt.billings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.billings.model.Zakupnina;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BillingsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // BEGIN TESTS

    @org.junit.jupiter.api.Test
    @Order(1)
    public void kreirajZakupninu_VALID() throws Exception{

        var datumSklapanja = new Date();
        var datumRaskida = new Date(2,2,2100);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/billings")
                .header("pozivaoc-id",(long)1).header("pozivaoc-rola", Boolean.TRUE)
                .content(asJsonString(new Zakupnina((long)3, (long)2, (long)123, (long)2, datumSklapanja, datumSklapanja, datumRaskida, new Date(), new Date(), Boolean.FALSE, Boolean.FALSE, 100.2)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(3)))
                .andExpect(status().is2xxSuccessful());
    }

    @org.junit.jupiter.api.Test
    @Order(2)
    public void kreirajZakupninu_INVALID_UserNePostoji() throws Exception{

        var datumSklapanja = new Date();
        var datumRaskida = new Date(2,2,2100);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/billings")
                .header("pozivaoc-id",(long)1).header("pozivaoc-rola", Boolean.TRUE)
                .content(asJsonString(new Zakupnina((long)3, (long)13, (long)123, (long)2, datumSklapanja, datumSklapanja, datumRaskida, new Date(), new Date(), Boolean.FALSE, Boolean.FALSE, 100.2)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    // END TESTS


}
