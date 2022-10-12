package lu.luxbank.restwebservice.external_api.iban;

import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.exeption.LuxBankGeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Service
@Log4j2
public class OpenIbanClient {

    private final RestTemplate rest;
    private final String serverUrl;

    public OpenIbanClient(
            @Value("${openiban.url}") String serverUrl,
            @Value("${openiban.connect-timeout-ms}") int connectTimeout,
            @Value("${openiban.read-timeout-ms}") int readTimeout) {

        rest = new RestTemplate();
        this.serverUrl = serverUrl;
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        rest.setRequestFactory(requestFactory);
    }

    @Cacheable("openIban")
    public boolean isIbanValid(String iban) {
        var uri = UriComponentsBuilder
                .fromHttpUrl(serverUrl + "/validate/" + iban).build();
        ResponseEntity<OpenIbanResponse> response;
        try {
            HttpEntity entity = new HttpEntity<>(null, composeHeaders());
            response = rest.exchange(uri.toUri(), HttpMethod.GET,
                    entity, OpenIbanResponse.class);
        } catch (Exception ex) {
            log.info(ex);
            throw new LuxBankGeneralException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error was occurred when we try to access openiban API");
        }
        return handleResponse(Objects.requireNonNull(response.getBody()));

    }

    private boolean handleResponse(OpenIbanResponse response) {
        return response.isValid();
    }

    private HttpHeaders composeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}
