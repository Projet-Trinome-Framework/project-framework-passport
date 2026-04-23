package fr.project.backoffice.repository;

import fr.project.backoffice.entity.DemandePieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandePieceJustificativeRepository extends JpaRepository<DemandePieceJustificative, Long> {
    
    List<DemandePieceJustificative> findByDemandeId(Long idDemande);
    
    List<DemandePieceJustificative> findByDemandeIdAndSoumisTrue(Long idDemande);
}