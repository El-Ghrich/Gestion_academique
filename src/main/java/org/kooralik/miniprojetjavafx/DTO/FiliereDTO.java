package org.kooralik.miniprojetjavafx.DTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class FiliereDTO {
    // On ne garde que les champs utiles pour l'affichage (pas de listes ici)
    private UUID id;
    private String code;
    private String nom;
    private String description;

    // Constructeur vide
    public FiliereDTO() {
    }

    // Constructeur complet
    public FiliereDTO(UUID id, String code, String nom, String description) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.description = description;
    }

    // --- La Méthode de Mapping (Factory) ---
    // Elle est 'static' pour qu'on puisse l'appeler sans instancier la classe avant.
    public static FiliereDTO fromResultSet(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String code = rs.getString("code");
        String nom = rs.getString("nom");
        String description = rs.getString("description");

        return new FiliereDTO(id, code, nom, description);
    }

    // --- Getters et Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Pour l'affichage facile dans les ComboBox JavaFX
    @Override
    public String toString() {
        return code + " - " + nom;
    }
}