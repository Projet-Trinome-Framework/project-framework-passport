package fr.project.backoffice.repository;

import fr.project.backoffice.entity.PhotoCapture;
import fr.project.backoffice.entity.Demandeur;
import fr.project.backoffice.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoCaptureRepository extends JpaRepository<PhotoCapture, Long> {
    List<PhotoCapture> findByDemandeur(Demandeur demandeur);
    List<PhotoCapture> findByDemande(Demande demande);
    Optional<PhotoCapture> findByDemandeurAndIsPrimaryTrue(Demandeur demandeur);
}
