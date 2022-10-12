package lu.luxbank.restwebservice.services;

import lu.luxbank.restwebservice.exeption.LuxBankGeneralException;
import lu.luxbank.restwebservice.model.enums.AccountStatus;
import lu.luxbank.restwebservice.model.jpa.entities.Account;
import lu.luxbank.restwebservice.model.jpa.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    public Optional<Account> findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    public void blockAccountByNumber(String number) {
        Account account = findByNumber(number)
                .orElseThrow(() -> new LuxBankGeneralException(HttpStatus.NOT_FOUND,
                        "Account not found"));
        if (account.getStatus().equals(AccountStatus.ENABLED)) {
            account.setStatus(AccountStatus.BLOCKED);
            accountRepository.saveAndFlush(account);
        }
    }
}
