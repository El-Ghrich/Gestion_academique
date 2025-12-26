package org.kooralik.miniprojetjavafx.dao;

import org.kooralik.miniprojetjavafx.model.DossierAdministratif;
import org.kooralik.miniprojetjavafx.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DossierAdministratifDAO {
    private final Connection connection = DBConnection.getConnection();

    public void save(DossierAdministratif dossier) {
        String sql = "INSERT INTO DossierAdministratif (id, numero_inscription, date_creation, eleve_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, dossier.getId());
            pstmt.setString(2, dossier.getNumeroInscription());
            // Conversion LocalDate -> java.sql.Date
            pstmt.setDate(3, Date.valueOf(dossier.getDateCreation()));
            pstmt.setObject(4, dossier.getEleveId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du dossier", e);
        }
    }

    public List<DossierAdministratif> findAll() {
        List<DossierAdministratif> listeDossiers = new ArrayList<>();
        String sql = "SELECT * FROM DossierAdministratif";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                DossierAdministratif d = new DossierAdministratif();

                // Mapping des colonnes BDD -> Objet Java
                d.setId((UUID) rs.getObject("id"));
                d.setNumeroInscription(rs.getString("numero_inscription"));

                // Conversion importante : SQL Date -> Java LocalDate
                if (rs.getDate("date_creation") != null) {
                    d.setDateCreation(rs.getDate("date_creation").toLocalDate());
                }

                d.setEleveId((UUID) rs.getObject("eleve_id"));

                listeDossiers.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du chargement des dossiers", e);
        }

        return listeDossiers;
    }
    public void delete(UUID id) {
        String sql = "DELETE FROM DossierAdministratif WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du dossier", e);
        }
    }

    public void update(DossierAdministratif dossier) {
        String sql = "UPDATE DossierAdministratif SET numero_inscription = ?, date_creation = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, dossier.getNumeroInscription());
            pstmt.setDate(2, Date.valueOf(dossier.getDateCreation()));
            pstmt.setObject(3, dossier.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du dossier", e);
        }
    }

    public Optional<DossierAdministratif> findByEleveId(UUID eleveId) {
        String sql = "SELECT * FROM DossierAdministratif WHERE eleve_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, eleveId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DossierAdministratif d = new DossierAdministratif();
                    d.setId((UUID) rs.getObject("id"));
                    d.setNumeroInscription(rs.getString("numero_inscription"));
                    // Conversion java.sql.Date -> LocalDate
                    d.setDateCreation(rs.getDate("date_creation").toLocalDate());
                    d.setEleveId((UUID) rs.getObject("eleve_id"));
                    return Optional.of(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void deleteByEleveId(UUID eleveId) {
        String sql = "DELETE FROM DossierAdministratif WHERE eleve_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, eleveId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}