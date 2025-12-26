package org.kooralik.miniprojetjavafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.kooralik.miniprojetjavafx.MainApp;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private void showFiliereView() {
        loadView("filiere-view.fxml");
    }

    @FXML
    private void showEtudiantView() {
        loadView("etudiant-view.fxml");
    }

    @FXML
    private void showCoursView() {loadView("cours-view.fxml");}

    @FXML
    private void showDossierView() {
        loadView("dossier-view.fxml");
    }

    private void loadView(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFileName));
            Parent view = loader.load();

            mainPane.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Impossible de charger la vue : " + fxmlFileName);
        }
    }
}