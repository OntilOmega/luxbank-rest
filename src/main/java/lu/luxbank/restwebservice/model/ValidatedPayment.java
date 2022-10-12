package lu.luxbank.restwebservice.model;

import lombok.*;
import lu.luxbank.restwebservice.model.enums.Currency;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder(setterPrefix = "set")
@AllArgsConstructor
public class ValidatedPayment {
    private String giverAccount;
    private String beneficiaryAccount;
    private BigDecimal amount;
    private Currency currency;
    private String beneficiaryName;
    private boolean beneficiaryInOutBank;

}
