package org.kooralik.miniprojetjavafx.dao;

import org.kooralik.miniprojetjavafx.model.Etudiant;
import org.kooralik.miniprojetjavafx.model.Status;
import org.kooralik.miniprojetjavafx.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EtudiantDAO implements BaseDAO<Etudiant> {

    private final Connection connection = DBConnection.getConnection();

    @Override
    public void save(Etudiant etudiant) {
        String sql = "INSERT INTO eleve (id, matricule, nom, prenom, email, filiere_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // 1. UUID (Généré dans le constructeur de l'objet Etudiant)
            pstmt.setObject(1, etudiant.getId());

            // 2. Champs String classiques
            pstmt.setString(2, etudiant.getMatricule());
            pstmt.setString(3, etudiant.getNom());
            pstmt.setString(4, etudiant.getPrenom());
            pstmt.setString(5, etudiant.getEmail());

            // 3. Clé étrangère (UUID de la filière)
            pstmt.setObject(6, etudiant.getFiliere_id());

            // 4. Enum -> String (Pour la base de données)
            // On enregistre "ACTIF" ou "SUSPENDU"
            pstmt.setString(7, etudiant.getStatus().name());

            pstmt.executeUpdate();
            System.out.println("Étudiant sauvegardé avec succès !");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'inscription de l'étudiant : " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Etudiant etudiant) {
        String sql = "UPDATE eleve SET matricule = ?, nom = ?, prenom = ?, email = ?, filiere_id = ?, status = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getMatricule());
            pstmt.setString(2, etudiant.getNom());
            pstmt.setString(3, etudiant.getPrenom());
            pstmt.setString(4, etudiant.getEmail());
            pstmt.setObject(5, etudiant.getFiliere_id());
            pstmt.setString(6, etudiant.getStatus().name());

            // WHERE clause
            pstmt.setObject(7, etudiant.getId());

            pstmt.executeUpdate();
            System.out.println("Étudiant mis à jour !");

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'étudiant", e);
        }
    }

    @Override
    public void delete(UUID id) {
        // Note : Si ON DELETE CASCADE est configuré dans ta BDD pour 'eleve_cours' et 'dossier',
        // cela supprimera tout. Sinon, ça plantera ici (ce qui est une sécurité).
        String sql = "DELETE FROM eleve WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
            System.out.println("Étudiant supprimé.");
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de supprimer l'étudiant (Probablement lié à des cours ou un dossier)", e);
        }
    }

    @Override
    public Optional<Etudiant> findById(UUID id) {
        String sql = "SELECT * FROM eleve WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEtudiant(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de Trouver l'étudiant (Probablement lié à des cours ou un dossier)", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Etudiant> findAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM eleve";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                etudiants.add(mapResultSetToEtudiant(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des étudiants", e);
        }
        return etudiants;
    }

    // --- Méthode Bonus (Utile pour l'interface plus tard) ---
    // Pour afficher "Les étudiants de la filière Informatique"
    public List<Etudiant> findByFiliere(UUID filiereId) {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM eleve WHERE filiere_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, filiereId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    etudiants.add(mapResultSetToEtudiant(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Un erreur s'est produit lors de trouver les etudiant de la filiere", e);
        }
        return etudiants;
    }

    // --- Helper pour transformer une ligne SQL en Objet Java ---
    private Etudiant mapResultSetToEtudiant(ResultSet rs) throws SQLException {
        Etudiant etudiant = new Etudiant();

        // Mapping des colonnes
        etudiant.setId((UUID) rs.getObject("id"));
        etudiant.setMatricule(rs.getString("matricule"));
        etudiant.setNom(rs.getString("nom"));
        etudiant.setPrenom(rs.getString("prenom"));
        etudiant.setEmail(rs.getString("email"));
        etudiant.setFiliere_id((UUID) rs.getObject("filiere_id"));

        // Gestion de l'Enum (String BDD -> Enum Java)
        String statusString = rs.getString("status");
        if (statusString != null) {
            etudiant.setStatus(Status.valueOf(statusString));
        } else {
            // Par défaut si null (sécurité)
            etudiant.setStatus(Status.ACTIF);
        }

        return etudiant;
    }
}