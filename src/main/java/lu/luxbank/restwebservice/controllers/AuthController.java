package lu.luxbank.restwebservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.exeption.ExceptionInfo;
import lu.luxbank.restwebservice.model.jpa.entities.User;
import lu.luxbank.restwebservice.model.jpa.repositories.UserRepository;
import lu.luxbank.restwebservice.security.JwtTokenUtils;
import lu.luxbank.restwebservice.security.model.LoginRequest;
import lu.luxbank.restwebservice.security.model.SignupRequest;
import lu.luxbank.restwebservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Log4j2
@RequestMapping(value = "/api/v1/auth",
        consumes = MediaType.APPLICATION_JSON_VALUE)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "500",
                description = "Unknown internal error",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(
                                example = """
                                        {"errorMsg": "Unknown internal error"}
                                        """,
                                implementation = ExceptionInfo.class)))})
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserService userService;


    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerNewUser(
            @Valid @RequestBody SignupRequest signUpRequest) {
        String username = signUpRequest.getName();
        if (userRepository.existsByName(username)) {
            log.info("User already exists");
            return new ResponseEntity("User already exists", HttpStatus.CONFLICT);
        }

        userRepository.save(User.builder()
                .setName(username)
                .setPassword(encoder.encode(signUpRequest.getPassword()))
                .build());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Authenticate user and get jwt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User authenticated successfully and jwt token has been generated",
                    content = @Content(schema = @Schema(example = """
                            eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MSIsImlzcyI6Ikx1eEJhbmsiLCJleHAiOjE2NjU0MzY4ODN9.67prSujS5elbDV4G_7gmmoldFWfuPNqMPvgLtStu2hY
                            """))),
            @ApiResponse(responseCode = "401",
                    description = "User not found or bad credentials",
                    content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping(value = "/signin", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByName(loginRequest.getName());
        if (user.isEmpty() ||
                !userService.isUserPasswordValid(loginRequest.getPassword(), user.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(jwtTokenUtils.generateToken(user.get()));
    }

}
