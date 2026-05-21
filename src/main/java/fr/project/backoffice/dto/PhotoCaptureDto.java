package fr.project.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sprint 5: DTO for photo capture
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoCaptureDto {
    private Long id;
    private Long demandeurId;
    private Long demandeId;
    private String photoData; // Base64 encoded
    private String photoFormat;
    private String fileName;
    private Boolean isPrimary;
    private Double qualityScore;
}
