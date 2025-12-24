package org.kooralik.miniprojetjavafx.dao;

import org.kooralik.miniprojetjavafx.DTO.FiliereDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseDAO<T> {
    void save(T t);                // T sera remplacé par Filiere ou Etudiant
    void update(T t);
    void delete(UUID id);
    Optional<T> findById(UUID id);
    List<T> findAll();
}
