package fr.project.backoffice.controller;

import fr.project.backoffice.entity.DemandePieceJustificative;
import fr.project.backoffice.entity.PieceJustificative;
import fr.project.backoffice.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // Get documents by category
    @GetMapping("/category/{categorie}")
    public ResponseEntity<List<PieceJustificative>> getDocumentsByCategory(@PathVariable String categorie) {
        List<PieceJustificative> documents = documentService.getDocumentsByCategory(categorie);
        return ResponseEntity.ok(documents);
    }

    // Get common documents
    @GetMapping("/common")
    public ResponseEntity<List<PieceJustificative>> getCommonDocuments() {
        List<PieceJustificative> documents = documentService.getCommonDocuments();
        return ResponseEntity.ok(documents);
    }

    // Get documents for specific categories
    @PostMapping("/by-categories")
    public ResponseEntity<List<PieceJustificative>> getDocumentsByCategories(@RequestBody List<String> categories) {
        List<PieceJustificative> documents = documentService.getDocumentsByCategories(categories);
        return ResponseEntity.ok(documents);
    }

    // Get all mandatory documents
    @GetMapping("/mandatory")
    public ResponseEntity<List<PieceJustificative>> getMandatoryDocuments() {
        List<PieceJustificative> documents = documentService.getMandatoryDocuments();
        return ResponseEntity.ok(documents);
    }

    // Get submitted documents for a demand
    @GetMapping("/submitted/{idDemande}")
    public ResponseEntity<List<DemandePieceJustificative>> getSubmittedDocuments(@PathVariable Long idDemande) {
        List<DemandePieceJustificative> documents = documentService.getSubmittedDocuments(idDemande);
        return ResponseEntity.ok(documents);
    }

    // Get submitted and validated documents for a demand
    @GetMapping("/submitted-validated/{idDemande}")
    public ResponseEntity<List<DemandePieceJustificative>> getSubmittedAndValidatedDocuments(@PathVariable Long idDemande) {
        List<DemandePieceJustificative> documents = documentService.getSubmittedAndValidatedDocuments(idDemande);
        return ResponseEntity.ok(documents);
    }

    // Save submitted documents for a demand
    @PostMapping("/submit/{idDemande}")
    public ResponseEntity<Void> submitDocuments(@PathVariable Long idDemande, @RequestBody List<Long> documentIds) {
        documentService.saveSubmittedDocuments(idDemande, documentIds);
        return ResponseEntity.ok().build();
    }

    // Update document validation status
    @PutMapping("/validate/{demandePieceId}")
    public ResponseEntity<Void> validateDocument(
            @PathVariable Long demandePieceId,
            @RequestBody ValidationRequest validationRequest) {
        documentService.updateDocumentValidation(
                demandePieceId, 
                validationRequest.getValidee(), 
                validationRequest.getCommentaire()
        );
        return ResponseEntity.ok().build();
    }

    // DTO for validation request
    public static class ValidationRequest {
        private Boolean validee;
        private String commentaire;

        public Boolean getValidee() {
            return validee;
        }

        public void setValidee(Boolean validee) {
            this.validee = validee;
        }

        public String getCommentaire() {
            return commentaire;
        }

        public void setCommentaire(String commentaire) {
            this.commentaire = commentaire;
        }
    }
}
