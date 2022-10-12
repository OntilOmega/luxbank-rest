package lu.luxbank.restwebservice.controllers;

import lu.luxbank.restwebservice.exeption.ExceptionInfo;
import lu.luxbank.restwebservice.model.jpa.entities.User;
import lu.luxbank.restwebservice.security.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class UsersControllerTest {

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
    @DisplayName("Update user info (bad current password)")
    @Sql({
            "/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",
    })
    void updateUserInfoBadCurrentPassword() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(
                        "/api/v1/users")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"currentPassword\":\"badPassword\",\"newPassword\":\"US64SVBKUS6S3300958879\",\"address\":\"EUR\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(400, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Update user info")
    @Sql({
            "/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",

    })
    void updateUserInfo() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(
                        "/api/v1/users")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"currentPassword\":\"test1@Password\",\"newPassword\":\"US64SVBKUS6S3300958879\",\"address\":\"New Address from user1\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

}
