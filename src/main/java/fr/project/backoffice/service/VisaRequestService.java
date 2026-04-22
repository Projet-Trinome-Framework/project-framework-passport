package fr.project.backoffice.service;

import fr.project.backoffice.dto.CreateVisaRequestDto;
import fr.project.backoffice.entity.Demande;
import fr.project.backoffice.entity.Demandeur;
import fr.project.backoffice.entity.Passeport;
import fr.project.backoffice.entity.SituationFamiliale;
import fr.project.backoffice.entity.Visa;
import fr.project.backoffice.entity.VisaTransformable;
import fr.project.backoffice.repository.DemandeRepository;
import fr.project.backoffice.repository.DemandeurRepository;
import fr.project.backoffice.repository.PasseportRepository;
import fr.project.backoffice.repository.SituationFamilialeRepository;
import fr.project.backoffice.repository.VisaRepository;
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

    public VisaRequestService(DemandeurRepository demandeurRepository,
                             DemandeRepository demandeRepository,
                             VisaRepository visaRepository,
                             PasseportRepository passeportRepository,
                             SituationFamilialeRepository situationFamilialeRepository) {
        this.demandeurRepository = demandeurRepository;
        this.demandeRepository = demandeRepository;
        this.visaRepository = visaRepository;
        this.passeportRepository = passeportRepository;
        this.situationFamilialeRepository = situationFamilialeRepository;
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

        // Create VisaTransformable
        VisaTransformable visaTransformable = new VisaTransformable();
        visaTransformable.setDemandeur(savedDemandeur);
        visaTransformable.setPasseport(savedPasseport);
        visaTransformable.setNumeroReference(generateReferenceVisa(savedDemandeur.getId()));

        // Create Demande
        Demande demande = new Demande();
        demande.setVisaTransformable(visaTransformable);
        demande.setDateDemande(LocalDate.now());
        demande.setIdStatut(1);
        demande.setDemandeur(savedDemandeur);
        Demande savedDemande = demandeRepository.save(demande);

        // Create Visa
        Visa visa = new Visa();
        visa.setDemande(savedDemande);
        visa.setReference(generateReferenceVisa(savedDemandeur.getId()));
        visa.setDateDebut(LocalDate.now());
        visa.setDateFin(dto.getDateExpiration());
        visa.setPasseport(savedPasseport);
        visaRepository.save(visa);
    }

    private String generateReferenceVisa(Long demandeurId) {
        return "VISA-" + demandeurId + "-" + System.currentTimeMillis();
    }
}
