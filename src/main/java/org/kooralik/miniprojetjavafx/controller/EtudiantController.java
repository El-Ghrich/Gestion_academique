package org.kooralik.miniprojetjavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kooralik.miniprojetjavafx.dao.EtudiantDAO;
import org.kooralik.miniprojetjavafx.dao.FiliereDAO;
import org.kooralik.miniprojetjavafx.model.Etudiant;
import org.kooralik.miniprojetjavafx.model.Filiere;
import org.kooralik.miniprojetjavafx.model.Status;

import java.util.UUID;

public class EtudiantController {

    // --- ÉLÉMENTS FXML ---
    @FXML private TextField txtMatricule;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;

    @FXML private ComboBox<Filiere> comboFiliere;
    @FXML private ComboBox<Status> comboStatus;

    @FXML private TableView<Etudiant> tableEtudiants;
    @FXML private TableColumn<Etudiant, String> colMatricule;
    @FXML private TableColumn<Etudiant, String> colNom;
    @FXML private TableColumn<Etudiant, String> colPrenom;
    @FXML private TableColumn<Etudiant, String> colEmail;
    @FXML private TableColumn<Etudiant, String> colStatus;
    // Note: Pour afficher le nom de la filière dans la colonne, c'est plus complexe.
    // Pour l'instant on affiche l'ID ou on laisse vide.

    // --- DAOs & DATA ---
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private final FiliereDAO filiereDAO = new FiliereDAO();
    private final ObservableList<Etudiant> etudiantData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Configurer les colonnes
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // 2. Charger les listes déroulantes
        comboFiliere.setItems(FXCollections.observableArrayList(filiereDAO.findAll()));
        comboStatus.setItems(FXCollections.observableArrayList(Status.values()));

        // 3. Charger le tableau
        refreshTable();

        // 4. LISTENER : Quand on clique sur une ligne, on remplit le formulaire
        tableEtudiants.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtMatricule.setText(newVal.getMatricule());
                txtNom.setText(newVal.getNom());
                txtPrenom.setText(newVal.getPrenom());
                txtEmail.setText(newVal.getEmail());
                comboStatus.setValue(newVal.getStatus());

                // Pour la filière, c'est un peu astucieux : il faut retrouver l'objet Filiere qui a le même ID
                comboFiliere.getItems().stream()
                        .filter(f -> f.getId().equals(newVal.getFiliere_id()))
                        .findFirst()
                        .ifPresent(f -> comboFiliere.setValue(f));
            }
        });
    }

    @FXML
    private void onAjouter() {
        if (!validateInput()) return;

        try {
            Etudiant etudiant = new Etudiant(
                    UUID.randomUUID(),
                    txtMatricule.getText(),
                    txtNom.getText(),
                    txtPrenom.getText(),
                    txtEmail.getText(),
                    comboFiliere.getValue().getId(),
                    comboStatus.getValue()
            );

            etudiantDAO.save(etudiant);
            refreshTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur ajout", e.getMessage());
        }
    }

    @FXML
    private void onModifier() {
        Etudiant selection = tableEtudiants.getSelectionModel().getSelectedItem();
        if (selection == null) {
            showAlert("Attention", "Veuillez sélectionner un étudiant.");
            return;
        }
        if (!validateInput()) return;

        try {
            selection.setMatricule(txtMatricule.getText());
            selection.setNom(txtNom.getText());
            selection.setPrenom(txtPrenom.getText());
            selection.setEmail(txtEmail.getText());
            selection.setFiliere_id(comboFiliere.getValue().getId());
            selection.setStatus(comboStatus.getValue());

            etudiantDAO.update(selection);
            refreshTable();
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur modification", e.getMessage());
        }
    }

    @FXML
    private void onSupprimer() {
        Etudiant selection = tableEtudiants.getSelectionModel().getSelectedItem();
        if (selection != null) {
            etudiantDAO.delete(selection.getId());
            refreshTable();
            clearFields();
        } else {
            showAlert("Attention", "Sélectionnez un étudiant à supprimer.");
        }
    }

    private void refreshTable() {
        etudiantData.clear();
        etudiantData.addAll(etudiantDAO.findAll());
        tableEtudiants.setItems(etudiantData);
    }

    private void clearFields() {
        txtMatricule.clear(); txtNom.clear(); txtPrenom.clear(); txtEmail.clear();
        comboFiliere.getSelectionModel().clearSelection();
        comboStatus.getSelectionModel().clearSelection();
    }

    private boolean validateInput() {
        if (comboFiliere.getValue() == null || comboStatus.getValue() == null || txtNom.getText().isEmpty()) {
            showAlert("Validation", "Veuillez remplir tous les champs obligatoires (Nom, Filière, Statut).");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}