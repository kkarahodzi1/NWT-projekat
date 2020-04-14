package com.nwt.storagecontrol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.storagecontrol.apiclient.BillingsClient;
import com.nwt.storagecontrol.apiclient.NotificationsClient;
import com.nwt.storagecontrol.apiclient.UsersClient;
import com.nwt.storagecontrol.model.SkladisneJedinice;
import com.nwt.storagecontrol.model.Skladiste;
import com.nwt.storagecontrol.model.Tipovi;
import com.nwt.storagecontrol.repos.SkladisteRepository;
import com.nwt.storagecontrol.repos.TipoviRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StoragecontrolApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SkladisteRepository skladisteRepository;

    @Autowired
    TipoviRepository tipoviRepository;


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
    public void getAll_VALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tipovi")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].naziv", Matchers.is("Veliko")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].naziv", Matchers.is("Malo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].naziv", Matchers.is("Deluxe")))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(2)
    public void getNaziv_VALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tipovi?naziv=Veliko")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].naziv", Matchers.is("Veliko")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cijena", Matchers.is(180.0)))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(3)
    public void getNaziv_INVALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tipovi?naziv=Srednje")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @org.junit.jupiter.api.Test
    @Order(4)
    public void getId_VALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tipovi/6")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naziv", Matchers.is("Malo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cijena", Matchers.is(90.0)))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(5)
    public void getId_INVALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tipovi/2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji tip sa tim id")))
                .andExpect(status().isNotFound());
    }

    @org.junit.jupiter.api.Test
    @Order(6)
    public void post_VALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/tipovi")
                .content(asJsonString(new Tipovi("Srednje", 120f)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.naziv", Matchers.is("Srednje")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cijena", Matchers.is(120.0)))
                .andExpect(status().isCreated());


        mockMvc.perform(MockMvcRequestBuilders.get("/api/tipovi?naziv=Srednje")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(7)
    public void post_INVALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/tipovi")
                .content(asJsonString(new Tipovi("Veliko", 120f)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Proslijeđeni podaci nisu ispravni")))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(8)
    public void deleteNaziv_VALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tipovi?naziv=Deluxe")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tipovi?naziv=Deluxe")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @org.junit.jupiter.api.Test
    @Order(9)
    public void deleteNaziv_INVALID_notfound_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tipovi?naziv=Mini")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji tip sa tim nazivom")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.naziv", Matchers.is("Mini")))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(10)
    public void deleteNaziv_INVALID_dependant_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tipovi?naziv=Veliko")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Greška pri brisanju")))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(11)
    public void deleteId_VALID_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tipovi/11")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tipovi?naziv=Srednje")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @org.junit.jupiter.api.Test
    @Order(12)
    public void deleteId_INVALID_notfound_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tipovi/2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Greška pri brisanju")))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(13)
    public void deleteId_INVALID_dependant_t() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tipovi/5")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Greška pri brisanju")))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(14)
    public void getAll_VALID_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladista")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].adresa", Matchers.is("Prva avenija 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].adresa", Matchers.is("Druga avenija 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].adresa", Matchers.is("Treca avenija 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].adresa", Matchers.is("Cetvrta avenija 4")))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(15)
    public void getAdresa_VALID_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladista?adresa=Prva avenija 1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].adresa", Matchers.is("Prva avenija 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].brojJedinica", Matchers.is(50)))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(16)
    public void getId_VALID_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladista/2")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.adresa", Matchers.is("Druga avenija 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brojJedinica", Matchers.is(70)))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(17)
    public void getId_INVALID_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladista/22")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji skladište sa tim id")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(22)))
                .andExpect(status().isNotFound());
    }


    @org.junit.jupiter.api.Test
    @Order(18)
    public void post_VALID_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/skladista")
                .content(asJsonString(new Skladiste("Peta avenija 5", 12)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.adresa", Matchers.is("Peta avenija 5")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.brojJedinica", Matchers.is(12)))
                .andExpect(status().isCreated());


        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladista?adresa=Peta avenija 5")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(19)
    public void post_INVALID_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/skladista")
                .content(asJsonString(skladisteRepository.findById(1L)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Proslijeđeni podaci nisu ispravni")))
                .andExpect(status().isExpectationFailed());
    }


    @org.junit.jupiter.api.Test
    @Order(20)
    public void put_VALID_s() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladista/{id}", 1)
                .content(asJsonString(new Skladiste("Prva avenija 2",  20)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.adresa", Matchers.is("Prva avenija 2")))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladista/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.brojJedinica", Matchers.is(20)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.adresa", Matchers.is("Prva avenija 2")))
                .andExpect(status().isOk());
    }


    @org.junit.jupiter.api.Test
    @Order(21)
    public void put_INVALID_s() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladista/{id}", 11)
                .content(asJsonString(new Skladiste("Prva avenija 2",  20)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji skladište sa tim id")))
                .andExpect(status().isNotFound());
    }

    @org.junit.jupiter.api.Test
    @Order(22)
    public void obrisi_VALID_s() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladista/obrisi/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.obrisan", Matchers.is(Boolean.TRUE)))
                .andExpect(status().isOk());
    }


    @org.junit.jupiter.api.Test
    @Order(23)
    public void obrisi_INVALID_s() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladista/obrisi/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Skladište je već obrisano")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(24)
    public void vrati_VALID_s() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladista/vrati/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.obrisan", Matchers.is(Boolean.FALSE)))
                .andExpect(status().isOk());
    }


    @org.junit.jupiter.api.Test
    @Order(25)
    public void vrati_INVALID_s() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladista/vrati/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Skladište nije obrisano")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(26)
    public void delete_VALID_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/skladista/4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladista/4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @org.junit.jupiter.api.Test
    @Order(27)
    public void delete_INVALID_notfound_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/skladista/22")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Greška pri brisanju")))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(28)
    public void delete_INVALID_dependant_s() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/skladista/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Greška pri brisanju")))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(29)
    public void getAll_VALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladjed")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].broj", Matchers.is(154)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].broj", Matchers.is(158)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].broj", Matchers.is(128)))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(30)
    public void getSkladiste_VALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladjed?skladiste=1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].broj", Matchers.is(154)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].broj", Matchers.is(158)))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(30)
    public void getBroj_VALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladjed?broj=128")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].broj", Matchers.is(128)))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(31)
    public void getId_VALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladjed/8")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.broj", Matchers.is(154)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.skladiste.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip.id", Matchers.is(5)))
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(32)
    public void getId_INVALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladjed/22")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji skladišna jedinica sa tim id")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(22)))
                .andExpect(status().isNotFound());
    }

    @org.junit.jupiter.api.Test
    @Order(33)
    public void post_VALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/skladjed")
                .content("{\n" +
                        "         \"skladiste\": 1,\n" +
                        "    \"tip\": \"Malo\",\n" +
                        "    \"broj\": 111\n" +
                        "}\n")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.broj", Matchers.is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.skladiste.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip.id", Matchers.is(6)))
                .andExpect(status().isCreated());


        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladjed?broj=111")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @org.junit.jupiter.api.Test
    @Order(34)
    public void post_INVALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/skladjed")
                .content(asJsonString(new SkladisneJedinice(154, skladisteRepository.findById(1L).get(), tipoviRepository.findByNaziv("Malo"))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Proslijeđeni podaci nisu ispravni")))
                .andExpect(status().isExpectationFailed());
    }

    @org.junit.jupiter.api.Test
    @Order(35)
    public void put_VALID_sj() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladjed/8")
                .content("{\n" +
                        "    \"tip\": \"Malo\"\n" +
                        "}\n")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip.naziv", Matchers.is("Malo")))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladjed/8")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tip.naziv", Matchers.is("Malo")))
                .andExpect(status().isOk());
    }


    @org.junit.jupiter.api.Test
    @Order(36)
    public void put_INVALID_notfound_sj() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladjed/{id}", 88)
                .content("{\n" +
                        "    \"tip\": \"Veliko\"\n" +
                        "}\n")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Ne postoji skladišna jedinica sa tim id")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(88)))
                .andExpect(status().isNotFound());
    }


    @org.junit.jupiter.api.Test
    @Order(37)
    public void put_INVALID_bad_sj() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/skladjed/{id}", 8)
                .content("{\n" +
                        "    \"tip\": \"Mini\"\n" +
                        "}\n")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Serverska greška")))
                .andExpect(status().isInternalServerError());
    }

    @org.junit.jupiter.api.Test
    @Order(38)
    public void delete_VALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/skladjed/8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/skladjed/8")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @org.junit.jupiter.api.Test
    @Order(39)
    public void delete_INVALID_sj() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/skladjed/22")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", Matchers.is("Greška pri brisanju")))
                .andExpect(status().isExpectationFailed());
    }

}