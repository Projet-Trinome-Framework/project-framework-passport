package fr.project.backoffice.service;

import fr.project.backoffice.dto.DemandeurSearchResultDto;
import fr.project.backoffice.dto.DuplicateRequestDto;
import fr.project.backoffice.entity.CarteResident;
import fr.project.backoffice.entity.Demande;
import fr.project.backoffice.entity.Demandeur;
import fr.project.backoffice.entity.DemandePieceJustificative;
import fr.project.backoffice.entity.Nationalite;
import fr.project.backoffice.entity.Passeport;
import fr.project.backoffice.entity.SituationFamiliale;
import fr.project.backoffice.entity.TypeDemande;
import fr.project.backoffice.entity.TypeVisa;
import fr.project.backoffice.entity.Visa;
import fr.project.backoffice.entity.VisaTransformable;
import fr.project.backoffice.repository.CarteResidentRepository;
import fr.project.backoffice.repository.DemandeRepository;
import fr.project.backoffice.repository.DemandeurRepository;
import fr.project.backoffice.repository.DemandePieceJustificativeRepository;
import fr.project.backoffice.repository.NationaliteRepository;
import fr.project.backoffice.repository.PasseportRepository;
import fr.project.backoffice.repository.PieceJustificativeRepository;
import fr.project.backoffice.repository.SituationFamilialeRepository;
import fr.project.backoffice.repository.TypeDemandeRepository;
import fr.project.backoffice.repository.TypeVisaRepository;
import fr.project.backoffice.repository.VisaRepository;
import fr.project.backoffice.repository.VisaTransformableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DuplicateRequestService {

    private final DemandeurRepository demandeurRepository;
    private final DemandeRepository demandeRepository;
    private final VisaRepository visaRepository;
    private final PasseportRepository passeportRepository;
    private final CarteResidentRepository carteResidentRepository;
    private final SituationFamilialeRepository situationFamilialeRepository;
    private final NationaliteRepository nationaliteRepository;
    private final TypeVisaRepository typeVisaRepository;
    private final TypeDemandeRepository typeDemandeRepository;
    private final VisaTransformableRepository visaTransformableRepository;
    private final DemandePieceJustificativeRepository demandePieceJustificativeRepository;
    private final PieceJustificativeRepository pieceJustificativeRepository;

    public DuplicateRequestService(DemandeurRepository demandeurRepository,
                                   DemandeRepository demandeRepository,
                                   VisaRepository visaRepository,
                                   PasseportRepository passeportRepository,
                                   CarteResidentRepository carteResidentRepository,
                                   SituationFamilialeRepository situationFamilialeRepository,
                                   NationaliteRepository nationaliteRepository,
                                   TypeVisaRepository typeVisaRepository,
                                   TypeDemandeRepository typeDemandeRepository,
                                   VisaTransformableRepository visaTransformableRepository,
                                   DemandePieceJustificativeRepository demandePieceJustificativeRepository,
                                   PieceJustificativeRepository pieceJustificativeRepository) {
        this.demandeurRepository = demandeurRepository;
        this.demandeRepository = demandeRepository;
        this.visaRepository = visaRepository;
        this.passeportRepository = passeportRepository;
        this.carteResidentRepository = carteResidentRepository;
        this.situationFamilialeRepository = situationFamilialeRepository;
        this.nationaliteRepository = nationaliteRepository;
        this.typeVisaRepository = typeVisaRepository;
        this.typeDemandeRepository = typeDemandeRepository;
        this.visaTransformableRepository = visaTransformableRepository;
        this.demandePieceJustificativeRepository = demandePieceJustificativeRepository;
        this.pieceJustificativeRepository = pieceJustificativeRepository;
    }

    /**
     * Search for an existing applicant by email or name
     */
    public Optional<DemandeurSearchResultDto> searchApplicant(String email, String nom) {
        Optional<Demandeur> demandeur = Optional.empty();

        if (email != null && !email.isEmpty()) {
            demandeur = demandeurRepository.findByEmail(email);
        } else if (nom != null && !nom.isEmpty()) {
            List<Demandeur> demandeurs = demandeurRepository.findByNomContainingIgnoreCase(nom);
            if (!demandeurs.isEmpty()) {
                demandeur = Optional.of(demandeurs.get(0));
            }
        }

        if (demandeur.isPresent()) {
            return Optional.of(mapToDemandeurSearchResultDto(demandeur.get()));
        }
        return Optional.empty();
    }

    /**
     * Map Demandeur entity to DTO for search results
     */
    private DemandeurSearchResultDto mapToDemandeurSearchResultDto(Demandeur demandeur) {
        DemandeurSearchResultDto dto = new DemandeurSearchResultDto();
        dto.setId(demandeur.getId());
        dto.setNom(demandeur.getNom());
        dto.setPrenom(demandeur.getPrenom());
        dto.setDateNaissance(demandeur.getDateNaissance());
        dto.setLieuNaissance(demandeur.getLieuNaissance());
        dto.setEmail(demandeur.getEmail());
        dto.setTelephone(demandeur.getTelephone());
        dto.setAdresse(demandeur.getAdresse());
        dto.setNationalite(demandeur.getNationalite().getLibelle());
        dto.setSituationFamiliale(demandeur.getSituationFamiliale().getLibelle());

        // Get the most recent passport
        List<Passeport> passeports = passeportRepository.findByDemandeur(demandeur);
        if (!passeports.isEmpty()) {
            Passeport dernier = passeports.get(passeports.size() - 1);
            dto.setNumeroPasseportActuel(dernier.getNumeroPasseport());
            dto.setDateExpirationPasseportActuel(dernier.getDateExpiration());
        }

        // Check if applicant has visa or resident card
        List<Visa> visas = visaRepository.findAll().stream()
                .filter(v -> v.getPasseport().getDemandeur().getId().equals(demandeur.getId()))
                .toList();
        List<CarteResident> cartes = carteResidentRepository.findAll().stream()
                .filter(c -> c.getDemande().getDemandeur().getId().equals(demandeur.getId()))
                .toList();

        dto.setAVisaOuCarteResident(!visas.isEmpty() || !cartes.isEmpty());

        return dto;
    }

    /**
     * Create a duplicate request for lost passport (visa transfer)
     */
    @Transactional
    public void createVissaTransferRequest(DuplicateRequestDto dto) {
        Demandeur demandeur = getDemandeur(dto);
        Passeport ancienPasseport = passeportRepository.findByNumeroPasseport(dto.getNumeroPasseport())
                .orElseThrow(() -> new IllegalArgumentException("Ancien passeport non trouvé"));

        // Create new passport
        Passeport nouveauPasseport = new Passeport();
        nouveauPasseport.setDemandeur(demandeur);
        nouveauPasseport.setNumeroPasseport(dto.getNouveauNumeroPasseport());
        nouveauPasseport.setDateDelivrance(dto.getDateDelivranceNouveauPasseport());
        nouveauPasseport.setDateExpiration(dto.getDateExpirationNouveauPasseport());
        nouveauPasseport.setPaysDelivrance(dto.getPaysDelivrance());
        Passeport savedNouveauPasseport = passeportRepository.save(nouveauPasseport);

        // Get the existing visa from the old passport
        List<Visa> visasAnciens = visaRepository.findAll().stream()
                .filter(v -> v.getPasseport().getId().equals(ancienPasseport.getId()))
                .toList();

        // Create TypeVisa and TypeDemande
        TypeVisa typeVisa = new TypeVisa();
        typeVisa.setLibelle("transfert_visa");
        TypeVisa savedTypeVisa = typeVisaRepository.save(typeVisa);

        TypeDemande typeDemande = new TypeDemande();
        typeDemande.setLibelle("transfert_visa_passeport_perdu");
        TypeDemande savedTypeDemande = typeDemandeRepository.save(typeDemande);

        // Create VisaTransformable
        VisaTransformable visaTransformable = new VisaTransformable();
        visaTransformable.setDemandeur(demandeur);
        visaTransformable.setPasseport(savedNouveauPasseport);
        visaTransformable.setNumeroReference(generateReferenceVisa(demandeur.getId()));
        VisaTransformable savedVisaTransformable = visaTransformableRepository.save(visaTransformable);

        // Create Demande with status "Approuvée" (status = 4 ou valeur correspondante)
        Demande demande = new Demande();
        demande.setVisaTransformable(savedVisaTransformable);
        demande.setDateDemande(LocalDate.now());
        demande.setIdStatut(3); // Status "Approuvée"
        demande.setDemandeur(demandeur);
        demande.setTypeVisa(savedTypeVisa);
        demande.setTypeDemande(savedTypeDemande);
        demande.setDateTraitement(LocalDate.now());
        Demande savedDemande = demandeRepository.save(demande);

        // Create new Visa for the new passport
        if (!visasAnciens.isEmpty()) {
            Visa visaAncien = visasAnciens.get(0);
            Visa nouvelleVisa = new Visa();
            nouvelleVisa.setDemande(savedDemande);
            nouvelleVisa.setReference(generateReferenceVisa(demandeur.getId()));
            nouvelleVisa.setDateDebut(LocalDate.now());
            nouvelleVisa.setDateFin(visaAncien.getDateFin()); // Keep same expiration date
            nouvelleVisa.setPasseport(savedNouveauPasseport);
            visaRepository.save(nouvelleVisa);
        }

        // Save submitted documents
        if (dto.getDocuments() != null && !dto.getDocuments().isEmpty()) {
            for (Long documentId : dto.getDocuments()) {
                DemandePieceJustificative demandePiece = new DemandePieceJustificative();
                demandePiece.setDemande(savedDemande);
                demandePiece.setPieceJustificative(pieceJustificativeRepository.findById(documentId).orElse(null));
                demandePiece.setSoumis(true);
                demandePiece.setDateSoumission(LocalDate.now());
                demandePieceJustificativeRepository.save(demandePiece);
            }
        }
    }

    /**
     * Create a duplicate request for lost resident card
     */
    @Transactional
    public void createCarteResidentDuplicateRequest(DuplicateRequestDto dto) {
        Demandeur demandeur = getDemandeur(dto);
        Passeport passeport = passeportRepository.findByNumeroPasseport(dto.getNumeroPasseport())
                .orElseThrow(() -> new IllegalArgumentException("Passeport non trouvé"));

        // Get existing resident card
        List<CarteResident> cartesExistantes = carteResidentRepository.findAll().stream()
                .filter(c -> c.getPasseport().getId().equals(passeport.getId()))
                .toList();

        // Create TypeVisa and TypeDemande
        TypeVisa typeVisa = new TypeVisa();
        typeVisa.setLibelle("carte_resident");
        TypeVisa savedTypeVisa = typeVisaRepository.save(typeVisa);

        TypeDemande typeDemande = new TypeDemande();
        typeDemande.setLibelle("duplicata_carte_resident");
        TypeDemande savedTypeDemande = typeDemandeRepository.save(typeDemande);

        // Create VisaTransformable
        VisaTransformable visaTransformable = new VisaTransformable();
        visaTransformable.setDemandeur(demandeur);
        visaTransformable.setPasseport(passeport);
        visaTransformable.setNumeroReference(generateReferenceVisa(demandeur.getId()));
        VisaTransformable savedVisaTransformable = visaTransformableRepository.save(visaTransformable);

        // Create Demande with status "Approuvée" (status = 3 ou valeur correspondante)
        Demande demande = new Demande();
        demande.setVisaTransformable(savedVisaTransformable);
        demande.setDateDemande(LocalDate.now());
        demande.setIdStatut(3); // Status "Approuvée"
        demande.setDemandeur(demandeur);
        demande.setTypeVisa(savedTypeVisa);
        demande.setTypeDemande(savedTypeDemande);
        demande.setDateTraitement(LocalDate.now());
        Demande savedDemande = demandeRepository.save(demande);

        // Create new resident card
        if (!cartesExistantes.isEmpty()) {
            CarteResident carteAncienne = cartesExistantes.get(0);
            CarteResident nouvelleCarteResident = new CarteResident();
            nouvelleCarteResident.setDemande(savedDemande);
            nouvelleCarteResident.setReference(generateReferenceVisa(demandeur.getId()));
            nouvelleCarteResident.setDateDebut(LocalDate.now());
            nouvelleCarteResident.setDateFin(carteAncienne.getDateFin()); // Keep same expiration date
            nouvelleCarteResident.setPasseport(passeport);
            carteResidentRepository.save(nouvelleCarteResident);
        }

        // Save submitted documents
        if (dto.getDocuments() != null && !dto.getDocuments().isEmpty()) {
            for (Long documentId : dto.getDocuments()) {
                DemandePieceJustificative demandePiece = new DemandePieceJustificative();
                demandePiece.setDemande(savedDemande);
                demandePiece.setPieceJustificative(pieceJustificativeRepository.findById(documentId).orElse(null));
                demandePiece.setSoumis(true);
                demandePiece.setDateSoumission(LocalDate.now());
                demandePieceJustificativeRepository.save(demandePiece);
            }
        }
    }

    /**
     * Get or create a Demandeur based on the DTO
     */
    private Demandeur getDemandeur(DuplicateRequestDto dto) {
        // If existing applicant ID is provided, use it
        if (dto.getIdDemandeurExistant() != null) {
            return demandeurRepository.findById(dto.getIdDemandeurExistant())
                    .orElseThrow(() -> new IllegalArgumentException("Demandeur non trouvé"));
        }

        // Otherwise, create a new applicant
        Demandeur demandeur = new Demandeur();
        demandeur.setNom(dto.getNom());
        demandeur.setPrenom(dto.getPrenom());
        demandeur.setDateNaissance(dto.getDateNaissance());
        demandeur.setLieuNaissance(dto.getLieuNaissance());
        demandeur.setEmail(dto.getEmail());
        demandeur.setTelephone(dto.getTelephone());
        demandeur.setAdresse(dto.getAdresse());

        // Create or find Nationalite
        Nationalite nationalite = new Nationalite();
        nationalite.setLibelle(dto.getNationalite());
        Nationalite savedNationalite = nationaliteRepository.save(nationalite);
        demandeur.setNationalite(savedNationalite);

        // Create or find Situation Familiale
        SituationFamiliale situation = new SituationFamiliale();
        situation.setLibelle(dto.getSituationFamiliale());
        SituationFamiliale savedSituation = situationFamilialeRepository.save(situation);
        demandeur.setSituationFamiliale(savedSituation);

        return demandeurRepository.save(demandeur);
    }

    private String generateReferenceVisa(Long demandeurId) {
        return "DUP-" + demandeurId + "-" + System.currentTimeMillis();
    }
}
