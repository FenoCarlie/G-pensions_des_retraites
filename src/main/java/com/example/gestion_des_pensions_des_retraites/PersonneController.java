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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TableColumn<Personne, String> PcPrenom;

    @FXML
    private TableColumn<Personne, String> PcStatut;

    @FXML
    private TableColumn<Personne, String> PcDate_nais;

    @FXML
    private TableColumn<Personne, String> PcContact;

    @FXML
    private TableColumn<Personne, String> PcDiplome;

    @FXML
    private TableColumn<Personne, String> PcSituation;

    @FXML
    private TableColumn<Personne, String> PcNom_Conj;

    @FXML
    private TableColumn<Personne, String> PcPrenom_Conj;

    private ObservableList<Personne> personnes;

    @FXML
    private void initialize() {
        personnes = FXCollections.observableArrayList();

        PcId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        PcIm.setCellValueFactory(data -> data.getValue().imProperty());
        PcNom.setCellValueFactory(data -> data.getValue().nomProperty());
        PcPrenom.setCellValueFactory(data -> data.getValue().prenomProperty());
        PcStatut.setCellValueFactory(data -> data.getValue().prenomProperty());
        PcDate_nais.setCellValueFactory(data -> data.getValue().date_naisProperty());
        PcContact.setCellValueFactory(data -> data.getValue().contactProperty());
        PcDiplome.setCellValueFactory(data -> data.getValue().diplomeProperty());
        PcSituation.setCellValueFactory(data -> data.getValue().situationProperty());
        PcNom_Conj.setCellValueFactory(data -> data.getValue().nom_conjointProperty());
        PcPrenom_Conj.setCellValueFactory(data -> data.getValue().prenom_conjointProperty());

        tbvPersonnes.setItems(personnes);

        // Récupérer les données des personnes depuis la base de données
        loadDataFromDatabase();

        // Gérer le clic droit sur la table
        tbvPersonnes.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showContextMenu(event);
            }
        });
    }

    private ContextMenu contextMenu; // Déclarez une variable de classe pour stocker le menu contextuel

    private void showContextMenu(MouseEvent event) {
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

            contextMenu.show(tbvPersonnes, event.getScreenX(), event.getScreenY());

            // Gérer l'événement de clic droit pour masquer le menu contextuel
            tbvPersonnes.setOnMousePressed(mouseEvent -> {
                if (mouseEvent.isSecondaryButtonDown()) {
                    contextMenu.hide();
                    contextMenu = null;
                }
            });
        }
    }

    private void modifierPersonne(Personne personne) {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier un personne");

        // Créer des champs de saisie pour les informations du personne
        TextField tfIm = new TextField(personne.getIm());
        TextField tfNom = new TextField(personne.getNom());
        TextField tfPrenom = new TextField(personne.getPrenom());
        TextField tfDateNais = new TextField(personne.getDate_nais());
        TextField tfContact = new TextField(personne.getContact());
        TextField tfStatut = new TextField(personne.getContact());
        TextField tfDiplome = new TextField(personne.getDiplome());
        TextField tfSituation = new TextField(personne.getSituation());
        TextField tfNomConj = new TextField(personne.getNom_conjoint());
        TextField tfPrenomConj = new TextField(personne.getPrenom_conjoint());


        // Créer une disposition pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("IM:"), tfIm);
        gridPane.addRow(1, new Label("Nom:"), tfNom);
        gridPane.addRow(2, new Label("Prénom:"), tfPrenom);
        gridPane.addRow(3, new Label("Date de Naissance:"), tfDateNais);
        gridPane.addRow(4, new Label("Contact:"), tfContact);
        gridPane.addRow(4, new Label("Statut:"), tfStatut);
        gridPane.addRow(5, new Label("Diplôme:"), tfDiplome);
        gridPane.addRow(6, new Label("Situation:"), tfSituation);
        gridPane.addRow(7, new Label("Nom du Conjoint:"), tfNomConj);
        gridPane.addRow(8, new Label("Prénom du Conjoint:"), tfPrenomConj);

        dialog.getDialogPane().setContent(gridPane);

        // Ajouter des boutons de confirmation et d'annulation
        ButtonType validerButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, annulerButtonType);

        // Obtenir le bouton de validation
        Button validerButton = (Button) dialog.getDialogPane().lookupButton(validerButtonType);

        // Désactiver le bouton de validation par défaut
        validerButton.setDefaultButton(false);

        // Écouter les événements de clic sur le bouton de validation
        validerButton.addEventFilter(ActionEvent.ACTION, event -> {
            // Vérifier les entrées utilisateur et afficher un message d'erreur si nécessaire
            if (validateInputs(tfIm.getText(), tfNom.getText(), tfPrenom.getText(), tfDateNais.getText(), tfContact.getText(), tfStatut.getText(), tfDiplome.getText(), tfSituation.getText(), tfNomConj.getText(), tfPrenomConj.getText())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations de la personne ici
            String im = tfIm.getText();
            String nom = tfNom.getText();
            String prenom = tfPrenom.getText();
            String dateNais = tfDateNais.getText();
            String contact = tfContact.getText();
            String statut= tfStatut.getText();
            String diplome = tfDiplome.getText();
            String situation = tfSituation.getText();
            String nomConj = tfNomConj.getText();
            String prenomConj = tfPrenomConj.getText();

            // Mettre à jour les propriétés de la personne
            personne.setIm(im);
            personne.setNom(nom);
            personne.setPrenom(prenom);
            personne.setDate_nais(dateNais);
            personne.setContact(contact);
            personne.setStatut(statut);
            personne.setDiplome(diplome);
            personne.setSituation(situation);
            personne.setNom_conjoint(nomConj);
            personne.setPrenom_conjoint(prenomConj);

            // Mettre à jour la personne dans la base de données
            updatePersonne(personne);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }

    @FXML
    private void ajouterPersonne() {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un personne");

        // Créer des champs de saisie pour les informations du tarif
        TextField tfIm = new TextField();
        TextField tfNom = new TextField();
        TextField tfPrenom = new TextField();
        TextField tfDateNais = new TextField();
        TextField tfContact = new TextField();
        TextField tfStatut = new TextField();
        TextField tfDiplome = new TextField();
        TextField tfSituation = new TextField();
        TextField tfNomConj = new TextField();
        TextField tfPrenomConj = new TextField();

        // Créer une disposition pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("IM:"), tfIm);
        gridPane.addRow(1, new Label("Nom:"), tfNom);
        gridPane.addRow(2, new Label("Prénom:"), tfPrenom);
        gridPane.addRow(3, new Label("Date de Naissance:"), tfDateNais);
        gridPane.addRow(4, new Label("Contact:"), tfContact);
        gridPane.addRow(4, new Label("Statut:"), tfStatut);
        gridPane.addRow(5, new Label("Diplôme:"), tfDiplome);
        gridPane.addRow(6, new Label("Situation:"), tfSituation);
        gridPane.addRow(7, new Label("Nom du Conjoint:"), tfNomConj);
        gridPane.addRow(8, new Label("Prénom du Conjoint:"), tfPrenomConj);


        dialog.getDialogPane().setContent(gridPane);

        // Ajouter les boutons "Valider" et "Annuler" à la fenêtre de dialogue
        ButtonType validerButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, annulerButtonType);

        // Obtenir le bouton de validation
        Button validerButton = (Button) dialog.getDialogPane().lookupButton(validerButtonType);

        // Désactiver le bouton de validation par défaut
        validerButton.setDefaultButton(false);

        // Écouter les événements de clic sur le bouton de validation
        validerButton.addEventFilter(ActionEvent.ACTION, event -> {
            // Vérifier les entrées utilisateur et afficher un message d'erreur si nécessaire
            if (validateInputs(tfIm.getText(), tfNom.getText(), tfPrenom.getText(), tfDateNais.getText(), tfContact.getText(), tfStatut.getText(), tfDiplome.getText(), tfSituation.getText(), tfNomConj.getText(), tfPrenomConj.getText())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations de la personne ici
            String im = tfIm.getText();
            String nom = tfNom.getText();
            String prenom = tfPrenom.getText();
            String dateNais = tfDateNais.getText();
            String contact = tfContact.getText();
            String statut= tfStatut.getText();
            String diplome = tfDiplome.getText();
            String situation = tfSituation.getText();
            String nomConj = tfNomConj.getText();
            String prenomConj = tfPrenomConj.getText();

            Personneadd personne = new Personneadd(im, nom, prenom, dateNais, contact, statut, diplome, situation, nomConj, prenomConj);
            addToDatabase(personne);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }


    private void updatePersonne(Personne personne) {
        Connection conn = ConnectionDatabase.connect();

        try {
            String updateQuery = "UPDATE personnes SET im = ?, nom = ?, prenom = ?, date_nais = ?, contact = ?, diplome = ?, " +
                    "situation = ?, nom_conj = ?, prenom_conj = ? WHERE id = ?";
            // Créez une déclaration préparée en utilisant l'instruction SQL
            PreparedStatement statement = conn.prepareStatement(updateQuery);

            statement.setString(1, personne.getIm());
            statement.setString(2, personne.getNom());
            statement.setString(3, personne.getPrenom());
            statement.setString(4, personne.getDate_nais());
            statement.setString(5, personne.getContact());
            statement.setString(6, personne.getDiplome());
            statement.setString(7, personne.getSituation());
            statement.setString(8, personne.getNom_conjoint());
            statement.setString(9, personne.getPrenom_conjoint());
            statement.setInt(10, personne.getId());

            // Exécutez la déclaration préparée pour effectuer la mise à jour
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Personne mis à jour avec succès dans la base de données.");
            } else {
                System.out.println("La mise à jour du personne a échoué.");
            }

            // Fermez la déclaration et la connexion à la base de données
            statement.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions liées à la base de données ici
        }
    }

    private void supprimerPersonne(Personne personne) {
        Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer le tarif");
        confirmationDialog.setHeaderText("Êtes-vous sûr de vouloir supprimer ce tarif ?");
        confirmationDialog.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la suppression, vous pouvez supprimer le tarif de la base de données ici
            deleteFromDatabase(personne);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }

    private void deleteFromDatabase(Personne personne) {
        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");

                // Supprimer le tarif
                String deleteQuery = "DELETE FROM TARIF WHERE im = ?";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                deleteStatement.setString(1, personne.getIm());
                deleteStatement.executeUpdate();
                deleteStatement.close();

                System.out.println("Le tarif a été supprimé avec succès.");

                conn.close();
            } else {
                showErrorMessage("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private boolean validateInputs( String im, String nom, String prenom, String dateNais, String contact, String statut, String diplome, String situation, String nomConj, String prenomConj) {
        // Vérifier si les champs sont vides
        if (im.isEmpty() || nom.isEmpty() || prenom.isEmpty() || dateNais.isEmpty() || contact.isEmpty() || statut.isEmpty() || diplome.isEmpty() || situation.isEmpty() || nomConj.isEmpty() || prenomConj.isEmpty()) {
            showErrorMessage("Veuillez remplir tous les champs.");
            return true;
        }
        return false;
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addToDatabase(Personneadd personne) {
        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");

                // Vérifier si le numéro du tarif existe déjà
                String existingQuery = "SELECT COUNT(*) FROM PERSONNE WHERE im = ?";
                PreparedStatement existingStatement = conn.prepareStatement(existingQuery);
                existingStatement.setString(1, personne.getIm());
                ResultSet existingResultSet = existingStatement.executeQuery();
                existingResultSet.next();
                int existingCount = existingResultSet.getInt(1);
                existingResultSet.close();
                existingStatement.close();

                if (existingCount > 0) {
                    // Le numéro du tarif existe déjà, afficher un message d'erreur
                    showErrorMessage("L'IM du tarif existe déjà.");
                } else {
                    // Insérer le nouveau tarif
                    String insertQuery = "INSERT INTO PERSONNE (im, nom, prenom, datenais, contact, statut, diplome, situation, nomconjoint, prenomconjoit) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                    insertStatement.setString(1, personne.getIm());
                    insertStatement.setString(2, personne.getNom());
                    insertStatement.setString(3, personne.getPrenom());
                    insertStatement.setString(4, personne.getDate_nais());
                    insertStatement.setString(5, personne.getContact());
                    insertStatement.setString(6, personne.getStatut());
                    insertStatement.setString(7, personne.getDiplome());
                    insertStatement.setString(8, personne.getSituation());
                    insertStatement.setString(9, personne.getNom_conjoint());
                    insertStatement.setString(10, personne.getPrenom_conjoint());

                    insertStatement.executeUpdate();
                    insertStatement.close();

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
    private void loadDataFromDatabase() {
        personnes.clear();

        // Charger les données depuis la base de données
        try {
            Connection conn = ConnectionDatabase.connect();
            if (conn != null) {
                // Récupérer les tarifs depuis la base de données
                String query = "SELECT * FROM personne";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Ajouter les tarifs à la liste observable
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String im = resultSet.getString("im");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String date_nais = resultSet.getString("date_nais");
                    String contact = resultSet.getString("contact");
                    String statut = resultSet.getString("statut");
                    String diplome = resultSet.getString("diplome");
                    String situation = resultSet.getString("situation");
                    String nom_conjoint = resultSet.getString("nom_conjoint");
                    String prenom_conjoint = resultSet.getString("prenom_conjoint");

                    Personne personne = new Personne(id, im, nom, prenom, date_nais, contact, statut, diplome, situation, nom_conjoint, prenom_conjoint);
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
}
