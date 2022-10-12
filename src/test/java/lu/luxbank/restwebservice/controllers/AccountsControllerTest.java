package lu.luxbank.restwebservice.controllers;

import lu.luxbank.restwebservice.model.jpa.entities.User;
import lu.luxbank.restwebservice.security.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class AccountsControllerTest {

    private String jwtToken;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void generateJwtToken() {
        jwtToken = jwtTokenUtils.generateToken(
                User.builder()
                        .setName("test1")
                        .build());
    }


    @Test
    @Sql({
            "/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",
            "/sql/deleteTestAccount1.sql",
            "/sql/deleteTestAccount2.sql",
            "/sql/addTestAccount1.sql",
            "/sql/addTestAccount2.sql",
            "/sql/assignAccount1ToUser1.sql",
            "/sql/assignAccount2ToUser1.sql",
    })
    void findAllAccounts() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                        "/api/v1/accounts")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}
