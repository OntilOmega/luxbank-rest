package lu.luxbank.restwebservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder(setterPrefix = "set")
@AllArgsConstructor

public class UpdateUserInfoRequest {

    @NotBlank
    @Size(min = 8, max = 100)
    @Schema(description = "Current password", example = "test1@password")
    private String currentPassword;

    @NotBlank
    @Size(min = 8, max = 100)
    @Schema(description = "New password", example = "test1@passwordNew")
    private String newPassword;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "New address", example = "rue de Bouillon L-1248 LUXEMBOURG")
    private String address;
}
