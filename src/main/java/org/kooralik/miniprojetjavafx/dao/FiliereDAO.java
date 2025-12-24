package org.kooralik.miniprojetjavafx.dao;

import org.kooralik.miniprojetjavafx.model.Etudiant;
import org.kooralik.miniprojetjavafx.model.Filiere;
import org.kooralik.miniprojetjavafx.model.Status;
import org.kooralik.miniprojetjavafx.util.DBConnection;
import org.kooralik.miniprojetjavafx.DTO.FiliereDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FiliereDAO implements BaseDAO<Filiere> {
    private final Connection connection = DBConnection.getConnection();

    @Override
    public void save(Filiere filiere) {
        String sql = "INSERT INTO filiere (id, code, nom, description) VALUES (?, ?, ?, ?)";
        try
        (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, filiere.getId()); // PostgreSQL accepte UUID via setObject
            pstmt.setString(2, filiere.getCode());
            pstmt.setString(3, filiere.getNom());
            pstmt.setString(4, filiere.getDescription());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la filière", e);
        }
    }

    @Override
    public void update(Filiere filiere) {
        String sql = "UPDATE filiere SET nom = ?, code = ? ,description = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, filiere.getNom());
            pstmt.setString(2, filiere.getCode());
            pstmt.setString(3, filiere.getDescription());
            pstmt.setObject(4, filiere.getId()); // PostgreSQL accepte UUID via setObject
            pstmt.executeUpdate();
        }catch(Exception e){
            throw new RuntimeException("Erreur lors de la mise a jour de la filière", e);
        }
    }

    @Override
    public void delete(UUID id) {
        // 1. Vérification : Est-ce qu'il y a des élèves ?
        String checkSql = "SELECT COUNT(*) FROM eleve WHERE filiere_id = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setObject(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new RuntimeException("Suppression impossible : Cette filière contient des élèves inscrits.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification des élèves", e);
        }

        String deleteSql = "DELETE FROM filiere WHERE id = ?";
        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
            deleteStmt.setObject(1, id);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la filière", e);
        }
    }

    @Override
    public Optional<Filiere> findById(UUID id) {
        String sql = "SELECT * FROM filiere WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToFiliere(rs));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Filiere> findAll() {
        List<Filiere> filieres = new ArrayList<>();
        String sql = "SELECT * FROM filiere";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                filieres.add(mapResultSetToFiliere(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des filières", e);
        }
        return filieres;
    }
    private Filiere mapResultSetToFiliere(ResultSet rs) throws SQLException {
        return new Filiere(
                (UUID) rs.getObject("id"),
                rs.getString("code"),
                rs.getString("nom"),
                rs.getString("description")
        );
    }
}
