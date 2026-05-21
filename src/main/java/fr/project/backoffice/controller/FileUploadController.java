package fr.project.backoffice.controller;

import fr.project.backoffice.dto.DocumentFileDto;
import fr.project.backoffice.service.FileUploadService;
import fr.project.backoffice.repository.DemandeRepository;
import fr.project.backoffice.repository.PieceJustificativeRepository;
import fr.project.backoffice.entity.Demande;
import fr.project.backoffice.entity.PieceJustificative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Sprint 3: Controller for document file upload and OCR processing
 */
@Controller
@RequestMapping("/backoffice/documents")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private PieceJustificativeRepository pieceJustificativeRepository;

    /**
     * Display document upload form
     */
    @GetMapping("/upload/{demandeId}")
    public String showUploadForm(@PathVariable Long demandeId, Model model) {
        Optional<Demande> demande = demandeRepository.findById(demandeId);
        if (demande.isEmpty()) {
            return "redirect:/backoffice/dashboard";
        }

        model.addAttribute("demande", demande.get());
        model.addAttribute("pieces", pieceJustificativeRepository.findAll());
        
        return "document-upload-form";
    }

    /**
     * Upload a single document
     */
    @PostMapping("/upload/{demandeId}/{pieceId}")
    @ResponseBody
    public ResponseEntity<DocumentFileDto> uploadDocument(
            @PathVariable Long demandeId,
            @PathVariable Long pieceId,
            @RequestParam("file") MultipartFile file) {
        try {
            Optional<Demande> demande = demandeRepository.findById(demandeId);
            Optional<PieceJustificative> piece = pieceJustificativeRepository.findById(pieceId);

            if (demande.isEmpty() || piece.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            DocumentFileDto uploaded = fileUploadService.uploadDocument(demande.get(), piece.get(), file);
            
            // Automatically process the document
            DocumentFileDto processed = fileUploadService.processDocument(uploaded.getId());

            return ResponseEntity.ok(processed);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all documents for a request
     */
    @GetMapping("/{demandeId}")
    @ResponseBody
    public ResponseEntity<List<DocumentFileDto>> getDocuments(@PathVariable Long demandeId) {
        Optional<Demande> demande = demandeRepository.findById(demandeId);
        if (demande.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<DocumentFileDto> documents = fileUploadService.getDocumentsByDemande(demande.get());
        return ResponseEntity.ok(documents);
    }

    /**
     * Process a document (OCR)
     */
    @PostMapping("/process/{documentId}")
    @ResponseBody
    public ResponseEntity<DocumentFileDto> processDocument(@PathVariable Long documentId) {
        try {
            DocumentFileDto result = fileUploadService.processDocument(documentId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a document
     */
    @DeleteMapping("/{documentId}")
    @ResponseBody
    public ResponseEntity<?> deleteDocument(@PathVariable Long documentId) {
        try {
            fileUploadService.deleteDocument(documentId);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get unprocessed documents for a request
     */
    @GetMapping("/{demandeId}/unprocessed")
    @ResponseBody
    public ResponseEntity<List<DocumentFileDto>> getUnprocessedDocuments(@PathVariable Long demandeId) {
        Optional<Demande> demande = demandeRepository.findById(demandeId);
        if (demande.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<DocumentFileDto> documents = fileUploadService.getUnprocessedDocuments(demande.get());
        return ResponseEntity.ok(documents);
    }
}
