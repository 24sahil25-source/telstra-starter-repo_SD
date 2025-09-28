package au.com.telstra.simcardactivator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SimActivationRepository extends JpaRepository<SimActivationRecord, Long> {
}
