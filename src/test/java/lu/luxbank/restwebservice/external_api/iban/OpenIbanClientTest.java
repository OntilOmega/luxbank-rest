package lu.luxbank.restwebservice.external_api.iban;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OpenIbanClientTest {

    @Autowired
    private OpenIbanClient openIbanClient;


    @Test
    void isIbanValid() {
        assertTrue(openIbanClient.isIbanValid("LU280019400644750000"));
        assertTrue(openIbanClient.isIbanValid("LU280019400644750000"));
        assertTrue(openIbanClient.isIbanValid("LU280019400644750000"));
        assertTrue(openIbanClient.isIbanValid("LU280019400644750000"));

    }

    @Test
    void isIbanNotValid() {
        assertFalse(openIbanClient.isIbanValid("LU12001000123456@891"));
    }
}
