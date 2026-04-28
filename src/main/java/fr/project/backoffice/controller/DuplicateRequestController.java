package fr.project.backoffice.controller;

import fr.project.backoffice.dto.DemandeurSearchResultDto;
import fr.project.backoffice.dto.DuplicateRequestDto;
import fr.project.backoffice.entity.PieceJustificative;
import fr.project.backoffice.service.DocumentService;
import fr.project.backoffice.service.DuplicateRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/backoffice/duplicates")
public class DuplicateRequestController {

    private final DuplicateRequestService duplicateRequestService;
    private final DocumentService documentService;

    public DuplicateRequestController(DuplicateRequestService duplicateRequestService,
                                      DocumentService documentService) {
        this.duplicateRequestService = duplicateRequestService;
        this.documentService = documentService;
    }

    /**
     * Display the duplicate request form
     */
    @GetMapping("/form")
    public String duplicateForm(Model model) {
        // Load documents from database
        List<PieceJustificative> commonDocuments = documentService.getCommonDocuments();
        List<PieceJustificative> investorDocuments = documentService.getDocumentsByCategory("investisseur");
        List<PieceJustificative> workerDocuments = documentService.getDocumentsByCategory("travailleur");

        model.addAttribute("commonDocuments", commonDocuments);
        model.addAttribute("investorDocuments", investorDocuments);
        model.addAttribute("workerDocuments", workerDocuments);

        return "duplicate-request-form";
    }

    /**
     * Search for an existing applicant
     */
    @PostMapping("/search-applicant")
    public ResponseEntity<?> searchApplicant(@RequestParam(required = false) String email,
                                              @RequestParam(required = false) String nom) {
        if ((email == null || email.isEmpty()) && (nom == null || nom.isEmpty())) {
            return ResponseEntity.badRequest().body("Email ou nom requis");
        }

        Optional<DemandeurSearchResultDto> result = duplicateRequestService.searchApplicant(email, nom);
        return ResponseEntity.ok(result);
    }

    /**
     * Submit a visa transfer request (lost passport)
     */
    @PostMapping("/visa-transfer")
    public String submitVisaTransferRequest(DuplicateRequestDto dto, RedirectAttributes redirectAttributes) {
        try {
            if (!"passeport_perdu".equals(dto.getTypesDuplicate())) {
                throw new IllegalArgumentException("Type de duplicata invalide");
            }
            duplicateRequestService.createVissaTransferRequest(dto);
            redirectAttributes.addFlashAttribute("success", 
                "Demande de transfert de visa créée avec succès ! Statut : Approuvée");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erreur lors de la création de la demande : " + e.getMessage());
        }
        return "redirect:/backoffice/duplicates/form";
    }

    /**
     * Submit a resident card duplicate request
     */
    @PostMapping("/resident-card-duplicate")
    public String submitCarteResidentDuplicateRequest(DuplicateRequestDto dto, RedirectAttributes redirectAttributes) {
        try {
            if (!"carte_resident_perdue".equals(dto.getTypesDuplicate())) {
                throw new IllegalArgumentException("Type de duplicata invalide");
            }
            duplicateRequestService.createCarteResidentDuplicateRequest(dto);
            redirectAttributes.addFlashAttribute("success", 
                "Demande de duplicata de carte résident créée avec succès ! Statut : Approuvée");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Erreur lors de la création de la demande : " + e.getMessage());
        }
        return "redirect:/backoffice/duplicates/form";
    }
}
