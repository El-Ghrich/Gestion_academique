package org.kooralik.miniprojetjavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kooralik.miniprojetjavafx.dao.FiliereDAO;
import org.kooralik.miniprojetjavafx.model.Filiere;

import java.util.UUID;

public class FiliereController {

    @FXML private TextField txtCode;
    @FXML private TextField txtNom;
    @FXML private TextArea txtDescription;
    @FXML private TableView<Filiere> tableFilieres;
    @FXML private TableColumn<Filiere, String> colCode;
    @FXML private TableColumn<Filiere, String> colNom;
    @FXML private TableColumn<Filiere, String> colDesc;

    private final FiliereDAO dao = new FiliereDAO();
    private final ObservableList<Filiere> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Configurer les colonnes du tableau
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        // 2. Charger les données depuis la base
        refreshTable();

        tableFilieres.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCode.setText(newSelection.getCode());
                txtNom.setText(newSelection.getNom());
                txtDescription.setText(newSelection.getDescription());
            }
        });
    }
    @FXML
    private void onModifier() {
        // 1. Récupérer la ligne sélectionnée
        Filiere selection = tableFilieres.getSelectionModel().getSelectedItem();

        // 2. Vérifier si l'utilisateur a bien cliqué sur une ligne
        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur de sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une filière dans le tableau pour la modifier.");
            alert.showAndWait();
            return;
        }

        try {
            // 3. Mettre à jour l'objet avec les données des champs
            // IMPORTANT : On ne touche PAS à l'ID (selection.getId())
            selection.setCode(txtCode.getText());
            selection.setNom(txtNom.getText());
            selection.setDescription(txtDescription.getText());

            // 4. Appeler le DAO pour faire le UPDATE en base de données
            dao.update(selection);

            // 5. Rafraîchir l'affichage
            refreshTable();
            clearFields(); // Vide les champs après la modif

        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de modifier la filière");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void clearFields() {
        txtCode.setText("");
        txtNom.setText("");
        txtDescription.setText("");
    }

    private void refreshTable() {
        data.clear();
        data.addAll(dao.findAll());
        tableFilieres.setItems(data);
    }

    @FXML
    private void onAjouter() {
        Filiere f = new Filiere(UUID.randomUUID(), txtCode.getText(), txtNom.getText(), txtDescription.getText());
        dao.save(f);
        refreshTable();
        txtCode.clear(); txtNom.clear(); txtDescription.clear();
    }

    @FXML
    private void onSupprimer() {
        Filiere selection = tableFilieres.getSelectionModel().getSelectedItem();
        if (selection != null) {
            dao.delete(selection.getId());
            refreshTable();
        }
    }

}