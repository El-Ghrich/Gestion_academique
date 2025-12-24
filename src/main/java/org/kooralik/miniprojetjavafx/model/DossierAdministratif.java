package org.kooralik.miniprojetjavafx.model;

import java.time.LocalDate;
import java.util.UUID;

public class DossierAdministratif {
    private UUID id;
    private String numeroInscription;
    private LocalDate dateCreation;
    private UUID eleveId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroInscription() {
        return numeroInscription;
    }

    public void setNumeroInscription(String numeroInscription) {
        this.numeroInscription = numeroInscription;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public UUID getEleveId() {
        return eleveId;
    }

    public void setEleveId(UUID eleveId) {
        this.eleveId = eleveId;
    }

    public DossierAdministratif() {}
    public DossierAdministratif(String numeroInscription, LocalDate dateCreation, UUID eleveId) {
        this.id = UUID.randomUUID();
        this.numeroInscription = numeroInscription;
        this.dateCreation = dateCreation;
        this.eleveId = eleveId;
    }

    public DossierAdministratif(UUID id, String numeroInscription, LocalDate dateCreation, UUID eleveId) {
        this.id = id;
        this.numeroInscription = numeroInscription;
        this.dateCreation = dateCreation;
        this.eleveId = eleveId;
    }

}
