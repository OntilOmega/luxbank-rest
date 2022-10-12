package lu.luxbank.restwebservice.security;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InvalidTokenService {

    // Redis will be better)

    private Map<String, LocalDateTime> invalidTokens = new ConcurrentHashMap<>();

    public void addInvalidToken(String jwt) {
         invalidTokens.put(jwt, LocalDateTime.now());
    }

    public boolean isTokenInvalid(String jwt) {
        return invalidTokens.containsKey(jwt);
    }
}
