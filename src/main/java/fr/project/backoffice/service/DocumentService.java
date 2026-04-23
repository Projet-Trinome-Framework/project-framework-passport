package fr.project.backoffice.service;

import fr.project.backoffice.entity.DemandePieceJustificative;
import fr.project.backoffice.entity.PieceJustificative;
import fr.project.backoffice.repository.DemandePieceJustificativeRepository;
import fr.project.backoffice.repository.PieceJustificativeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentService {

    private final PieceJustificativeRepository pieceJustificativeRepository;
    private final DemandePieceJustificativeRepository demandePieceJustificativeRepository;

    public DocumentService(PieceJustificativeRepository pieceJustificativeRepository,
                        DemandePieceJustificativeRepository demandePieceJustificativeRepository) {
        this.pieceJustificativeRepository = pieceJustificativeRepository;
        this.demandePieceJustificativeRepository = demandePieceJustificativeRepository;
    }

    // Get all documents by category
    public List<PieceJustificative> getDocumentsByCategory(String categorie) {
        return pieceJustificativeRepository.findByCategorie(categorie);
    }

    // Get all common documents
    public List<PieceJustificative> getCommonDocuments() {
        return pieceJustificativeRepository.findByCategorie("commun");
    }

    // Get documents for specific categories (for dynamic form display)
    public List<PieceJustificative> getDocumentsByCategories(List<String> categories) {
        return pieceJustificativeRepository.findByCategorieIn(categories);
    }

    // Get all mandatory documents
    public List<PieceJustificative> getMandatoryDocuments() {
        return pieceJustificativeRepository.findByObligatoireTrue();
    }

    // Save submitted documents for a demand
    public void saveSubmittedDocuments(Long idDemande, List<Long> documentIds) {
        for (Long documentId : documentIds) {
            DemandePieceJustificative demandePiece = new DemandePieceJustificative();
            
            // Set the demand (you'll need to fetch it from DemandeRepository)
            // demandePiece.setDemande(demande);
            
            // Set the piece justificative
            PieceJustificative piece = pieceJustificativeRepository.findById(documentId).orElse(null);
            demandePiece.setPieceJustificative(piece);
            
            // Mark as submitted
            demandePiece.setSoumis(true);
            demandePiece.setDateSoumission(LocalDate.now());
            
            demandePieceJustificativeRepository.save(demandePiece);
        }
    }

    // Get submitted documents for a demand
    public List<DemandePieceJustificative> getSubmittedDocuments(Long idDemande) {
        return demandePieceJustificativeRepository.findByDemandeId(idDemande);
    }

    // Get submitted and validated documents for a demand
    public List<DemandePieceJustificative> getSubmittedAndValidatedDocuments(Long idDemande) {
        return demandePieceJustificativeRepository.findByDemandeIdAndSoumisTrue(idDemande);
    }

    // Update document validation status
    public void updateDocumentValidation(Long demandePieceId, Boolean validee, String commentaire) {
        DemandePieceJustificative demandePiece = demandePieceJustificativeRepository.findById(demandePieceId).orElse(null);
        if (demandePiece != null) {
            demandePiece.setValidee(validee);
            demandePiece.setCommentaire(commentaire);
            if (validee) {
                demandePiece.setDateValidation(LocalDate.now());
            }
            demandePieceJustificativeRepository.save(demandePiece);
        }
    }
}
