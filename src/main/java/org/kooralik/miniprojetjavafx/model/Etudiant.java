package org.kooralik.miniprojetjavafx.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Etudiant {
    private UUID id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private UUID filiere_id;
    private Status status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getFiliere_id() {
        return filiere_id;
    }

    public void setFiliere_id(UUID filiere_id) {
        this.filiere_id = filiere_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Etudiant(UUID id, String matricule, String nom, String prenom, String email, UUID filiere_id, Status status) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.filiere_id = filiere_id;
        this.status = status;
    }

    public Etudiant(String matricule, String nom, String prenom, String email, UUID filiere_id, Status status) {
        this.id = UUID.randomUUID();
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.filiere_id = filiere_id;
        this.status = status;
    }
    public Etudiant() {
    }

    @Override
    public String toString() {
        return this.nom + " " + this.prenom + " " + this.email + " ";
    }
}
