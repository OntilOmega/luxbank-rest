package lu.luxbank.restwebservice.security;

import lu.luxbank.restwebservice.model.jpa.repositories.UserRepository;
import lu.luxbank.restwebservice.security.model.LuxBankUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public LuxBankUser loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new LuxBankUser(
                user.getId(),
                username,
                "some user password",
                List.of(new SimpleGrantedAuthority("LUXBANK_USER")));
    }
}
