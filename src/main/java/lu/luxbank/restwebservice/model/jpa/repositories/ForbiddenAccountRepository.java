package lu.luxbank.restwebservice.model.jpa.repositories;

import lu.luxbank.restwebservice.model.jpa.entities.ForbiddenAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ForbiddenAccountRepository extends JpaRepository<ForbiddenAccount, UUID> {
    boolean existsByNumber(String number);

}
