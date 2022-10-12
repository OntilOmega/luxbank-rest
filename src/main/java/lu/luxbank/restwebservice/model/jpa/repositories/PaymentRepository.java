package lu.luxbank.restwebservice.model.jpa.repositories;

import lu.luxbank.restwebservice.model.jpa.entities.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    @Query("""
            select p from Payment p inner join p.giverAccount.users users 
            where users.name = :name""")
    List<Payment> findByUsername(@Param("name") String username, Pageable pageable);


    @Query("""
            select p from Payment p inner join p.giverAccount.users users
            where 
                   users.name = :name
               and p.beneficiaryAccountNumber = :beneficiaryAccountNumber
               and p.createdDate >= :dateFrom
               and p.createdDate <= :dateTo
               """)
    List<Payment> findByGiverAccountAndPeriod(
            @Param("name") String username,
            @Param("beneficiaryAccountNumber") String beneficiaryAccountNumber,
            @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo,
            Pageable pageable);

    @Query("""
            select (count(p) > 0) from Payment p inner join p.giverAccount.users users
            where 
                       users.name = :name
                   and p.beneficiaryAccountNumber = :beneficiaryAccountNumber""")
    boolean existsByUsernameAndBeneficiaryAccountNumber(
            @Param("name") String username,
            @Param("beneficiaryAccountNumber") String beneficiaryAccountNumber);
}
