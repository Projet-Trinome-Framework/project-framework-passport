package fr.project.backoffice.repository;

import fr.project.backoffice.entity.TypeDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeDemandeRepository extends JpaRepository<TypeDemande, Long> {
}
