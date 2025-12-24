package org.kooralik.miniprojetjavafx.model;

import java.util.UUID;

public class Cours {
    private UUID id;
    private String code;
    private String intitule;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Cours(UUID id, String code, String intitule) {
        this.id = id;
        this.code = code;
        this.intitule = intitule;
    }

    public Cours() {}

    public Cours(String code, String intitule) {
        this.id = UUID.randomUUID();
        this.code = code;
        this.intitule = intitule;
    }

    @Override
    public String toString() {
        return this.code + " - " + this.intitule;
    }
}