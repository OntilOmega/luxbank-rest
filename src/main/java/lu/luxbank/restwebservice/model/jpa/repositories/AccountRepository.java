package lu.luxbank.restwebservice.model.jpa.repositories;

import lu.luxbank.restwebservice.model.jpa.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByNumber(String number);
}
