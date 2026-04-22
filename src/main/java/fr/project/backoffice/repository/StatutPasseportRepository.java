package fr.project.backoffice.repository;

import fr.project.backoffice.entity.StatutPasseport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutPasseportRepository extends JpaRepository<StatutPasseport, Long> {
}
