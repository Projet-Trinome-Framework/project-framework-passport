package fr.project.backoffice.service;

import fr.project.backoffice.dto.SearchResultDto;
import fr.project.backoffice.repository.DemandeRepository;
import fr.project.backoffice.repository.DemandeurRepository;
import fr.project.backoffice.repository.PasseportRepository;
import fr.project.backoffice.entity.Demande;
import fr.project.backoffice.entity.Demandeur;
import fr.project.backoffice.entity.Passeport;
import fr.project.backoffice.entity.StatutDemande;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Sprint 4: Service for searching requests by request number or passport
 */
@Service
@Transactional(readOnly = true)
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private DemandeurRepository demandeurRepository;

    @Autowired
    private PasseportRepository passeportRepository;

    /**
     * Search by request number (demand ID)
     */
    public List<SearchResultDto> searchByRequestNumber(String requestNumber) {
        try {
            Long demandeId = Long.parseLong(requestNumber);
            List<Demande> demandes = demandeRepository.findById(demandeId)
                    .stream()
                    .collect(Collectors.toList());

            return demandes.stream()
                    .map(this::mapToSearchResult)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            logger.warn("Invalid request number format: {}", requestNumber);
            return List.of();
        }
    }

    /**
     * Search by passport number (chronological order)
     */
    public List<SearchResultDto> searchByPassport(String passeportNumber) {
        List<Passeport> passports = passeportRepository.findByNumeroPasseport(passeportNumber);

        if (passports.isEmpty()) {
            logger.info("No passport found for number: {}", passeportNumber);
            return List.of();
        }

        // Collect all demandeurs from passports
        List<Demandeur> demandeurs = passports.stream()
                .map(Passeport::getDemandeur)
                .distinct()
                .collect(Collectors.toList());

        // Get all demands for these demandeurs, sorted by date (descending)
        List<Demande> demandes = demandeRepository.findByDemandeurIn(demandeurs,
                Sort.by(Sort.Direction.DESC, "dateDemande"));

        return demandes.stream()
                .map(this::mapToSearchResult)
                .toList();
    }

    /**
     * Search with advanced filters
     */
    public List<SearchResultDto> searchAdvanced(String searchTerm, String statut, String typeVisa) {
        // This method can be extended with more complex search logic
        List<SearchResultDto> results = List.of();

        if (searchTerm != null && !searchTerm.isEmpty()) {
            try {
                Long id = Long.parseLong(searchTerm);
                results = searchByRequestNumber(searchTerm);
            } catch (NumberFormatException e) {
                results = searchByPassport(searchTerm);
            }
        }

        // Filter by status if provided
        if (statut != null && !statut.isEmpty() && !results.isEmpty()) {
            results = results.stream()
                    .filter(r -> r.getStatut() != null && r.getStatut().equals(statut))
                    .collect(Collectors.toList());
        }

        // Filter by visa type if provided
        if (typeVisa != null && !typeVisa.isEmpty() && !results.isEmpty()) {
            results = results.stream()
                    .filter(r -> r.getTypeVisa() != null && r.getTypeVisa().equals(typeVisa))
                    .collect(Collectors.toList());
        }

        return results;
    }

    /**
     * Map Demande entity to SearchResultDto
     */
    private SearchResultDto mapToSearchResult(Demande demande) {
        StatutDemande statut = null;
        try {
            // Fetch status label (you may need to adjust this based on your actual entity structure)
            statut = demande.getIdStatut() != null ? 
                    new StatutDemande() : null; // This needs proper implementation
        } catch (Exception e) {
            logger.warn("Error fetching status for demand: {}", demande.getId());
        }

        // Get first passport for this demandeur if available
        String passeportNumber = "N/A";
        try {
            List<Passeport> passports = passeportRepository.findByDemandeur(demande.getDemandeur());
            if (!passports.isEmpty()) {
                passeportNumber = passports.get(0).getNumeroPasseport();
            }
        } catch (Exception e) {
            logger.warn("Error fetching passport for demandeur: {}", demande.getDemandeur().getId());
        }

        return SearchResultDto.builder()
                .demandeId(demande.getId())
                .demandeurNom(demande.getDemandeur().getNom())
                .demandeurPrenom(demande.getDemandeur().getPrenom())
                .requestNumber(String.valueOf(demande.getId()))
                .passeportNumber(passeportNumber)
                .dateDemande(demande.getDateDemande())
                .statut(demande.getIdStatut() != null ? demande.getIdStatut().toString() : "UNKNOWN")
                .typeVisa(demande.getTypeVisa() != null ? demande.getTypeVisa().getLibelle() : "N/A")
                .typeDemande(demande.getTypeDemande() != null ? demande.getTypeDemande().getLibelle() : "N/A")
                .dateTraitement(demande.getDateTraitement() != null ?
                        demande.getDateTraitement().atStartOfDay() : null)
                .build();
    }
}
