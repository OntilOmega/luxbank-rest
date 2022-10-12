package lu.luxbank.restwebservice.security.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "User login name", example = "test1")
    private String name;

    @NotBlank
    @Size(min = 8, max = 100)
    @Schema(description = "User password", example = "test1@password")
    private String password;
}
