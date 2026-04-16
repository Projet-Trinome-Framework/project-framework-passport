package fr.project.backoffice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "situation_familiale")
public class SituationFamiliale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_situation_familiale")
    private Long idSituationFamiliale;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_personne", nullable = false, unique = true)
    private Personne personne;

    @Column(name = "statut_familial", nullable = false, length = 50)
    private String statutFamilial;

    @Column(name = "adresse", length = 255)
    private String adresse;

    @Column(name = "nb_enfants")
    private Integer nbEnfants;

    public Long getIdSituationFamiliale() {
        return idSituationFamiliale;
    }

    public void setIdSituationFamiliale(Long idSituationFamiliale) {
        this.idSituationFamiliale = idSituationFamiliale;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public String getStatutFamilial() {
        return statutFamilial;
    }

    public void setStatutFamilial(String statutFamilial) {
        this.statutFamilial = statutFamilial;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getNbEnfants() {
        return nbEnfants;
    }

    public void setNbEnfants(Integer nbEnfants) {
        this.nbEnfants = nbEnfants;
    }
}
