package com.nwt.usercontrol.tests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nwt.usercontrol.controller.UserController;
import com.nwt.usercontrol.model.User;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.nwt.usercontrol.repos.UserRepository;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserController.class)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repo;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void init()
    {
        /*repo.save(new User("Mujo", "Mujic", "mujo@gmail.com", "123", 0));
        repo.save(new User("Todd", "Howard", "howard@bethesda.com", "itjustworks", 1));*/
        User u1 = new User("Mujo", "Mujic", "mujo@gmail.com", "123", 0);
        User u2 = new User("Todd", "Howard", "howard@bethesda.com", "itjustworks", 1);
        User u3 = new User("Huse", "Fatkic", "hfatkic@etf.ba", "gauss1234", 0);
        u3.setId(3L);
        u1.setId(1L);
        u2.setId(2L);
        List<User> list = new ArrayList<User>();
        list.add(u1);
        list.add(u2);
        long smece = 2;
        when(repo.findAll()).thenReturn(list);
        when(repo.findById(smece)).thenReturn(u2);
        when(repo.save(u3)).thenReturn(u3);
    }

   @Test
    public void dobaviSveKorisnikeTest() throws Exception{
       mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2))) // da li su vracena 2 objekta
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].ime", Matchers.is("Mujo"))) // da li je prvom ime mujo
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].prezime", Matchers.is("Howard"))); // da li je drugom prezime Howard
   }

   @Test
    public void dobaviOdredjenogKorisnikaTestPogresno() throws Exception{
       mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 123)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
   }

   @Test
    public void obrisiTest() throws Exception{
       mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
   }


    @Test
    public void kreirajTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/users")
                .content(asJsonString(new User("Huse", "Fatkic", "hfatkic@etf.ba", "gauss1234", 0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTest() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                .put("/api/users/{id}", 1)
                .content(asJsonString(new User("Huse", "Fatkic", "hfatkic@etf.ba", "gauss1234", 0)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
