package lu.luxbank.restwebservice.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * A DTO for the {@link lu.luxbank.restwebservice.model.jpa.entities.Account} entity
 */
@AllArgsConstructor
@Getter
public class AccountDto implements Serializable {
    private final String number;
}
