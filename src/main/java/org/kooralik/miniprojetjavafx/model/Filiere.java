package org.kooralik.miniprojetjavafx.model;

import java.util.UUID;

public class Filiere {
    private UUID id;
    private String code;
    private String nom;
    private String description;

    public Filiere() {
    }

    public Filiere(String code, String nom, String description) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.nom = nom;
        this.description = description;
    }

    public Filiere(UUID id, String code, String nom, String description) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return this.code + "," + this.nom + "," + this.description;
    }
}
