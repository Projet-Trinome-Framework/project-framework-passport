package fr.project.backoffice.repository;

import fr.project.backoffice.entity.Demandeur;
import fr.project.backoffice.entity.Passeport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasseportRepository extends JpaRepository<Passeport, Long> {
    Optional<Passeport> findByNumeroPasseport(String numeroPasseport);
    List<Passeport> findByDemandeur(Demandeur demandeur);
}
