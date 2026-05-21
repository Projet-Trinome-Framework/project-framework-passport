package fr.project.backoffice.repository;

import fr.project.backoffice.entity.Demande;
import fr.project.backoffice.entity.Demandeur;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByDemandeurIn(List<Demandeur> demandeurs, Sort sort);
}
