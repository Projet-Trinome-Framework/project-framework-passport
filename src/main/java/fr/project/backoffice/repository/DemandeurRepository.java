package fr.project.backoffice.repository;

import fr.project.backoffice.entity.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeurRepository extends JpaRepository<Demandeur, Long> {
    Optional<Demandeur> findByEmail(String email);
    List<Demandeur> findByNomAndPrenom(String nom, String prenom);
    List<Demandeur> findByNomContainingIgnoreCase(String nom);
}
