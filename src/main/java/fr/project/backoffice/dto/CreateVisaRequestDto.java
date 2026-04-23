package fr.project.backoffice.dto;

import java.time.LocalDate;
import java.util.List;

public class CreateVisaRequestDto {

    // Personne fields
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String nationalite;
    private String numeroPasseport;
    private String email;
    private String telephone;

    // Demande Visa fields
    private String typeDemande;
    private String motif;

    // Visa fields
    private LocalDate dateExpiration;
    private Boolean estTransformable;
    private String referenceVisa;

    // Documents fields
    private List<Long> documents;

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

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

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

    public List<Long> getDocuments() { return documents; }
    public void setDocuments(List<Long> documents) { this.documents = documents; }
}
