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
        dto.setIdVisa(visa.getId());
        dto.setDemandeId(visa.getDemande().getId());
        dto.setReferenceVisa(visa.getReference());
        dto.setPassportNumber(visa.getPasseport().getNumeroPasseport());
        dto.setRequesterName(visa.getPasseport().getDemandeur().getPrenom() + " " + visa.getPasseport().getDemandeur().getNom());
        dto.setDateEmission(visa.getDateDebut());
        dto.setDateExpiration(visa.getDateFin());
        dto.setTransformable(false);
        dto.setStatutDemande("en cours");
        dto.setTypeDemande("");
        dto.setTypeVisa("");
        dto.setMotifDemande("");

        long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), visa.getDateFin());
        dto.setDaysToExpire(daysToExpire);

        return dto;
    }
}
