package lu.luxbank.restwebservice.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lu.luxbank.restwebservice.model.enums.Currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link lu.luxbank.restwebservice.model.jpa.entities.Payment} entity
 */
@AllArgsConstructor
@Getter
public class PaymentDto implements Serializable {
    private final UUID id;
    private final LocalDateTime createdDate;
    private final BigDecimal amount;
    private final Currency currency;
    private final AccountDto giverAccount;
    private final String beneficiaryAccountNumber;
    private final String beneficiaryName;
}
