package lu.luxbank.restwebservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.luxbank.restwebservice.exeption.ExceptionInfo;
import lu.luxbank.restwebservice.model.jpa.entities.User;
import lu.luxbank.restwebservice.security.JwtTokenUtils;
import org.junit.jupiter.api.*;
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
class PaymentsControllerTest {
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
    @DisplayName("Giver account doesn't belong to the user")
    @Sql(scripts = {"/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",
            "/sql/deleteTestAccount1.sql",
            "/sql/addTestAccount1.sql",
    })
    void payment_wrongGiverUser() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"giverAccount\":\"DE89370400440532013000\",\"beneficiaryAccount\":\"US64SVBKUS6S3300958879\",\"amount\":1.55,\"currency\":\"EUR\",\"beneficiaryName\":\"Beneficiary name\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertEquals(400, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Payment to the same account")
    @Sql(scripts = {"/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",
            "/sql/deleteTestAccount1.sql",
            "/sql/addTestAccount1.sql",
            "/sql/assignAccount1ToUser1.sql"
    })
    void payment_sameAccount() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"giverAccount\":\"DE89370400440532013000\",\"beneficiaryAccount\":\"DE89370400440532013000\",\"amount\":0.1,\"currency\":\"EUR\",\"beneficiaryName\":\"Beneficiary name\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    @DisplayName("Payment from the account with low balance")
    @Sql(scripts = {"/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",
            "/sql/deleteTestAccount1.sql",
            "/sql/addTestAccount1.sql",
            "/sql/assignAccount1ToUser1.sql",
            "/sql/makeAccount1Blocked.sql"
    })
    void payment_giverAccountBlocked() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"giverAccount\":\"DE89370400440532013000\",\"beneficiaryAccount\":\"US64SVBKUS6S3300958879\",\"amount\":100.0,\"currency\":\"EUR\",\"beneficiaryName\":\"Beneficiary name\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertEquals(400, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Payment, giver and beneficiary currencies should be the same")
    @Sql(scripts = {"/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",
            "/sql/deleteTestAccount1.sql",
            "/sql/addTestAccount1.sql",
            "/sql/assignAccount1ToZeroEURBalance.sql",
            "/sql/assignAccount1ToUser1.sql",
            "/sql/deleteTestAccount2.sql",
            "/sql/addTestAccount2.sql",
            "/sql/assignAccount2ToZeroEURBalance.sql"
    })
    void payment_paymentBeneficiaryGiverCurrenciesTheSame() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"giverAccount\":\"DE89370400440532013000\",\"beneficiaryAccount\":\"US64SVBKUS6S3300958879\",\"amount\":100.0,\"currency\":\"USD\",\"beneficiaryName\":\"Beneficiary name\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertEquals(400, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Payment from the account with low balance")
    @Sql(scripts = {"/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",
            "/sql/deleteTestAccount1.sql",
            "/sql/addTestAccount1.sql",
            "/sql/assignAccount1ToUser1.sql",
            "/sql/assignAccount1ToZeroEURBalance.sql",
            "/sql/deleteTestAccount2.sql",
            "/sql/addTestAccount2.sql",
            "/sql/assignAccount2ToZeroEURBalance.sql"
    })
    void payment_lowBalance() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"giverAccount\":\"DE89370400440532013000\",\"beneficiaryAccount\":\"US64SVBKUS6S3300958879\",\"amount\":100.0,\"currency\":\"EUR\",\"beneficiaryName\":\"Beneficiary name\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertEquals(400, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Trying to pay to forbidden account")
    void payment_forbiddenAccount() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"giverAccount\":\"DE89370400440532013000\",\"beneficiaryAccount\":\"LU280019400644750000\",\"amount\":100.0,\"currency\":\"EUR\",\"beneficiaryName\":\"Beneficiary name\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertEquals(400, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Beneficiary iban is not valid")
    void payment_ibanInvalid() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"giverAccount\":\"DE89370400440532013000\",\"beneficiaryAccount\":\"LU2800194006447500001\",\"amount\":100.0,\"currency\":\"EUR\",\"beneficiaryName\":\"Beneficiary name\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        assertEquals(400, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("Payment successful")
    @Sql(scripts = {"/sql/deleteTestUser1.sql",
            "/sql/addTestUser1.sql",
            "/sql/deleteTestAccount1.sql",
            "/sql/addTestAccount1.sql",
            "/sql/assignAccount1ToUser1.sql",
            "/sql/assignAccount1ToZeroEURBalance.sql",
            "/sql/deleteTestAccount2.sql",
            "/sql/addTestAccount2.sql",
            "/sql/assignAccount2ToZeroEURBalance.sql",
            "/sql/assignAccount1Balance1000EUR.sql",
    })
    void createPayment() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{\"giverAccount\":\"DE89370400440532013000\",\"beneficiaryAccount\":\"US64SVBKUS6S3300958879\",\"amount\":100.0,\"currency\":\"EUR\",\"beneficiaryName\":\"Beneficiary name\"}");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(201, result.getResponse().getStatus());
    }


    @Test
    @DisplayName("List all payments ordered by creation date")
    void listAllPayments() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                        "/api/v1/payments")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }


    @Test
    @DisplayName("List all payments to given beneficiary and period")
    void listAllPaymentsByBeneficiary() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                        "/api/v1/payments/US64SVBKUS6S3300958879?dateFrom=2022-01-04&dateTo=2023-10-04")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        String contentAsString = result.getResponse().getContentAsString();
        System.out.println(contentAsString);
    }


    @Test
    @DisplayName("Delete payment")
    @Sql({"/sql/deleteAllPayments.sql",
            "/sql/addPaymentUser1.sql",
    })
    void deletePayment() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                        "/api/v1/payments/1cf5f9b8-240c-48e7-af2c-c6b008834414")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + jwtToken);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(204, result.getResponse().getStatus());
    }
}
