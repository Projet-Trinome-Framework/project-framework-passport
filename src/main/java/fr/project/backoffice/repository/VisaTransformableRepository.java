package fr.project.backoffice.repository;

import fr.project.backoffice.entity.VisaTransformable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisaTransformableRepository extends JpaRepository<VisaTransformable, Long> {
}
