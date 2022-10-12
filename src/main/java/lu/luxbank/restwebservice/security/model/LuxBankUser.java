package lu.luxbank.restwebservice.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class LuxBankUser extends User {

    private UUID id;

    public LuxBankUser(UUID id,
                       String username,
                       String password,
                       Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }
}
