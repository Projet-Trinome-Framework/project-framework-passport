package fr.project.backoffice.repository;

import fr.project.backoffice.entity.TypeVisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeVisaRepository extends JpaRepository<TypeVisa, Long> {
}
