package fr.project.backoffice.repository;

import fr.project.backoffice.entity.QRCodeData;
import fr.project.backoffice.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QRCodeDataRepository extends JpaRepository<QRCodeData, Long> {
    Optional<QRCodeData> findByDemande(Demande demande);
    Optional<QRCodeData> findByRequestToken(String requestToken);
}
