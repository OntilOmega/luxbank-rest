package lu.luxbank.restwebservice.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Register a new user")
    @Sql(scripts = {"/sql/deleteTestUser1.sql"})
    @Order(1)
    void registerNewUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\":\"test1\",\"password\":\"test1@2022\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(201, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Register a new user again")
    @Order(2)
    void registerNewUserAgain() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\":\"test1\",\"password\":\"test1@2022\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(409, result.getResponse().getStatus());
    }


    @Test
    void registerNewUserWithShortName() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"name\":\"t\",\"password\":\"test@2022\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(400, result.getResponse().getStatus());
    }

}
