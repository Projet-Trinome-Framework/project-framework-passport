package fr.project.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sprint 4: DTO for QR Code response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QRCodeDto {
    private Long id;
    private Long demandeId;
    private String qrCodeImage; // Base64 encoded PNG
    private String requestToken;
    private String requestNumber; // For reference
    private String demandeurName;
}
