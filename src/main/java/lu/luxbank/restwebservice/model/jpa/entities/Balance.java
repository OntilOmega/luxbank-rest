package lu.luxbank.restwebservice.model.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lu.luxbank.restwebservice.model.enums.BalanceType;
import lu.luxbank.restwebservice.model.enums.Currency;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@SuperBuilder(setterPrefix = "set")
@Getter
@Setter
@ToString
@Entity
@Table(name = "balances")
public class Balance extends BaseEntity {
    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BalanceType type;

}
