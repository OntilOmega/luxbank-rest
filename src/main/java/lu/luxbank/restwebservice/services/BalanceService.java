package lu.luxbank.restwebservice.services;


import lu.luxbank.restwebservice.exeption.LuxBankGeneralException;
import lu.luxbank.restwebservice.model.enums.BalanceType;
import lu.luxbank.restwebservice.model.enums.Currency;
import lu.luxbank.restwebservice.model.jpa.entities.Account;
import lu.luxbank.restwebservice.model.jpa.entities.Balance;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {


    public Balance getAvailableBalance(Currency currency, Account account) {
        return account.getBalances()
                .stream()
                .filter(balance -> balance.getType().equals(BalanceType.AVAILABLE) &&
                        balance.getCurrency().equals(currency))
                .findFirst().orElseThrow(
                        () -> new LuxBankGeneralException(HttpStatus.BAD_REQUEST,
                                "Account doesn't have available balance"));
    }

}
