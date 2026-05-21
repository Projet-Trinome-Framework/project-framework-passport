package fr.project.backoffice.repository;

import fr.project.backoffice.entity.DocumentFile;
import fr.project.backoffice.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentFileRepository extends JpaRepository<DocumentFile, Long> {
    List<DocumentFile> findByDemande(Demande demande);
    List<DocumentFile> findByDemandeAndProcessed(Demande demande, Boolean processed);
    List<DocumentFile> findByScanStatus(String scanStatus);
    Optional<DocumentFile> findByDemande_IdAndPieceJustificative_Id(Long demandeId, Long pieceId);
}
