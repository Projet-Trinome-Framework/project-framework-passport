package fr.project.backoffice.repository;

import fr.project.backoffice.entity.DemandeVisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeVisaRepository extends JpaRepository<DemandeVisa, Long> {
}
