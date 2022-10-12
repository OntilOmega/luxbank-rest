package lu.luxbank.restwebservice.services;

import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.exeption.LuxBankGeneralException;
import lu.luxbank.restwebservice.external_api.iban.OpenIbanClient;
import lu.luxbank.restwebservice.model.PaymentRequest;
import lu.luxbank.restwebservice.model.ValidatedPayment;
import lu.luxbank.restwebservice.model.enums.AccountStatus;
import lu.luxbank.restwebservice.model.enums.Currency;
import lu.luxbank.restwebservice.model.jpa.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class ValidatePaymentService {

    @Autowired
    private OpenIbanClient openIbanClient;

    @Autowired
    private AccountService accountService;


    public ValidatedPayment validate(PaymentRequest payment, String username) {
        String beneficiaryAccNumber = payment.getBeneficiaryAccount();

        validateBeneficiaryIban(beneficiaryAccNumber);

        String giverAccNumber = payment.getGiverAccount();
        Account giverAccount = accountService.findByNumber(giverAccNumber)
                .orElseThrow(() -> new LuxBankGeneralException(HttpStatus.NOT_FOUND,
                        "Giver account not found"));

        isGiverAccountBlocked(giverAccount);

        isGiverAccountBelongsUser(giverAccount, username);

        isPaymentToTheSameAccount(giverAccNumber, beneficiaryAccNumber);

        Optional<Account> optionalBeneficiaryAccount = accountService.findByNumber(beneficiaryAccNumber);


        boolean beneficiaryInOutBank = false;

        if (optionalBeneficiaryAccount.isPresent()) {
            beneficiaryInOutBank = true;
            Account beneficiaryAccount = optionalBeneficiaryAccount.get();
            Currency paymentCurrency = Currency.valueOf(payment.getCurrency());
            if (!(giverAccount.getBalances()
                    .stream()
                    .anyMatch(balance -> balance.getCurrency().equals(paymentCurrency)) &&
                    beneficiaryAccount.getBalances()
                            .stream()
                            .anyMatch(balance -> balance.getCurrency().equals(paymentCurrency)))
            ) {
                throw new LuxBankGeneralException(HttpStatus.BAD_REQUEST,
                        "Payment, giver and beneficiary currencies should be the same");
            }
        }

        return ValidatedPayment.builder()
                .setAmount(payment.getAmount())
                .setCurrency(Currency.valueOf(payment.getCurrency()))
                .setBeneficiaryAccount(beneficiaryAccNumber)
                .setBeneficiaryName(payment.getBeneficiaryName())
                .setGiverAccount(giverAccNumber)
                .setBeneficiaryInOutBank(beneficiaryInOutBank)
                .build();
    }


    private void isPaymentToTheSameAccount(String giverAccNumber, String beneficiaryAccNumber) {
        if (giverAccNumber.equalsIgnoreCase(beneficiaryAccNumber)) {
            throw new LuxBankGeneralException(HttpStatus.BAD_REQUEST,
                    "Payments to the same account is not valid");
        }
    }

    private void isGiverAccountBelongsUser(Account giverAccount, String username) {
        giverAccount.getUsers()
                .stream()
                .filter(user -> user.getName().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(() -> new LuxBankGeneralException(HttpStatus.BAD_REQUEST,
                        "Giver account does not belong to the user"));
    }

    private void isGiverAccountBlocked(Account giverAccount) {
        if (giverAccount.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new LuxBankGeneralException(HttpStatus.BAD_REQUEST,
                    "Giver account is blocked");
        }
    }

    private void validateBeneficiaryIban(String beneficiaryAccount) {
        if (!openIbanClient.isIbanValid(beneficiaryAccount)) {
            throw new LuxBankGeneralException(HttpStatus.BAD_REQUEST,
                    "Beneficiary iban is not valid");
        }

    }
}
