package fr.project.backoffice.repository;

import fr.project.backoffice.entity.Passeport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasseportRepository extends JpaRepository<Passeport, Long> {
}
