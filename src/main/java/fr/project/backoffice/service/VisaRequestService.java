package fr.project.backoffice.service;

import fr.project.backoffice.dto.CreateVisaRequestDto;
import fr.project.backoffice.entity.DemandeVisa;
import fr.project.backoffice.entity.Passeport;
import fr.project.backoffice.entity.Personne;
import fr.project.backoffice.entity.SituationFamiliale;
import fr.project.backoffice.entity.Visa;
import fr.project.backoffice.repository.DemandeVisaRepository;
import fr.project.backoffice.repository.PasseportRepository;
import fr.project.backoffice.repository.PersonneRepository;
import fr.project.backoffice.repository.SituationFamilialeRepository;
import fr.project.backoffice.repository.VisaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class VisaRequestService {

    private final PersonneRepository personneRepository;
    private final DemandeVisaRepository demandeVisaRepository;
    private final VisaRepository visaRepository;
    private final PasseportRepository passeportRepository;
    private final SituationFamilialeRepository situationFamilialeRepository;

    public VisaRequestService(PersonneRepository personneRepository,
                             DemandeVisaRepository demandeVisaRepository,
                             VisaRepository visaRepository,
                             PasseportRepository passeportRepository,
                             SituationFamilialeRepository situationFamilialeRepository) {
        this.personneRepository = personneRepository;
        this.demandeVisaRepository = demandeVisaRepository;
        this.visaRepository = visaRepository;
        this.passeportRepository = passeportRepository;
        this.situationFamilialeRepository = situationFamilialeRepository;
    }

    @Transactional
    public void createVisaRequest(CreateVisaRequestDto dto) {
        // Create Personne
        Personne personne = new Personne();
        personne.setNom(dto.getNom());
        personne.setPrenom(dto.getPrenom());
        personne.setDateNaissance(dto.getDateNaissance());
        personne.setNationalite(dto.getNationalite());
        personne.setEmail(dto.getEmail());
        personne.setTelephone(dto.getTelephone());
        Personne savedPersonne = personneRepository.save(personne);

        // Create Situation Familiale
        SituationFamiliale situation = new SituationFamiliale();
        situation.setPersonne(savedPersonne);
        situation.setStatutFamilial(dto.getSituationFamiliale());
        situation.setAdresse(dto.getAdresse());
        situation.setNbEnfants(dto.getNbEnfants() != null ? dto.getNbEnfants() : 0);
        situationFamilialeRepository.save(situation);

        // Create Passeport
        Passeport passeport = new Passeport();
        passeport.setPersonne(savedPersonne);
        passeport.setNumeroPasseport(dto.getNumeroPasseport());
        passeport.setDateDelivrance(dto.getDateDelivrance());
        passeport.setDateExpiration(dto.getDateExpirationPasseport());
        passeport.setPaysDelivrance(dto.getPaysDelivrance());
        passeport.setTypePasseport(dto.getTypePasseport());
        passeportRepository.save(passeport);

        // Create DemandeVisa
        DemandeVisa demandeVisa = new DemandeVisa();
        demandeVisa.setPersonne(savedPersonne);
        demandeVisa.setTypeDemande(dto.getTypeDemande());
        demandeVisa.setMotif(dto.getMotif());
        demandeVisa.setDateDemande(LocalDateTime.now());
        demandeVisa.setStatutDemande("dossier crée");
        DemandeVisa savedDemande = demandeVisaRepository.save(demandeVisa);

        // Create Visa
        Visa visa = new Visa();
        visa.setPersonne(savedPersonne);
        visa.setDemandeVisa(savedDemande);
        visa.setTypeVisa(dto.getTypeDemande().equals("transformable") ? "travailleur" : "etudiant");
        visa.setDateEmission(LocalDate.now());
        visa.setDateExpiration(dto.getDateExpiration());
        visa.setEstTransformable(dto.getEstTransformable());
        visa.setReferenceVisa(dto.getReferenceVisa() != null ? dto.getReferenceVisa() : generateReferenceVisa(savedPersonne.getIdPersonne()));
        visaRepository.save(visa);
    }

    private String generateReferenceVisa(Long personneId) {
        return "VISA-" + personneId + "-" + System.currentTimeMillis();
    }
}
