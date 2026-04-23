package fr.project.backoffice.repository;

import fr.project.backoffice.entity.PieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, Long> {
    
    List<PieceJustificative> findByCategorie(String categorie);
    
    List<PieceJustificative> findByCategorieIn(List<String> categories);
    
    List<PieceJustificative> findByObligatoireTrue();
}