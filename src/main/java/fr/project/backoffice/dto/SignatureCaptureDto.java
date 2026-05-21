package fr.project.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sprint 5: DTO for signature capture
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureCaptureDto {
    private Long id;
    private Long demandeurId;
    private Long demandeId;
    private String signatureData; // SVG or base64 image
    private String signatureFormat;
    private String fileName;
    private Boolean isValid;
}
