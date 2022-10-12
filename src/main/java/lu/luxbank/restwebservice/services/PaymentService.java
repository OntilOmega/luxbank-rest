package lu.luxbank.restwebservice.services;

import lombok.extern.log4j.Log4j2;
import lu.luxbank.restwebservice.exeption.LuxBankGeneralException;
import lu.luxbank.restwebservice.mappers.PaymentMapper;
import lu.luxbank.restwebservice.model.ValidatedPayment;
import lu.luxbank.restwebservice.model.dtos.PaymentDto;
import lu.luxbank.restwebservice.model.enums.PaymentStatus;
import lu.luxbank.restwebservice.model.jpa.entities.Account;
import lu.luxbank.restwebservice.model.jpa.entities.Balance;
import lu.luxbank.restwebservice.model.jpa.entities.Payment;
import lu.luxbank.restwebservice.model.jpa.repositories.AccountRepository;
import lu.luxbank.restwebservice.model.jpa.repositories.BalanceRepository;
import lu.luxbank.restwebservice.model.jpa.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class PaymentService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private PaymentMapper paymentMapper;


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public void createPayment(ValidatedPayment payment) {

        Account giverAccount = accountRepository
                .findByNumber(payment.getGiverAccount()).get();

        Balance giverAccountBalance = balanceService
                .getAvailableBalance(payment.getCurrency(), giverAccount);

        BigDecimal paymentAmount = payment.getAmount();
        isGiverAccountHasEnoughBalance(paymentAmount, giverAccountBalance);

        giverAccountBalance.setAmount(giverAccountBalance.getAmount().subtract(paymentAmount));
        balanceRepository.save(giverAccountBalance);
        if (payment.isBeneficiaryInOutBank()) {
            Account beneficiaryAccount = accountRepository
                    .findByNumber(payment.getBeneficiaryAccount()).get();

            Balance beneficiaryAccountBalance = balanceService
                    .getAvailableBalance(payment.getCurrency(), beneficiaryAccount);
            beneficiaryAccountBalance.setAmount(beneficiaryAccountBalance.getAmount().add(paymentAmount));
            balanceRepository.save(beneficiaryAccountBalance);
        }

        paymentRepository.save(Payment.builder()
                .setAmount(paymentAmount)
                .setCurrency(payment.getCurrency())
                .setBeneficiaryAccountNumber(payment.getBeneficiaryAccount())
                .setBeneficiaryName(payment.getBeneficiaryName())
                .setStatus(PaymentStatus.EXECUTED)
                .setGiverAccount(giverAccount)
                .build());
    }

    private void isGiverAccountHasEnoughBalance(BigDecimal amount, Balance giverAccountBalance) {
        if (giverAccountBalance.getAmount().compareTo(amount) < 0) {
            throw new LuxBankGeneralException(HttpStatus.BAD_REQUEST, "Insufficient funds");
        }
    }


    public Page<List<PaymentDto>> listAllPayments(String username, Pageable pageable) {
        pageable = deleteSorting(pageable);
        List<PaymentDto> paymentDtos = paymentRepository.findByUsername(username, pageable)
                .stream()
                .map(paymentMapper::paymentToPaymentDto)
                .sorted(Comparator.comparing(PaymentDto::getCreatedDate).reversed())
                .toList();
        if (paymentDtos.isEmpty()) {
            throw new LuxBankGeneralException(HttpStatus.NOT_FOUND,
                    "Payments not found");
        }
        return new PageImpl(paymentDtos, pageable, paymentDtos.size());
    }


    public Page<List<PaymentDto>> listAllPayments(String username,
                                                  String beneficiaryAccountNumber,
                                                  LocalDate dateFrom, LocalDate dateTo,
                                                  Pageable pageable) {
        pageable = deleteSorting(pageable);
        if (!paymentRepository.existsByUsernameAndBeneficiaryAccountNumber
                (username, beneficiaryAccountNumber)) {
            throw new LuxBankGeneralException(HttpStatus.NOT_FOUND,
                    "Payments with this beneficiary account number not found");
        }
        List<PaymentDto> paymentDtos = paymentRepository.findByGiverAccountAndPeriod(
                        username,
                        beneficiaryAccountNumber,
                        LocalDateTime.from(dateFrom.atStartOfDay()),
                        LocalDateTime.from(dateTo.atStartOfDay()),
                        pageable)
                .stream()
                .map(paymentMapper::paymentToPaymentDto)
                .sorted(Comparator.comparing(PaymentDto::getCreatedDate).reversed())
                .toList();
        if (paymentDtos.isEmpty()) {
            throw new LuxBankGeneralException(HttpStatus.NOT_FOUND,
                    "Payments not found");
        }
        return new PageImpl(paymentDtos, pageable, paymentDtos.size());
    }


    public void deletePayment(UUID id, String username) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new LuxBankGeneralException(HttpStatus.NOT_FOUND,
                        "Payment not found"));

        payment.getGiverAccount().getUsers()
                .stream()
                .filter(user -> user.getName().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(() -> new LuxBankGeneralException(HttpStatus.BAD_REQUEST,
                        "Payment doesn't belong to the user"));

        paymentRepository.deleteById(id);
    }


    private Pageable deleteSorting(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.unsorted());
    }
}
