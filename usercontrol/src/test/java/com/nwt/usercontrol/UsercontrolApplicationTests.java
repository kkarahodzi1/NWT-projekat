package com.nwt.usercontrol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.usercontrol.model.User;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsercontrolApplicationTests {

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
    public void getAll_VALID() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))) // da li su vracena 2 objekta
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].prezime", Matchers.is("Howard"))) // da li je drugom prezime Howard
                .andExpect(status().isOk()); // status OK
    }


    @org.junit.jupiter.api.Test
    @Order(2)
    public void getById_VALID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.prezime", Matchers.is("Howard")));
    }


    @org.junit.jupiter.api.Test
    @Order(3)
    public void getById_INVALID() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 123)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji")))
                .andExpect(status().isNotFound());
    }


    @org.junit.jupiter.api.Test
    @Order(4)
    public void post_VALID() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users")
                .content(asJsonString(new User("Tony", "Montana", "tmontana@etf.ba", "gauss1234", 0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prezime", Matchers.is("Montana")))
                .andExpect(status().isCreated());
    }


    @org.junit.jupiter.api.Test
    @Order(5)
    public void post_INVALID1() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users")
                .content(asJsonString(new User("Mony", "Tontana", "mujo@gmail.com", "gauss1234", 0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ova email adresa je zauzeta")))
                .andExpect(status().isBadRequest());
    }


    @org.junit.jupiter.api.Test
    @Order(6)
    public void post_INVALID2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users")
                .content(asJsonString(new User("", "Tontana", "tmontana@etf.ba", "gauss1234", 0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @org.junit.jupiter.api.Test
    @Order(7)
    public void put_VALID() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/users/{id}", 1)
                .content(asJsonString(new User("Peter", "Parker", "spiderman@etf.ba", "gauss1234", 0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prezime", Matchers.is("Parker")))
                .andExpect(status().isOk());
    }


    @org.junit.jupiter.api.Test
    @Order(8)
    public void put_INVALID() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/users/{id}", 1)
                .content(asJsonString(new User("", "Parker", "spiderman@etf.ba", "gauss1234", 0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg[0]", Matchers.is("ime")))
                .andExpect(status().isBadRequest());
    }


    @org.junit.jupiter.api.Test
    @Order(9)
    public void delete_VALID() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg", Matchers.is("Korisnik uspjesno obrisan")))
                .andExpect(status().isOk());
    }


    @org.junit.jupiter.api.Test
    @Order(10)
    public void delete_INVALID() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji")))
                .andExpect(status().isNotFound());
    }

    @org.junit.jupiter.api.Test
    @Order(11)
    public void getZakupnine_VALID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/billings/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))); // da li su vracena 2 objekta
    }

    @org.junit.jupiter.api.Test
    @Order(12)
    public void getZakupnine_INVALID() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/billings/23")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji")));
    }

    // END TESTS
}
