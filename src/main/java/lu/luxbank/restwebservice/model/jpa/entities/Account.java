package lu.luxbank.restwebservice.model.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lu.luxbank.restwebservice.model.enums.AccountStatus;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@SuperBuilder(setterPrefix = "set")
@Getter
@Setter
@ToString
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {
    @Column(name = "number")
    private String number;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_user",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Set<User> users = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_balance",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "balance_id")
    )
    private Set<Balance> balances = new HashSet<>();

}


