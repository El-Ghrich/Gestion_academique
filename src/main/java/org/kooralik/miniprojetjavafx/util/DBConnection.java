package org.kooralik.miniprojetjavafx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // 1. Paramètres de connexion
    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String DB_NAME = "gestion_academique";
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME;
    private static final String USER = "postgres"; // Utilisateur par défaut
    private static final String PASSWORD = "Rahrah.123"; // REMPLACE PAR TON MOT DE PASSE

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("--- Connexion établie avec PostgreSQL ---");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL non trouvé ! Ajoute la dépendance au projet.");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
        return connection;
    }
}