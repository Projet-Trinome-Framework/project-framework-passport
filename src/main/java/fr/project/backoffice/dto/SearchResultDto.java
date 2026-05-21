package fr.project.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Sprint 4: DTO for search results (by request number or passport)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {
    private Long demandeId;
    private String demandeurNom;
    private String demandeurPrenom;
    private String requestNumber; // Reference to request
    private String passeportNumber;
    private LocalDate dateDemande;
    private String statut;
    private String typeVisa;
    private String typeDemande;
    private LocalDateTime dateTraitement;
}
