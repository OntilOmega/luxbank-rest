package lu.luxbank.restwebservice;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link lu.luxbank.restwebservice.model.jpa.entities.User} entity
 */
@AllArgsConstructor
@Getter
public class UserDto implements Serializable {
    private final UUID id;
    @Size(min = 3, max = 50)
    @NotBlank
    private final String name;
}
