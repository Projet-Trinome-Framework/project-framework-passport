package fr.project.backoffice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Sprint 3: DTO for document file upload and processing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFileDto {
    private Long id;
    private Long demandeId;
    private Long pieceJustificativeId;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadDate;
    private Boolean processed;
    private String scanStatus; // PENDING, IN_PROGRESS, COMPLETED, FAILED
    private String extractedText;
    private Double ocrConfidence;
    private String processingError;
    private LocalDateTime processingDate;
}
