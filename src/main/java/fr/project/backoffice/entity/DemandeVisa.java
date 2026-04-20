package fr.project.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande_visa")
public class DemandeVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_demande")
    private Long idDemande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_personne", nullable = false)
    private Personne personne;

    @Column(name = "type_demande", nullable = false, length = 20)
    private String typeDemande;

    @Column(name = "motif", length = 50)
    private String motif;

    @Column(name = "date_demande", nullable = false)
    private LocalDateTime dateDemande;

    @Column(name = "statut_demande", length = 30)
    private String statutDemande;

    // Getters and setters
    public Long getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(Long idDemande) {
        this.idDemande = idDemande;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getStatutDemande() {
        return statutDemande;
    }

    public void setStatutDemande(String statutDemande) {
        this.statutDemande = statutDemande;
    }
}
