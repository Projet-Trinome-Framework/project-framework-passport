package fr.project.backoffice.repository;

import fr.project.backoffice.entity.CarteResident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteResidentRepository extends JpaRepository<CarteResident, Long> {
}
