package lu.luxbank.restwebservice.model.jpa.repositories;

import lu.luxbank.restwebservice.model.jpa.entities.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {
}
