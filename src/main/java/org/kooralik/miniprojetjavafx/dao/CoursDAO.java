package org.kooralik.miniprojetjavafx.dao;

import org.kooralik.miniprojetjavafx.model.Cours;
import org.kooralik.miniprojetjavafx.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CoursDAO implements BaseDAO<Cours> {
    private final Connection connection = DBConnection.getConnection();

    @Override
    public void save(Cours cours) {
        String sql = "INSERT INTO cours (id, code, intitule) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, cours.getId());
            pstmt.setString(2, cours.getCode());
            pstmt.setString(3, cours.getIntitule());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du cours", e);
        }
    }

    @Override
    public void update(Cours cours) {
        String sql = "UPDATE cours SET code = ?, intitule = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, cours.getCode());
            pstmt.setString(2, cours.getIntitule());
            pstmt.setObject(3, cours.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du cours", e);
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM cours WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de supprimer le cours (lié à des inscriptions)", e);
        }
    }

    @Override
    public Optional<Cours> findById(UUID id) {
        String sql = "SELECT * FROM cours WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapResultSetToCours(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Echec de la recherche du cours");
        }
        return Optional.empty();
    }

    @Override
    public List<Cours> findAll() {
        List<Cours> list = new ArrayList<>();
        String sql = "SELECT * FROM cours";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToCours(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Echec de la recherche des cours");
        }
        return list;
    }

    private Cours mapResultSetToCours(ResultSet rs) throws SQLException {
        return new Cours(
                (UUID) rs.getObject("id"),
                rs.getString("code"),
                rs.getString("intitule")
        );
    }
}