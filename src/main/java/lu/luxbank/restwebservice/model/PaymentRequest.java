package lu.luxbank.restwebservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lu.luxbank.restwebservice.model.enums.Currency;
import lu.luxbank.restwebservice.validation.ValueOfEnum;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder(setterPrefix = "set")
@AllArgsConstructor
public class PaymentRequest {

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Giver account", example = "DE89370400440532013000")
    @Pattern(regexp = "^[A-Z0-9]*$")
    private String giverAccount;

    @NotBlank
    @Size(min = 15, max = 100)
    @Schema(description = "Beneficiary account", example = "US64SVBKUS6S3300958879")
    @Pattern(regexp = "^[A-Z0-9]*$")
    private String beneficiaryAccount;

    @DecimalMin(value = "0.01")
    @Schema(description = "Payment amount", example = "10.00")
    private BigDecimal amount;

    @NotNull
    @ValueOfEnum(enumClass = Currency.class)
    @Schema(description = "Payment currency", example = "EUR")
    private String currency;

    @NotBlank
    @Size(min = 15, max = 100)
    @Schema(description = "Beneficiary name", example = "Beneficiary name")
    private String beneficiaryName;
}
