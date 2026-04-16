package fr.project.backoffice.dto;

import java.time.LocalDate;

public class VisaInfoDto {

    private Long idVisa;
    private String referenceVisa;
    private String typeVisa;
    private String passportNumber;
    private String requesterName;
    private String typeDemande;
    private String motifDemande;
    private String statutDemande;
    private LocalDate dateEmission;
    private LocalDate dateExpiration;
    private Boolean transformable;
    private Long daysToExpire;

    public Long getIdVisa() {
        return idVisa;
    }

    public void setIdVisa(Long idVisa) {
        this.idVisa = idVisa;
    }

    public String getReferenceVisa() {
        return referenceVisa;
    }

    public void setReferenceVisa(String referenceVisa) {
        this.referenceVisa = referenceVisa;
    }

    public String getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(String typeVisa) {
        this.typeVisa = typeVisa;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    public String getMotifDemande() {
        return motifDemande;
    }

    public void setMotifDemande(String motifDemande) {
        this.motifDemande = motifDemande;
    }

    public String getStatutDemande() {
        return statutDemande;
    }

    public void setStatutDemande(String statutDemande) {
        this.statutDemande = statutDemande;
    }

    public LocalDate getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Boolean getTransformable() {
        return transformable;
    }

    public void setTransformable(Boolean transformable) {
        this.transformable = transformable;
    }

    public Long getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(Long daysToExpire) {
        this.daysToExpire = daysToExpire;
    }
}
