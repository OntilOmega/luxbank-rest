package lu.luxbank.restwebservice.services;

import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.model.UpdateUserInfoRequest;
import lu.luxbank.restwebservice.model.jpa.entities.User;
import lu.luxbank.restwebservice.model.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;


    public ResponseEntity<?> updateUserInfo(UpdateUserInfoRequest request, String username) {
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        if (isUserPasswordInvalid(request.getCurrentPassword(), user)) {
            log.info("Current password is not valid");
            return ResponseEntity.badRequest().build();
        }

        user.setAddress(request.getAddress());
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok().build();
    }


    public boolean isUserPasswordInvalid(String password, User user) {
        String usersEncodedPassword = user.getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return !bCryptPasswordEncoder.matches(password, usersEncodedPassword);
    }

}
