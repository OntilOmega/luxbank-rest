package lu.luxbank.restwebservice.model.jpa.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lu.luxbank.restwebservice.model.enums.Currency;
import lu.luxbank.restwebservice.model.enums.PaymentStatus;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@SuperBuilder(setterPrefix = "set")
@Getter
@Setter
@ToString
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {
    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "giver_account_id")
    private Account giverAccount;

    @Column(name = "beneficiary_account_number")
    private String beneficiaryAccountNumber;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @Column(name = "communication")
    private String communication;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
