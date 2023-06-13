package com.example.gestion_des_pensions_des_retraites;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.sql.*;
import java.util.Date;
import java.util.Optional;

public class PersonneController {

    @FXML
    private TableView<Personne> tbvPersonnes;

    @FXML
    private TableColumn<Personne, Integer> PcId;

    @FXML
    private TableColumn<Personne, String> PcIm;

    @FXML
    private TableColumn<Personne, String> PcNom;

    @FXML
    private TableColumn<Personne, String> PcPrenoms;

    @FXML
    private TableColumn<Personne, Boolean> PcStatut;

    @FXML
    private TableColumn<Personne, Date> PcDatenais;

    @FXML
    private TableColumn<Personne, String> PcContact;

    @FXML
    private TableColumn<Personne, String> PcDiplome;

    @FXML
    private TableColumn<Personne, String> PcSituation;

    @FXML
    private TableColumn<Personne, String> PcNomconjoint;

    @FXML
    private TableColumn<Personne, String> PcPrenomconjoint;

    private ObservableList<Personne> personnes;

    @FXML
    private void initialize() {
        personnes = FXCollections.observableArrayList();

        PcId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        PcIm.setCellValueFactory(data -> data.getValue().imProperty());
        PcNom.setCellValueFactory(data -> data.getValue().nomProperty());
        PcPrenoms.setCellValueFactory(data -> data.getValue().prenomsProperty());
        PcStatut.setCellValueFactory(data -> data.getValue().statutProperty());
        PcDatenais.setCellValueFactory(data -> data.getValue().datenaisProperty());
        PcContact.setCellValueFactory(data -> data.getValue().contactProperty());
        PcDiplome.setCellValueFactory(data -> data.getValue().diplomeProperty());
        PcSituation.setCellValueFactory(data -> data.getValue().situationProperty());
        PcNomconjoint.setCellValueFactory(data -> data.getValue().nomconjointProperty());
        PcPrenomconjoint.setCellValueFactory(data -> data.getValue().prenomconjointProperty());

        tbvPersonnes.setItems(personnes);

        // Récupérer les données des personnes depuis la base de données
        loadDataFromDatabase();

        // Gérer le clic droit sur la table
        /*tbvPersonnes.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showContextMenu(event);
            }
        });*/
    }

    private ContextMenu contextMenu; // Déclarez une variable de classe pour stocker le menu contextuel

    /*private void showContextMenu(MouseEvent event) {
        Personne selectedPersonne = tbvPersonnes.getSelectionModel().getSelectedItem();

        if (selectedPersonne != null) {
            if (contextMenu != null) {
                // Si le menu contextuel existe déjà, le fermer avant d'en créer un nouveau
                contextMenu.hide();
                contextMenu = null;
            }

            contextMenu = new ContextMenu();

            // Option Modifier
            MenuItem modifierMenuItem = new MenuItem("Modifier");
            modifierMenuItem.setOnAction(e -> modifierPersonne(selectedPersonne));
            contextMenu.getItems().add(modifierMenuItem);

            // Option Supprimer
            MenuItem supprimerMenuItem = new MenuItem("Supprimer");
            supprimerMenuItem.setOnAction(e -> supprimerPersonne(selectedPersonne));
            contextMenu.getItems().add(supprimerMenuItem);

            // Afficher le menu contextuel à l'emplacement du clic
            contextMenu.show(tbvPersonnes, event.getScreenX(), event.getScreenY());
        }
    }*/

    @FXML
    private void ajouterPersonne() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une personne");

        // Créer une grille pour les champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Créer les champs de saisie
        TextField tfim = new TextField();
        tfim.setPromptText("IM");
        TextField tfnom = new TextField();
        tfnom.setPromptText("Nom");
        TextField tfprenoms = new TextField();
        tfprenoms.setPromptText("Prénoms");
        DatePicker datenaisDatePicker = new DatePicker();
        datenaisDatePicker.setPromptText("Date de naissance");
        TextField tfcontact = new TextField();
        tfcontact.setPromptText("Contact");
        TextField tfdiplome = new TextField();
        tfdiplome.setPromptText("Diplôme");
        TextField tfsituation = new TextField();
        tfsituation.setPromptText("Situation");
        TextField tfnomconjoint = new TextField();
        tfnomconjoint.setPromptText("Nom conjoint");
        TextField tfprenomconjoint = new TextField();
        tfprenomconjoint.setPromptText("Prénom conjoint");

        // Ajouter les champs de saisie à la grille
        grid.add(new Label("IM:"), 0, 0);
        grid.add(tfim, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(tfnom, 1, 1);
        grid.add(new Label("Prénoms:"), 0, 2);
        grid.add(tfprenoms, 1, 2);
        grid.add(new Label("Date de naissance:"), 0, 3);
        grid.add(datenaisDatePicker, 1, 3);
        grid.add(new Label("Contact:"), 0, 4);
        grid.add(tfcontact, 1, 4);
        grid.add(new Label("Diplôme:"), 0, 5);
        grid.add(tfdiplome, 1, 5);
        grid.add(new Label("Situation:"), 0, 6);
        grid.add(tfsituation, 1, 6);
        grid.add(new Label("Nom conjoint:"), 0, 7);
        grid.add(tfnomconjoint, 1, 7);
        grid.add(new Label("Prénom conjoint:"), 0, 8);
        grid.add(tfprenomconjoint, 1, 8);

        dialog.getDialogPane().setContent(grid);

        // Ajouter les boutons "Valider" et "Annuler" à la fenêtre de dialogue
        ButtonType validerButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, annulerButtonType);

        // Obtenir le bouton de validation
        Button validerButton = (Button) dialog.getDialogPane().lookupButton(validerButtonType);

        // Désactiver le bouton de validation par défaut
        validerButton.setDefaultButton(false);

        // Demander à l'utilisateur d'ajouter la personne
        Optional<ButtonType> result = dialog.showAndWait();

        // Écouter les événements de clic sur le bouton de validation
        validerButton.addEventFilter(ActionEvent.ACTION, event -> {
            // Vérifier les entrées utilisateur et afficher un message d'erreur si nécessaire
            if (validateInputs(tfim.getText(), tfnom.getText(), tfprenoms.getText(), datenaisDatePicker.getTooltip(), tfcontact.getText(), tfdiplome.getText(), tfsituation.getText(), tfnomconjoint.getText(), tfprenomconjoint.getText())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });
        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations du tarif ici
            String im = tfim.getText();
            String nom = tfnom.getText();
            String prenoms = tfprenoms.getText();
            Tooltip datenais = datenaisDatePicker.getTooltip();
            String contact = tfcontact.getText();
            String diplome = tfdiplome.getText();
            String situation = tfsituation.getText();
            String nomconjoint = tfnomconjoint.getText();
            String prenomconjoint = tfprenomconjoint.getText();

            Personneadd personne = new Personneadd(im, nom, prenoms, datenais, contact, diplome, situation, nomconjoint, prenomconjoint);
            addToDatabase(personne);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }

    private boolean validateInputs(String im, String nom, String prenoms, Date datenais) {
        // Vérifier si les champs sont vides
        if (im.isEmpty() || nom.isEmpty() || prenoms.isEmpty() || datenais == null) {
        showErrorMessage("Veuillez remplir tous les champs.");
        return true;
        }
        return false;
    }

    /*private void modifierPersonne(Personne personne) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier une personne");
        dialog.setHeaderText("Veuillez modifier les informations de la personne.");

        // Définir les boutons Modifier et Annuler
        ButtonType modifierButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierButtonType, ButtonType.CANCEL);

        // Créer une grille pour les champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Créer les champs de saisie pré-remplis avec les informations de la personne sélectionnée
        TextField imTextField = new TextField(personne.getIm());
        TextField nomTextField = new TextField(personne.getNom());
        TextField prenomsTextField = new TextField(personne.getPrenoms());
        DatePicker datenaisDatePicker = new DatePicker(personne.getDatenais().toLocalDate());
        TextField contactTextField = new TextField(personne.getContact());
        TextField diplomeTextField = new TextField(personne.getDiplome());
        TextField situationTextField = new TextField(personne.getSituation());
        TextField nomconjointTextField = new TextField(personne.getNomconjoint());
        TextField prenomconjointTextField = new TextField(personne.getPrenomconjoint());

        // Ajouter les champs de saisie à la grille
        grid.add(new Label("IM:"), 0, 0);
        grid.add(imTextField, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomTextField, 1, 1);
        grid.add(new Label("Prénoms:"), 0, 2);
        grid.add(prenomsTextField, 1, 2);
        grid.add(new Label("Date de naissance:"), 0, 3);
        grid.add(datenaisDatePicker, 1, 3);
        grid.add(new Label("Contact:"), 0, 4);
        grid.add(contactTextField, 1, 4);
        grid.add(new Label("Diplôme:"), 0, 5);
        grid.add(diplomeTextField, 1, 5);
        grid.add(new Label("Situation:"), 0, 6);
        grid.add(situationTextField, 1, 6);
        grid.add(new Label("Nom conjoint:"), 0, 7);
        grid.add(nomconjointTextField, 1, 7);
        grid.add(new Label("Prénom conjoint:"), 0, 8);
        grid.add(prenomconjointTextField, 1, 8);

        dialog.getDialogPane().setContent(grid);

        // Demander à l'utilisateur de modifier la personne
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == modifierButtonType) {
            String im = imTextField.getText();
            String nom = nomTextField.getText();
            String prenoms = prenomsTextField.getText();
            Date datenais = java.sql.Date.valueOf(datenaisDatePicker.getValue());
            String contact = contactTextField.getText();
            String diplome = diplomeTextField.getText();
            String situation = situationTextField.getText();
            String nomconjoint = nomconjointTextField.getText();
            String prenomconjoint = prenomconjointTextField.getText();

            // Valider les champs obligatoires
            if (im.isEmpty() || nom.isEmpty() || prenoms.isEmpty() || datenais == null) {
                showAlertDialog(AlertType.WARNING, "Champs obligatoires", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Mettre à jour les informations de la personne dans la base de données
            updatePersonne(personne.getId(), im, nom, prenoms, datenais, contact, diplome, situation, nomconjoint, prenomconjoint);

            // Rafraîchir la liste des personnes
            loadDataFromDatabase();
        }
    }*/

    /*private void supprimerPersonne(Personne personne) {
        Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer une personne");
        confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cette personne ?");

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer la personne de la base de données
            deletePersonne(personne.getId());

            // Rafraîchir la liste des personnes
            loadDataFromDatabase();
        }
    }*/

    private void loadDataFromDatabase() {
        personnes.clear();

        // Établir une connexion à la base de données
        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                // Récupérer les tarifs depuis la base de données
                String query = "SELECT * FROM personne";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String im = resultSet.getString("im");
                String nom = resultSet.getString("nom");
                String prenoms = resultSet.getString("prenoms");
                boolean statut = resultSet.getBoolean("statut");
                Date datenais = resultSet.getDate("datenais");
                String contact = resultSet.getString("contact");
                String diplome = resultSet.getString("diplome");
                String situation = resultSet.getString("situation");
                String nomconjoint = resultSet.getString("nomconjoint");
                String prenomconjoint = resultSet.getString("prenomconjoint");

                Personne personne = new Personne(id, im, nom, prenoms, datenais, contact, statut, diplome, situation, nomconjoint, prenomconjoint);
                personnes.add(personne);
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
            conn.close();
        } else {
                showErrorMessage("Échec de la connexion à la base de données.");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addToDatabase(Personneadd personne) {
        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");
                // Vérifier si le Im du tarif existe déjà
                String existingQuery = "SELECT COUNT(*) FROM personne WHERE im = ?";
                PreparedStatement existingStatement = conn.prepareStatement(existingQuery);
                existingStatement.setString(1, personne.getIm());
                ResultSet existingResultSet = existingStatement.executeQuery();
                existingResultSet.next();
                int existingCount = existingResultSet.getInt(1);
                existingResultSet.close();
                existingStatement.close();

                if (existingCount > 0) {
                    // Le numéro du tarif existe déjà, afficher un message d'erreur
                    showErrorMessage("Le numéro du tarif existe déjà.");
                } else {
                    String insertQuery = "INSERT INTO personnes (im, nom, prenoms, datenais, contact, diplome, situation, nomconjoint, prenomconjoint) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                    insertStatement.setString(1, personne.getIm());
                    insertStatement.setString(2, personne.getNom());
                    insertStatement.setString(3, personne.getPrenoms());
                    insertStatement.setDate(4, new java.sql.Date(personne.getDatenais()));
                    insertStatement.setString(5, personne.getContact());
                    insertStatement.setBoolean(5, personne.getStatut());
                    insertStatement.setString(6, personne.getDiplome());
                    insertStatement.setString(7, personne.getSituation());
                    insertStatement.setString(8, personne.getNomconjoint());
                    insertStatement.setString(9, personne.getPrenomconjoint());
                    //String im, String nom, String prenoms, Date datenais, String contact, String diplome, String situation, String nomconjoint, String prenomconjoint
                    System.out.println("Le tarif a été ajouté avec succès.");
                }
                conn.close();
        } else {
                showErrorMessage("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*private void updatePersonne(int id, String im, String nom, String prenoms, Date datenais, String contact, String diplome, String situation, String nomconjoint, String prenomconjoint) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_des_pensions_des_retraites", "root", "")) {
            String query = "UPDATE personnes SET im = ?, nom = ?, prenoms = ?, datenais = ?, contact = ?, diplome = ?, situation = ?, nomconjoint = ?, prenomconjoint = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, im);
            statement.setString(2, nom);
            statement.setString(3, prenoms);
            statement.setDate(4, new java.sql.Date(datenais.getTime()));
            statement.setString(5, contact);
            statement.setString(6, diplome);
            statement.setString(7, situation);
            statement.setString(8, nomconjoint);
            statement.setString(9, prenomconjoint);
            statement.setInt(10, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    /*private void deletePersonne(int id) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_des_pensions_des_retraites", "root", "")) {
            String query = "DELETE FROM personnes WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
