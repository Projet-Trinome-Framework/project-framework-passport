package fr.project.backoffice.repository;

import fr.project.backoffice.entity.PieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PieceJustificativeRepository extends JpaRepository<PieceJustificative, Long> {
}