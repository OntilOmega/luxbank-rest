package lu.luxbank.restwebservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.model.dtos.AccountDto;
import lu.luxbank.restwebservice.exeption.ExceptionInfo;
import lu.luxbank.restwebservice.exeption.LuxBankGeneralException;
import lu.luxbank.restwebservice.mappers.AccountMapper;
import lu.luxbank.restwebservice.model.jpa.entities.User;
import lu.luxbank.restwebservice.model.jpa.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
@RequestMapping(value = "/api/v1/accounts",
        produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "401",
                description = "Invalid jwt token",
                content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(
                responseCode = "500",
                description = "Unknown internal error",
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(
                                example = """
                                        {"errorMsg": "Unknown internal error"}
                                        """,
                                implementation = ExceptionInfo.class)))
})
public class AccountsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Operation(summary = "List all user's accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List the bank accounts of the user",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Accounts not found",
                    content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> findAllAccounts(Principal user) {
        User userEntity = userRepository.findByName(user.getName()).get();
        if (userEntity.getAccounts().isEmpty()) {
            throw new LuxBankGeneralException(HttpStatus.NOT_FOUND, "Accounts not found");
        }
        return userEntity.getAccounts()
                .stream()
                .map(accountMapper::accountToAccountDto)
                .toList();
    }
}
