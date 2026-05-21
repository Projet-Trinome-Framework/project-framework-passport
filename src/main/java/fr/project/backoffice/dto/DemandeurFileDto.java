package fr.project.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Sprint 5: DTO for complete demandeur file with photo and signature
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandeurFileDto {
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private String telephone;
    private String email;
    private String adresse;
    private String situationFamiliale;
    private String nationalite;
    private String photoData; // Base64 encoded
    private String signatureData; // Base64 encoded or SVG
    private Long lastRequestId;
}
