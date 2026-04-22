package fr.project.backoffice.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "statut_passeport")
public class StatutPasseport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passeport", nullable = false)
    private Passeport passeport;

    @Column(name = "statut", nullable = false)
    private Integer statut;

    @Column(name = "date_changement_statut")
    private LocalDate dateChangementStatut;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Passeport getPasseport() {
        return passeport;
    }

    public void setPasseport(Passeport passeport) {
        this.passeport = passeport;
    }

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }

    public LocalDate getDateChangementStatut() {
        return dateChangementStatut;
    }

    public void setDateChangementStatut(LocalDate dateChangementStatut) {
        this.dateChangementStatut = dateChangementStatut;
    }
}
