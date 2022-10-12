package lu.luxbank.restwebservice.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.exeption.ExceptionInfo;
import lu.luxbank.restwebservice.model.UpdateUserInfoRequest;
import lu.luxbank.restwebservice.security.InvalidTokenService;
import lu.luxbank.restwebservice.security.JwtTokenUtils;
import lu.luxbank.restwebservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/users",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@Log4j2
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
public class UsersController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils tokenUtils;

    @Autowired
    private InvalidTokenService invalidTokenService;

    @Operation(summary = "Update user info")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address and password have been updated successfully",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Current password is not valid",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PatchMapping("")
    public ResponseEntity<?> updateUserInfo(@RequestBody UpdateUserInfoRequest request,
                                            Principal principal) {
        String username = principal.getName();
        return userService.updateUserInfo(request, username);
    }


    @Operation(summary = "Logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User token has been invalidated successfully",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401",
                    description = "Invalid jwt token",
                    content = @Content(schema = @Schema(hidden = true)))})
    @DeleteMapping(value = "/logout", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String jwt = tokenUtils.getTokenFromHeader(request);
        invalidTokenService.addInvalidToken(jwt);
        return ResponseEntity.ok().build();
    }
}
