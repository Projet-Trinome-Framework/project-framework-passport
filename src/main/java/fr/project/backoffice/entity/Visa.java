package fr.project.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visa")
public class Visa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_visa")
    private Long idVisa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_personne", nullable = false)
    private Personne personne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_demande", nullable = false)
    private DemandeVisa demandeVisa;

    @Column(name = "type_visa", nullable = false, length = 20)
    private String typeVisa;

    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;

    @Column(name = "date_expiration", nullable = false)
    private LocalDate dateExpiration;

    @Column(name = "est_transformable")
    private Boolean estTransformable;

    @Column(name = "reference_visa", nullable = false, unique = true, length = 100)
    private String referenceVisa;

    // Getters and setters
    public Long getIdVisa() {
        return idVisa;
    }

    public void setIdVisa(Long idVisa) {
        this.idVisa = idVisa;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public DemandeVisa getDemandeVisa() {
        return demandeVisa;
    }

    public void setDemandeVisa(DemandeVisa demandeVisa) {
        this.demandeVisa = demandeVisa;
    }

    public String getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(String typeVisa) {
        this.typeVisa = typeVisa;
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

    public Boolean getEstTransformable() {
        return estTransformable;
    }

    public void setEstTransformable(Boolean estTransformable) {
        this.estTransformable = estTransformable;
    }

    public String getReferenceVisa() {
        return referenceVisa;
    }

    public void setReferenceVisa(String referenceVisa) {
        this.referenceVisa = referenceVisa;
    }
}
