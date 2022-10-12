package lu.luxbank.restwebservice.services;

import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.exeption.LuxBankGeneralException;
import lu.luxbank.restwebservice.model.PaymentRequest;
import lu.luxbank.restwebservice.model.jpa.entities.FraudAttempt;
import lu.luxbank.restwebservice.model.jpa.repositories.ForbiddenAccountRepository;
import lu.luxbank.restwebservice.model.jpa.repositories.FraudAttemptRepository;
import lu.luxbank.restwebservice.security.model.LuxBankUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AntiFraudService {

    @Value(value = "${fraudAttemptsLimit}")
    private long fraudAttemptsLimit;

    @Autowired
    private ForbiddenAccountRepository forbiddenAccountRepository;

    @Autowired
    private FraudAttemptRepository fraudAttemptRepository;

    @Autowired
    private AccountService accountService;


    public void saveFraudAttempt(PaymentRequest payment) {
        LuxBankUser luxBankUser = getLuxBankUser();

        fraudAttemptRepository.save(FraudAttempt.builder()
                .setUserId(luxBankUser.getId())
                .setGiverAccount(payment.getGiverAccount())
                .setBeneficiaryAccount(payment.getBeneficiaryAccount())
                .build());
    }

    public void performAntiFraudTasks(PaymentRequest payment) {
        String beneficiaryAccount = payment.getBeneficiaryAccount();
        if (isAccountForbidden(beneficiaryAccount)) {
            log.info("Payment was cancelled. " +
                    "Trying to pay to forbidden account " + beneficiaryAccount);
            saveFraudAttempt(payment);
            if (isUserExceededFraudPaymentsLimit()) {
                accountService.blockAccountByNumber(payment.getGiverAccount());
            }
            throw new LuxBankGeneralException(HttpStatus.BAD_REQUEST,
                    "Payment forbidden");
        }
    }

    public boolean isAccountForbidden(String account) {
        return forbiddenAccountRepository.existsByNumber(account);
    }

    public long getUsersFraudAttempts() {
        return fraudAttemptRepository.countByUserId(getLuxBankUser().getId());
    }

    public boolean isUserExceededFraudPaymentsLimit() {
        return getUsersFraudAttempts() > fraudAttemptsLimit;
    }


    private static LuxBankUser getLuxBankUser() {
        return (LuxBankUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

}
