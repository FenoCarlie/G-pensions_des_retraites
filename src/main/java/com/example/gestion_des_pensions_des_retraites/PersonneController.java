package com.example.gestion_des_pensions_des_retraites;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;

public class PersonneController {
    @FXML
    private TableColumn<Personne, String> PcContact;

    @FXML
    private TableColumn<Personne, String> PcDatenais;

    @FXML
    private TableColumn<Personne, String> PcDiplome;

    @FXML
    private TableColumn<Personne, Integer> PcId;

    @FXML
    private TableColumn<Personne, String> PcIm;

    @FXML
    private TableColumn<Personne, String> PcNom;

    @FXML
    private TableColumn<Personne, String> PcNomconjoint;

    @FXML
    private TableColumn<Personne, String> PcPrenomconjoint;

    @FXML
    private TableColumn<Personne, String> PcPrenoms;

    @FXML
    private TableColumn<Personne, String> PcSituation;

    @FXML
    private TableColumn<Personne, String> PcStatut;

    @FXML
    private TextField filterField;

    @FXML
    private TableView<Personne> tbvPersonnes;

    @FXML
    private DatePicker dpDate;

    private ObservableList<Personne> personnes;

    @FXML
    private void initialize() {
        personnes = FXCollections.observableArrayList();

        PcId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        PcIm.setCellValueFactory(data -> data.getValue().imProperty());
        PcNom.setCellValueFactory(data -> data.getValue().nomProperty());
        PcPrenoms.setCellValueFactory(data -> data.getValue().prenomsProperty());
        PcDatenais.setCellValueFactory(data -> data.getValue().datenaisProperty());
        PcContact.setCellValueFactory(data -> data.getValue().contactProperty());
        PcStatut.setCellValueFactory(data -> data.getValue().statutProperty());
        PcDiplome.setCellValueFactory(data -> data.getValue().diplomeProperty());
        PcSituation.setCellValueFactory(data -> data.getValue().situationProperty());
        PcNomconjoint.setCellValueFactory(data -> data.getValue().nomconjointProperty());
        PcPrenomconjoint.setCellValueFactory(data -> data.getValue().prenomconjointProperty());
        tbvPersonnes.setItems(personnes);

        // Récupérer les données des tarifs depuis la base de données
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
        Personne selectedPayer = tbvPersonnes.getSelectionModel().getSelectedItem();

        if (selectedPayer != null) {
            if (contextMenu != null) {
                // Si le menu contextuel existe déjà, le fermer avant d'en créer un nouveau
                contextMenu.hide();
                contextMenu = null;
            }

            contextMenu = new ContextMenu();

            // Option Modifier
            MenuItem modifierMenuItem = new MenuItem("Modifier");
            modifierMenuItem.setOnAction(e -> modifierPayer(selectedPayer));
            contextMenu.getItems().add(modifierMenuItem);

            // Option Supprimer
            MenuItem supprimerMenuItem = new MenuItem("Supprimer");
            supprimerMenuItem.setOnAction(e -> supprimerPayer(selectedPayer));
            contextMenu.getItems().add(supprimerMenuItem);

            contextMenu.show(tbvPersonnes, event.getScreenX(), event.getScreenY());

            // Gérer l'événement de clic droit pour masquer le menu contextuel
            tbvPersonnes.setOnMousePressed(mouseEvent -> {
                if (mouseEvent.isSecondaryButtonDown()) {
                    if (contextMenu != null) {
                        contextMenu.hide();
                        contextMenu = null;
                    }
                }
            });
        }
    }

    @FXML
    private void ajouterPersonne() {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une personne");

        // Créer des champs de saisie pour les informations de la personne
        TextField tfIm = new TextField();
        tfIm.setPromptText("IM");
        TextField tfNom = new TextField();
        tfNom.setPromptText("Nom");
        TextField tfPrenoms = new TextField();
        tfPrenoms.setPromptText("Prénom");
        DatePicker dpDate = new DatePicker();
        dpDate.setPromptText("Date de naissance");
        TextField tfContact = new TextField();
        tfContact.setPromptText("Contact");
        ComboBox<String> cbDiplome = new ComboBox<>();
        cbDiplome.setPromptText("Diplôme");
        ComboBox<String> cbSituation = new ComboBox<>();
        cbSituation.setPromptText("Situation");
        cbSituation.getItems().addAll("divorcé(e)", "marié(e)", "veuf(ve)");
        TextField tfNomConjoint = new TextField();
        tfNomConjoint.setPromptText("Nom conjoint");
        TextField tfPrenomConjoint = new TextField();
        tfPrenomConjoint.setPromptText("Prénom conjoint");

        // Créer une disposition pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("IM:"), tfIm);
        gridPane.addRow(1, new Label("Nom:"), tfNom);
        gridPane.addRow(2, new Label("Prénom:"), tfPrenoms);
        gridPane.addRow(3, new Label("Date de naissance:"), dpDate);
        gridPane.addRow(4, new Label("Contact:"), tfContact);
        gridPane.addRow(5, new Label("Diplôme:"), cbDiplome);
        gridPane.addRow(6, new Label("Situation:"), cbSituation);
        gridPane.addRow(7, new Label("Nom conjoint:"), tfNomConjoint);
        gridPane.addRow(8, new Label("Prénom conjoint:"), tfPrenomConjoint);

        dialog.getDialogPane().setContent(gridPane);

        // Charger les diplômes depuis la base de données
        ObservableList<String> diplomeList = getDiplomesFromDatabase();
        cbDiplome.setItems(diplomeList);

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
            if (validateInputs(tfIm.getText(), tfNom.getText(), tfPrenoms.getText(), dpDate.getValue(), cbDiplome.getValue(), tfContact.getText(), cbSituation.getValue(), tfNomConjoint.getText(), tfPrenomConjoint.getText())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations de la personne ici
            String im = tfIm.getText();
            String nom = tfNom.getText();
            String prenoms= tfPrenoms.getText();
            LocalDate datenais = dpDate.getValue();
            String contact = tfContact.getText();
            String diplome = cbDiplome.getValue();
            String situation = cbSituation.getValue();
            String nomConjoint = tfNomConjoint.getText();
            String prenomConjoint = tfPrenomConjoint.getText();

            Personneadd personne = new Personneadd(im, nom, prenoms, datenais, situation, contact, diplome, nomConjoint, prenomConjoint);
            addToDatabase(personne);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }


    private ObservableList<String> getDiplomesFromDatabase() {
        ObservableList<String> diplomeList = FXCollections.observableArrayList();

        // Établir une connexion à la base de données
        try (Connection conn = ConnectionDatabase.connect()) {
            // Créer une instruction SQL pour récupérer les diplômes
            String sql = "SELECT diplome FROM tarif";

            // Exécuter la requête
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                // Parcourir les résultats de la requête
                while (rs.next()) {
                    String diplome = rs.getString("diplome");
                    diplomeList.add(diplome);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion à la base de données
        }

        return diplomeList;
    }


    private boolean validateInputs(String im, String nom, String prenom, LocalDate date, String contact, String diplome, String situation, String nomConjoint, String prenomConjoint) {
        boolean hasError = false;

        if (im.isEmpty() || nom.isEmpty() || prenom.isEmpty() || date == null || contact.isEmpty() || situation.isEmpty() || nomConjoint.isEmpty() || prenomConjoint.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText("Veuillez remplir tous les champs");
            alert.showAndWait();
            hasError = true;
        } else {
            try {
                Connection conn = ConnectionDatabase.connect();

                if (conn != null) {
                    System.out.println("La connexion à la base de données a été établie avec succès.");

                    // Vérifier si la valeur de "im" existe dans la table "personne"
                    String selectPersonneQuery = "SELECT COUNT(*) FROM personne WHERE im = ?";
                    PreparedStatement selectPersonneStatement = conn.prepareStatement(selectPersonneQuery);
                    selectPersonneStatement.setString(1, im);
                    ResultSet personneResultSet = selectPersonneStatement.executeQuery();
                    personneResultSet.next();
                    int personneRowCount = personneResultSet.getInt(1);
                    personneResultSet.close();
                    selectPersonneStatement.close();

                    if (personneRowCount > 0) {
                        // L'IM existe déjà, afficher un message d'erreur
                        showErrorMessage("L'IM existe déjà.");
                        hasError = true; // Définir hasError sur true car une erreur a été détectée
                    } else {
                        // Insérer la nouvelle personne
                        String insertQuery = "INSERT INTO personne (statut, im, nom, prenoms, datenais, situation, contact, diplome, nom_conjoint, prenom_conjoint) VALUES (true, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                        insertStatement.setString(1, im);
                        insertStatement.setString(2, nom);
                        insertStatement.setString(3, prenom);
                        insertStatement.setDate(4, java.sql.Date.valueOf(date));
                        insertStatement.setString(5, situation);
                        insertStatement.setString(6, contact);
                        insertStatement.setString(7, diplome);
                        insertStatement.setString(8, nomConjoint);
                        insertStatement.setString(9, prenomConjoint);
                        insertStatement.executeUpdate();
                        insertStatement.close();

                        System.out.println("La personne a été ajoutée avec succès.");
                    }

                    conn.close();
                } else {
                    showErrorMessage("Échec de la connexion à la base de données.");
                    hasError = true; // Définir hasError sur true car une erreur a été détectée
                }
            } catch (SQLException e) {
                e.printStackTrace();
                hasError = true; // Définir hasError sur true car une exception s'est produite
            }
        }

        return hasError;
    }


    private void addToDatabase(Personneadd personne) {
        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");

                // Vérifier si la valeur de "im" existe dans la table "personne"
                String selectPersonneQuery = "SELECT COUNT(*) FROM personne WHERE im = ?";
                PreparedStatement selectPersonneStatement = conn.prepareStatement(selectPersonneQuery);
                selectPersonneStatement.setString(1, personne.getIm());
                ResultSet personneResultSet = selectPersonneStatement.executeQuery();
                personneResultSet.next();
                int personneRowCount = personneResultSet.getInt(1);
                personneResultSet.close();
                selectPersonneStatement.close();

                if (personneRowCount > 0) {
                    // L'IM existe déjà, afficher un message d'erreur
                    showErrorMessage("L'IM existe déjà.");
                } else {
                    // Insérer la nouvelle personne
                    String insertQuery = "INSERT INTO personne (statut, im, nom, prenoms, datenais, situation, contact, diplome, nomconjoint, prenomconjoint) VALUES (true, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                    insertStatement.setString(1, personne.getIm());
                    insertStatement.setString(2, personne.getNom());
                    insertStatement.setString(3, personne.getPrenoms());
                    insertStatement.setDate(4, java.sql.Date.valueOf(personne.getDatenais()));
                    insertStatement.setString(5, personne.getSituation());
                    insertStatement.setString(6, personne.getContact());
                    insertStatement.setString(7, personne.getDiplome());
                    insertStatement.setString(8, personne.getNomconjoint());
                    insertStatement.setString(9, personne.getPrenomconjoint());
                    insertStatement.executeUpdate();
                    insertStatement.close();

                    System.out.println("La personne a été ajoutée avec succès.");
                }

                conn.close();
            } else {
                showErrorMessage("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private void modifierPayer(Personne personne) {

    }

    private void supprimerPayer (Personne personne) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer la paye");
        confirmationDialog.setHeaderText("Êtes-vous sûr de vouloir supprimer ce paye ?");
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
                String deleteQuery = "DELETE FROM personne WHERE id = ?";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, personne.getId());
                deleteStatement.executeUpdate();
                deleteStatement.close();

                System.out.println("La paye a été supprimé avec succès.");

                conn.close();
            } else {
                showErrorMessage("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromDatabase() {
        // Clear existing data
        personnes.clear();

        try (Connection conn = ConnectionDatabase.connect()) {
            if (conn != null) {
                // Retrieve payers from the database
                String query = "SELECT * FROM personne";
                try (PreparedStatement statement = conn.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    // Add payers to the observable list
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String im = resultSet.getString("im");
                        String nom = resultSet.getString("nom");
                        String prenoms = resultSet.getString("prenoms");
                        java.sql.Date datenais = resultSet.getDate("datenais");
                        String contact = resultSet.getString("contact");
                        boolean statut = resultSet.getBoolean("statut");
                        String statutText = statut ? "vivant" : "décédé"; // Modification ici
                        String diplome = resultSet.getString("diplome");
                        String situation = resultSet.getString("situation");
                        String nomconjoint = resultSet.getString("nomconjoint");
                        String prenomconjoint = resultSet.getString("prenomconjoint");

                        // Format the date using SimpleDateFormat
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                        String formattedDate = dateFormatter.format(datenais);

                        Personne personne = new Personne( id, im, nom, prenoms, formattedDate, situation, statutText, contact, diplome, nomconjoint, prenomconjoint);
                        personnes.add(personne);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorMessage("Échec de la connexion à la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
