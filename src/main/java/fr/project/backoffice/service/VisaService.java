package fr.project.backoffice.service;

import fr.project.backoffice.dto.VisaInfoDto;
import fr.project.backoffice.entity.Visa;
import fr.project.backoffice.repository.VisaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisaService {

    private final VisaRepository visaRepository;

    public VisaService(VisaRepository visaRepository) {
        this.visaRepository = visaRepository;
    }

    public List<VisaInfoDto> getAllVisaInfo() {
        return visaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private VisaInfoDto toDto(Visa visa) {
        VisaInfoDto dto = new VisaInfoDto();
        dto.setIdVisa(visa.getIdVisa());
        dto.setReferenceVisa(visa.getReferenceVisa());
        dto.setTypeVisa(visa.getTypeVisa());
        dto.setPassportNumber(visa.getPersonne().getNumeroPasseport());
        dto.setRequesterName(visa.getPersonne().getPrenom() + " " + visa.getPersonne().getNom());
        dto.setTypeDemande(visa.getDemandeVisa().getTypeDemande());
        dto.setMotifDemande(visa.getDemandeVisa().getMotif());
        dto.setStatutDemande(visa.getDemandeVisa().getStatutDemande());
        dto.setDateEmission(visa.getDateEmission());
        dto.setDateExpiration(visa.getDateExpiration());
        dto.setTransformable(Boolean.TRUE.equals(visa.getEstTransformable()));

        long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), visa.getDateExpiration());
        dto.setDaysToExpire(daysToExpire);

        return dto;
    }
}
