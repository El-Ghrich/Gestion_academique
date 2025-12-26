package org.kooralik.miniprojetjavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kooralik.miniprojetjavafx.dao.CoursDAO;
import org.kooralik.miniprojetjavafx.model.Cours;

import java.util.UUID;

public class CoursController {
    @FXML private TextField txtCode;
    @FXML private TextField txtIntitule;
    @FXML private TableView<Cours> tableCours;
    @FXML private TableColumn<Cours, String> colCode;
    @FXML private TableColumn<Cours, String> colIntitule;

    private final CoursDAO dao = new CoursDAO();
    private final ObservableList<Cours> data = FXCollections.observableArrayList();

    @FXML public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colIntitule.setCellValueFactory(new PropertyValueFactory<>("intitule"));

        refreshTable();
        tableCours.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCode.setText(newSelection.getCode());
                txtIntitule.setText(newSelection.getIntitule());
            }
        });

    }
    private void refreshTable() {
        data.clear();
        data.addAll(dao.findAll());
        tableCours.setItems(data);
    }

    @FXML
    private void onAjouter() {
        Cours cours= new Cours(UUID.randomUUID(), txtCode.getText(), txtIntitule.getText());
        dao.save(cours);
        refreshTable();
        txtCode.clear(); txtIntitule.clear();
    }
    @FXML
    private void onSupprimer() {
        Cours selection = tableCours.getSelectionModel().getSelectedItem();
        if (selection != null) {
            dao.delete(selection.getId());
            refreshTable();
        }
    }

    @FXML
    private void onModifier() {
        Cours selection = tableCours.getSelectionModel().getSelectedItem();
        if (selection == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun cours sélectionné");
            alert.setContentText("Veuillez sélectionner un cours dans le tableau.");
            alert.showAndWait();
        }
        else {
        try {
            selection.setCode(txtCode.getText());
            selection.setIntitule(txtIntitule.getText());
            dao.update(selection);
            refreshTable(); // Recharger le tableau pour voir les modifs
            clearFields();
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors de la modification : " + e.getMessage());
            alert.showAndWait();
        }
        }
    }

    private void clearFields() {
        txtCode.clear();
        txtIntitule.clear();
    }

}
