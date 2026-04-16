package fr.project.backoffice.repository;

import fr.project.backoffice.entity.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Long> {
}
