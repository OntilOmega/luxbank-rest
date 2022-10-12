package lu.luxbank.restwebservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lu.luxbank.restwebservice.model.enums.AccountStatus;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link lu.luxbank.restwebservice.model.jpa.entities.Account} entity
 */
@AllArgsConstructor
@Getter
public class AccountDto implements Serializable {
    private final UUID id;
    private final String number;
    private final String name;
    private final AccountStatus status;
    private final Set<UserDto> users;
    private final Set<BalanceDto> balances;
}
