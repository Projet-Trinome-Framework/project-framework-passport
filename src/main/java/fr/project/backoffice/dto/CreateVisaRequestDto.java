package fr.project.backoffice.dto;

import java.time.LocalDate;

public class CreateVisaRequestDto {

    // Personne fields
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String nationalite;
    private String email;
    private String telephone;
    private String situationFamiliale;
    private String adresse;
    private Integer nbEnfants;
    private String numeroPasseport;
    private LocalDate dateDelivrance;
    private LocalDate dateExpirationPasseport;
    private String paysDelivrance;
    private String typePasseport;

    // Demande Visa fields
    private String typeDemande;
    private String motif;

    // Visa fields
    private LocalDate dateExpiration;
    private Boolean estTransformable;
    private String referenceVisa;

    // Getters and Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }

    public String getNumeroPasseport() { return numeroPasseport; }
    public void setNumeroPasseport(String numeroPasseport) { this.numeroPasseport = numeroPasseport; }

    public LocalDate getDateDelivrance() { return dateDelivrance; }
    public void setDateDelivrance(LocalDate dateDelivrance) { this.dateDelivrance = dateDelivrance; }

    public LocalDate getDateExpirationPasseport() { return dateExpirationPasseport; }
    public void setDateExpirationPasseport(LocalDate dateExpirationPasseport) { this.dateExpirationPasseport = dateExpirationPasseport; }

    public String getPaysDelivrance() { return paysDelivrance; }
    public void setPaysDelivrance(String paysDelivrance) { this.paysDelivrance = paysDelivrance; }

    public String getTypePasseport() { return typePasseport; }
    public void setTypePasseport(String typePasseport) { this.typePasseport = typePasseport; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getSituationFamiliale() { return situationFamiliale; }
    public void setSituationFamiliale(String situationFamiliale) { this.situationFamiliale = situationFamiliale; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Integer getNbEnfants() { return nbEnfants; }
    public void setNbEnfants(Integer nbEnfants) { this.nbEnfants = nbEnfants; }

    public String getTypeDemande() { return typeDemande; }
    public void setTypeDemande(String typeDemande) { this.typeDemande = typeDemande; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }

    public Boolean getEstTransformable() { return estTransformable; }
    public void setEstTransformable(Boolean estTransformable) { this.estTransformable = estTransformable; }

    public String getReferenceVisa() { return referenceVisa; }
    public void setReferenceVisa(String referenceVisa) { this.referenceVisa = referenceVisa; }
}
