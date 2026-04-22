package fr.project.backoffice.repository;

import fr.project.backoffice.entity.DemandePieceJustificative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandePieceJustificativeRepository extends JpaRepository<DemandePieceJustificative, Long> {
}