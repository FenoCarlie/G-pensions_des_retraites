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
    private TableColumn<Personne, Integer> PcId;

    @FXML
    private TableColumn<Personne, String> PcIm;

    @FXML
    private TableColumn<Personne, String> PcNom;

    @FXML
    private TableColumn<Personne, String> PcPrenom;

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
        PcDate_nais.setCellValueFactory(data -> data.getValue().date_naisProperty());
        PcContact.setCellValueFactory(data -> data.getValue().contactProperty());
        PcDiplome.setCellValueFactory(data -> data.getValue().diplomeProperty());
        PcSituation.setCellValueFactory(data -> data.getValue().situationProperty());
        PcNom_Conj.setCellValueFactory(data -> data.getValue().nom_conjointProperty());
        PcPrenom_Conj.setCellValueFactory(data -> data.getValue().prenom_conjointProperty());


        // Récupérer les données des personnes depuis la base de données
        loadDataFromDatabase();

        // Gérer le clic droit sur la table
        PcId.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showContextMenu(event);
            }
        });
    }

    private ContextMenu contextMenu; // Déclarez une variable de classe pour stocker le menu contextuel

    private void showContextMenu(MouseEvent event) {
        Personne selectedPersonne = PcId.getSelectionModel().getSelectedItem();

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

            contextMenu.show(PcId, event.getScreenX(), event.getScreenY());

            // Gérer l'événement de clic droit pour masquer le menu contextuel
            PcIm.setOnMousePressed(mouseEvent -> {
                if (mouseEvent.isSecondaryButtonDown()) {
                    if (contextMenu != null) {
                        contextMenu.hide();
                        contextMenu = null;
                    }
                }
            });
        }
    }

    private void modifierPersonne (Personne personne) {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier un personne");

        // Créer des champs de saisie pour les informations
        TextField tfIm = new TextField(personne.getIm());
        tfIm.setPromptText("Im");
        TextField tfNom = new TextField(personne.getNom());
        tfNom.setPromptText("Nom");
        TextField tfPrenom = new TextField(personne.getPrenom());
        tfPrenom.setPromptText("Prénom");
        TextField tfDate_nais = new TextField(personne.getDate_nais());
        tfDate_nais.setPromptText("Date de naissance");
        TextField tfContact = new TextField(personne.getContact());
        tfContact.setPromptText("Contacte");
        TextField tfDiplome = new TextField(personne.getDiplome());
        tfDiplome.setPromptText("Diplome");
        TextField tfSituation = new TextField(personne.getSituation());
        tfSituation.setPromptText("Situation");
        TextField tfNom_conjoint = new TextField(personne.getNom_conjoint());
        tfNom_conjoint.setPromptText("Nom conjoint");
        TextField tfPrenom_conjoint = new TextField(personne.getPrenom_conjoint());
        tfPrenom_conjoint.setPromptText("prenom conjoint");



        // Créer une disposition pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("Im:"), tfIm);
        gridPane.addRow(1, new Label("Nom:"), tfNom);
        gridPane.addRow(2, new Label("Prenom:"), tfPrenom);
        gridPane.addRow(3, new Label("Date de naissance:"), tfDate_nais);
        gridPane.addRow(3, new Label("Contact:"), tfContact);
        gridPane.addRow(3, new Label("Diplôme:"), tfDiplome);
        gridPane.addRow(3, new Label("Situation:"), tfSituation);
        gridPane.addRow(3, new Label("Nom Conjoint:"), tfNom_conjoint);
        gridPane.addRow(3, new Label("Prenom Conjoint:"), tfPrenom_conjoint);

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
            if (validateInputs(tfSituation.getText(), tfDate_nais.getText(), tfDiplome.getText(), tfContact.getText(), tfNom_conjoint.getText(), tfPrenom_conjoint.getText(), tfIm.getText(), tfDiplome.getText(), tfNom.getText(), tfPrenom.getText())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations du personne ici
            String im = tfIm.getText();
            String nom = tfNom.getText();
            String prenom = tfPrenom.getText();
            String date_nais = tfDate_nais.getText();
            String diplome = tfDiplome.getText();
            String contact = tfContact.getText();
            String situation = tfSituation.getText();
            String nom_conjoint = tfNom_conjoint.getText();
            String prenom_conjoint = tfPrenom_conjoint.getText();


            // Mettre à jour les propriétés du personne
            personne.setIm(im);
            personne.setDiplome(diplome);
            personne.setNom(nom);
            personne.setPrenom(prenom);
            personne.setDate_nais(date_nais);
            personne.setContact(contact);
            personne.setSituation(situation);
            personne.setNom_conjoint(nom_conjoint);
            personne.setPrenom_conjoint(prenom_conjoint);

            // Mettre à jour le personne dans la base de données
            updateDatabase(personne);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }

    }

    private void updateDatabase(Personne personne) {
        Connection conn = ConnectionDatabase.connect();

        try {
            // Préparez une instruction SQL pour mettre à jour le personne
            String updateQuery = "UPDATE personne SET im = ?, nom = ?, prenom = ?, datenais = ?, diplome = ?, contact = ?, situation = ?,nomconjoint = ?, prenomconjoint = ? WHERE id = ?";

            // Créez une déclaration préparée en utilisant l'instruction SQL
            PreparedStatement statement = conn.prepareStatement(updateQuery);

            // Définissez les valeurs des paramètres dans la déclaration préparée
            statement.setString(1, personne.getNumero());
            statement.setString(2, personne.getDiplome());
            statement.setString(3, personne.getCategorie());
            statement.setInt(4, personne.getMontant());
            statement.setInt(5, personne.getId());

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

    private void supprimerPersonne (Personne personne) {
        Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer le personne");
        confirmationDialog.setHeaderText("Êtes-vous sûr de vouloir supprimer ce personne ?");
        confirmationDialog.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la suppression, vous pouvez supprimer le personne de la base de données ici
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

                // Supprimer le personne
                String deleteQuery = "DELETE FROM Personne WHERE num_personne = ?";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                deleteStatement.setString(1, personne.getNumero());
                deleteStatement.executeUpdate();
                deleteStatement.close();

                System.out.println("Le personne a été supprimé avec succès.");

                conn.close();
            } else {
                showErrorMessage("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterPersonne() {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un personne");

        // Créer des champs de saisie pour les informations du personne
        TextField tfNumero = new TextField();
        tfNumero.setPromptText("Numéro");
        TextField tfDiplome = new TextField();
        tfDiplome.setPromptText("Diplôme");
        TextField tfCategorie = new TextField();
        tfCategorie.setPromptText("Catégorie");
        TextField tfMontant = new TextField();
        tfMontant.setPromptText("Montant");

        // Créer une disposition pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("Numéro:"), tfNumero);
        gridPane.addRow(1, new Label("Diplôme:"), tfDiplome);
        gridPane.addRow(2, new Label("Catégorie:"), tfCategorie);
        gridPane.addRow(3, new Label("Montant:"), tfMontant);

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
            if (validateInputs(tfNumero.getText(), tfDiplome.getText(), tfCategorie.getText(), tfMontant.getText(), tfNom_conjoint.getText(), tfPrenom_conjoint.getText(), tfIm.getText(), tfDiplome.getText(), tfNom.getText(), tfPrenom.getText())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations du personne ici
            String numero = tfNumero.getText();
            String diplome = tfDiplome.getText();
            String categorie = tfCategorie.getText();
            int montant = Integer.parseInt(tfMontant.getText());

            Personneadd personne = new Personneadd(numero, diplome, categorie, montant);
            addToDatabase(personne);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }

    private boolean validateInputs(String numero, String diplome, String categorie, String montant, String text, String tfPrenomConjointText, String tfImText, String tfDiplomeText, String tfNomText, String tfPrenomText) {
        // Vérifier si les champs sont vides
        if (numero.isEmpty() || diplome.isEmpty() || categorie.isEmpty() || montant.isEmpty()) {
            showErrorMessage("Veuillez remplir tous les champs.");
            return true;
        }

        // Vérifier si le montant est un entier valide
        try {
            int montantValue = Integer.parseInt(montant);
            if (montantValue <= 0) {
                showErrorMessage("Le montant doit être un entier positif.");
                return true;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Le montant doit être un entier valide.");
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

                // Vérifier si le numéro du personne existe déjà
                String existingQuery = "SELECT COUNT(*) FROM Personne WHERE num_personne = ?";
                PreparedStatement existingStatement = conn.prepareStatement(existingQuery);
                existingStatement.setString(1, personne.getNumero());
                ResultSet existingResultSet = existingStatement.executeQuery();
                existingResultSet.next();
                int existingCount = existingResultSet.getInt(1);
                existingResultSet.close();
                existingStatement.close();

                if (existingCount > 0) {
                    // Le numéro du personne existe déjà, afficher un message d'erreur
                    showErrorMessage("Le numéro du personne existe déjà.");
                } else {
                    // Insérer le nouveau personne
                    String insertQuery = "INSERT INTO Personne (num_personne, diplome, categorie, montant) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                    insertStatement.setString(1, personne.getNumero());
                    insertStatement.setString(2, personne.getDiplome());
                    insertStatement.setString(3, personne.getCategorie());
                    insertStatement.setInt(4, personne.getMontant());
                    insertStatement.executeUpdate();
                    insertStatement.close();

                    System.out.println("Le personne a été ajouté avec succès.");
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
        // Effacer les données existantes
        personnes.clear();

        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                // Récupérer les personnes depuis la base de données
                String query = "SELECT * FROM personne";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Ajouter les personnes à la liste observable
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String numero = resultSet.getString("num_personne");
                    String diplome = resultSet.getString("diplome");
                    String categorie = resultSet.getString("categorie");
                    int montant = resultSet.getInt("montant");

                    personne personne = new personne(id, numero, diplome, categorie, montant);
                    personnes.add(personne);
                }

                // Fermer les ressources
                resultSet.close();
                statement.close();
                conn.close();
            } else {
                showErrorMessage("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}