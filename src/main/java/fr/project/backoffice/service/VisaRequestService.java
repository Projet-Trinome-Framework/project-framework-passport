package fr.project.backoffice.service;

import fr.project.backoffice.dto.CreateVisaRequestDto;
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
import fr.project.backoffice.repository.DemandeRepository;
import fr.project.backoffice.repository.DemandeurRepository;
import fr.project.backoffice.repository.DemandePieceJustificativeRepository;
import fr.project.backoffice.repository.NationaliteRepository;
import fr.project.backoffice.repository.PasseportRepository;
import fr.project.backoffice.repository.SituationFamilialeRepository;
import fr.project.backoffice.repository.TypeDemandeRepository;
import fr.project.backoffice.repository.TypeVisaRepository;
import fr.project.backoffice.repository.VisaRepository;
import fr.project.backoffice.repository.VisaTransformableRepository;
import fr.project.backoffice.repository.PieceJustificativeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class VisaRequestService {

    private final DemandeurRepository demandeurRepository;
    private final DemandeRepository demandeRepository;
    private final VisaRepository visaRepository;
    private final PasseportRepository passeportRepository;
    private final SituationFamilialeRepository situationFamilialeRepository;
    private final NationaliteRepository nationaliteRepository;
    private final TypeVisaRepository typeVisaRepository;
    private final TypeDemandeRepository typeDemandeRepository;
    private final VisaTransformableRepository visaTransformableRepository;
    private final DemandePieceJustificativeRepository demandePieceJustificativeRepository;
    private final PieceJustificativeRepository pieceJustificativeRepository;

    public VisaRequestService(DemandeurRepository demandeurRepository,
                             DemandeRepository demandeRepository,
                             VisaRepository visaRepository,
                             PasseportRepository passeportRepository,
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
        this.situationFamilialeRepository = situationFamilialeRepository;
        this.nationaliteRepository = nationaliteRepository;
        this.typeVisaRepository = typeVisaRepository;
        this.typeDemandeRepository = typeDemandeRepository;
        this.visaTransformableRepository = visaTransformableRepository;
        this.demandePieceJustificativeRepository = demandePieceJustificativeRepository;
        this.pieceJustificativeRepository = pieceJustificativeRepository;
    }

    @Transactional
    public void createVisaRequest(CreateVisaRequestDto dto) {
        // Create Demandeur
        Demandeur demandeur = new Demandeur();
        demandeur.setNom(dto.getNom());
        demandeur.setPrenom(dto.getPrenom());
        demandeur.setDateNaissance(dto.getDateNaissance());
        demandeur.setLieuNaissance(dto.getLieuNaissance());
        demandeur.setEmail(dto.getEmail());
        demandeur.setTelephone(dto.getTelephone());
        demandeur.setAdresse(dto.getAdresse());
        
        // Create Nationalite
        Nationalite nationalite = new Nationalite();
        nationalite.setLibelle(dto.getNationalite());
        Nationalite savedNationalite = nationaliteRepository.save(nationalite);
        demandeur.setNationalite(savedNationalite);
        
        // Create Situation Familiale
        SituationFamiliale situation = new SituationFamiliale();
        situation.setLibelle(dto.getSituationFamiliale());
        SituationFamiliale savedSituation = situationFamilialeRepository.save(situation);
        demandeur.setSituationFamiliale(savedSituation);
        
        Demandeur savedDemandeur = demandeurRepository.save(demandeur);

        // Create Passeport
        Passeport passeport = new Passeport();
        passeport.setDemandeur(savedDemandeur);
        passeport.setNumeroPasseport(dto.getNumeroPasseport());
        passeport.setDateDelivrance(dto.getDateDelivrance());
        passeport.setDateExpiration(dto.getDateExpirationPasseport());
        passeport.setPaysDelivrance(dto.getPaysDelivrance());
        Passeport savedPasseport = passeportRepository.save(passeport);

        // Create TypeVisa
        TypeVisa typeVisa = new TypeVisa();
        typeVisa.setLibelle(dto.getMotif());
        TypeVisa savedTypeVisa = typeVisaRepository.save(typeVisa);

        // Create TypeDemande
        TypeDemande typeDemande = new TypeDemande();
        typeDemande.setLibelle(dto.getTypeDemande());
        TypeDemande savedTypeDemande = typeDemandeRepository.save(typeDemande);

        // Create VisaTransformable
        VisaTransformable visaTransformable = new VisaTransformable();
        visaTransformable.setDemandeur(savedDemandeur);
        visaTransformable.setPasseport(savedPasseport);
        visaTransformable.setNumeroReference(generateReferenceVisa(savedDemandeur.getId()));
        VisaTransformable savedVisaTransformable = visaTransformableRepository.save(visaTransformable);

        // Create Demande
        Demande demande = new Demande();
        demande.setVisaTransformable(savedVisaTransformable);
        demande.setDateDemande(LocalDate.now());
        demande.setIdStatut(1);
        demande.setDemandeur(savedDemandeur);
        demande.setTypeVisa(savedTypeVisa);
        demande.setTypeDemande(savedTypeDemande);
        Demande savedDemande = demandeRepository.save(demande);

        // Create Visa
        Visa visa = new Visa();
        visa.setDemande(savedDemande);
        visa.setReference(generateReferenceVisa(savedDemandeur.getId()));
        visa.setDateDebut(LocalDate.now());
        visa.setDateFin(dto.getDateExpiration());
        visa.setPasseport(savedPasseport);
        visaRepository.save(visa);

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

    private String generateReferenceVisa(Long demandeurId) {
        return "VISA-" + demandeurId + "-" + System.currentTimeMillis();
    }
}
