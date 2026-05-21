package fr.project.backoffice.repository;

import fr.project.backoffice.entity.SignatureCapture;
import fr.project.backoffice.entity.Demandeur;
import fr.project.backoffice.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SignatureCaptureRepository extends JpaRepository<SignatureCapture, Long> {
    List<SignatureCapture> findByDemandeur(Demandeur demandeur);
    Optional<SignatureCapture> findByDemande(Demande demande);
    Optional<SignatureCapture> findByDemandeurAndIsValidTrue(Demandeur demandeur);
}
