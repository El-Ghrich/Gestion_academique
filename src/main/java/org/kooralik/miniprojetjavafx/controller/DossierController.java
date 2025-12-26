package org.kooralik.miniprojetjavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kooralik.miniprojetjavafx.dao.DossierAdministratifDAO; // Vérifie que c'est le bon nom
import org.kooralik.miniprojetjavafx.dao.EtudiantDAO;
import org.kooralik.miniprojetjavafx.model.DossierAdministratif; // Vérifie que c'est le bon nom
import org.kooralik.miniprojetjavafx.model.Etudiant;

import java.time.LocalDate;
import java.util.UUID;

public class DossierController {

    @FXML private TextField txtNumero;
    @FXML private DatePicker dateCreationPicker;
    @FXML private ComboBox<Etudiant> comboEtudiant;

    @FXML private TableView<DossierAdministratif> tableDossiers;
    @FXML private TableColumn<DossierAdministratif, String> colNumero;
    @FXML private TableColumn<DossierAdministratif, LocalDate> colDate;

    // Attention aux noms : Est-ce DossierDAO ou DossierAdministratifDAO ?
    private final DossierAdministratifDAO dossierDAO = new DossierAdministratifDAO();
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();

    private final ObservableList<DossierAdministratif> dossierData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numeroInscription"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));

        // Charger les étudiants
        comboEtudiant.setItems(FXCollections.observableArrayList(etudiantDAO.findAll()));

        refreshTable();

        // Listener : Remplir le formulaire au clic
        tableDossiers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNumero.setText(newVal.getNumeroInscription());
                dateCreationPicker.setValue(newVal.getDateCreation());

                // Sélectionner le bon étudiant dans la combo
                comboEtudiant.getItems().stream()
                        .filter(e -> e.getId().equals(newVal.getEleveId()))
                        .findFirst()
                        .ifPresent(e -> comboEtudiant.setValue(e));
            }
        });
    }

    @FXML
    private void onAjouter() {
        if (comboEtudiant.getValue() == null || dateCreationPicker.getValue() == null) {
            showAlert("Erreur", "Veuillez choisir un étudiant et une date.");
            return;
        }

        try {
            DossierAdministratif d = new DossierAdministratif();
            d.setId(UUID.randomUUID());
            d.setNumeroInscription(txtNumero.getText());
            d.setDateCreation(dateCreationPicker.getValue());
            d.setEleveId(comboEtudiant.getValue().getId());

            dossierDAO.save(d);
            refreshTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de créer le dossier : " + e.getMessage());
        }
    }

    @FXML
    private void onModifier() {
        DossierAdministratif selection = tableDossiers.getSelectionModel().getSelectedItem();
        if (selection == null) {
            showAlert("Attention", "Veuillez sélectionner un dossier.");
            return;
        }

        try {
            selection.setNumeroInscription(txtNumero.getText());
            selection.setDateCreation(dateCreationPicker.getValue());
            selection.setEleveId(comboEtudiant.getValue().getId());

            dossierDAO.update(selection);
            refreshTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    private void onSupprimer() {
        DossierAdministratif selection = tableDossiers.getSelectionModel().getSelectedItem();
        if (selection == null) {
            showAlert("Attention", "Veuillez sélectionner un dossier à supprimer.");
            return;
        }

        try {

            dossierDAO.delete(selection.getId());
            refreshTable();
            clearFields();

        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    // --- CES MÉTHODES DOIVENT ÊTRE DANS LA CLASSE (AVANT LA DERNIÈRE ACCOLADE) ---

    private void refreshTable() {
        dossierData.clear();
        dossierData.addAll(dossierDAO.findAll());
        tableDossiers.setItems(dossierData);
    }

    private void clearFields() {
        txtNumero.clear();
        dateCreationPicker.setValue(null);
        comboEtudiant.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

} // <--- FIN DE LA CLASSE ICI