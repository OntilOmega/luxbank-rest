package lu.luxbank.restwebservice.model.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@SuperBuilder(setterPrefix = "set")
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Size(min = 3, max = 50)
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @ManyToMany(mappedBy = "users")
    private Set<Account> accounts = new HashSet<>();

    @Size(max = 100)
    private String address;
}
