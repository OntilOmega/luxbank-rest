package lu.luxbank.restwebservice.model.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity
@NoArgsConstructor
@SuperBuilder(setterPrefix = "set")
@Getter
@Setter
@ToString
@Table(name = "fraud_attempts")
public class FraudAttempt extends BaseEntity {
    @Size(min = 15, max = 100)
    @NotBlank
    private String giverAccount;

    @Size(min = 15, max = 100)
    @NotBlank
    private String beneficiaryAccount;

    private UUID userId;

}