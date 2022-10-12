package lu.luxbank.restwebservice.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class WellcomeController {

    @RequestMapping(
            value = "/",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String homePage() {
        return "LuxBank REST Api is up and running";
    }
}
