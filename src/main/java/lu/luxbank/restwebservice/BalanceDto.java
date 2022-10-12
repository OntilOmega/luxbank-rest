package lu.luxbank.restwebservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lu.luxbank.restwebservice.model.enums.BalanceType;
import lu.luxbank.restwebservice.model.enums.Currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link lu.luxbank.restwebservice.model.jpa.entities.Balance} entity
 */
@AllArgsConstructor
@Getter
public class BalanceDto implements Serializable {
    private final LocalDateTime modifiedDate;
    private final BigDecimal amount;
    private final Currency currency;
    private final BalanceType type;
}
