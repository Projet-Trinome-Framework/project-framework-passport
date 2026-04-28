package fr.project.backoffice.repository;

import fr.project.backoffice.entity.CarteResident;
import fr.project.backoffice.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarteResidentRepository extends JpaRepository<CarteResident, Long> {
    List<CarteResident> findByDemande(Demande demande);
    Optional<CarteResident> findTopByOrderByIdDesc();
}
