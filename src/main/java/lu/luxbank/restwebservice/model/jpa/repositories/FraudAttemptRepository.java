package lu.luxbank.restwebservice.model.jpa.repositories;

import lu.luxbank.restwebservice.model.jpa.entities.FraudAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FraudAttemptRepository extends JpaRepository<FraudAttempt, UUID> {
    long countByUserId(UUID userId);

}