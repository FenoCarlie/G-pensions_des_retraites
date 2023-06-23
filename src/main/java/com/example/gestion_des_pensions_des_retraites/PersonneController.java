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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonneController {
    @FXML
    private TableColumn<Personne, String> PcContact;

    @FXML
    private TableColumn<Personne, String> PcNumtarif;


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
    private Button searchButton;

    @FXML
    private void initialize() {
        personnes = FXCollections.observableArrayList();

        PcId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        PcIm.setCellValueFactory(data -> data.getValue().imProperty());
        PcNumtarif.setCellValueFactory(data -> data.getValue().numtarifProperty());
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

        searchButton.setOnAction(event -> {
            String searchText = filterField.getText().toUpperCase(); // Convertir le texte en majuscules
            rechercherPersonnes(searchText);
        });
    }

    private void rechercherPersonnes(String searchText) {
        // Créer une requête SQL pour effectuer la recherche en utilisant LIKE
        String query = "SELECT * FROM personne WHERE nom LIKE ? OR prenoms LIKE ? OR im LIKE ?";

        try (Connection conn = ConnectionDatabase.connect();
             PreparedStatement statement = conn.prepareStatement(query)) {

            // Définir les paramètres de la requête
            String searchPattern = "%" + searchText + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            // Exécuter la requête
            ResultSet resultSet = statement.executeQuery();

            // Créer une liste temporaire pour stocker les résultats de la recherche
            List<Personne> resultatRecherche = new ArrayList<>();

            // Parcourir les résultats de la requête et créer des objets Personne correspondants
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String im = resultSet.getString("im");
                String nom = resultSet.getString("nom");
                String prenoms = resultSet.getString("prenoms");
                String datenais = resultSet.getString("datenais");
                String contact = resultSet.getString("contact");
                boolean statut = resultSet.getBoolean("statut");
                String statutText = statut ? "vivant" : "décédé"; // Modification ici
                String diplome = resultSet.getString("diplome");
                String situation = resultSet.getString("situation");
                String nomconjoint = resultSet.getString("nomconjoint");
                String prenomconjoint = resultSet.getString("prenomconjoint");


                // Créer un objet Personne et l'ajouter à la liste des résultats
                Personne personne = new Personne(id, im, numtarif, nom, prenoms, datenais, situation, statutText, contact, diplome, nomconjoint, prenomconjoint);
                resultatRecherche.add(personne);
            }

            // Mettre à jour la table avec les résultats de la recherche
            tbvPersonnes.setItems(FXCollections.observableArrayList(resultatRecherche));

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

            // Option Payer
            MenuItem payerMenuItem = new MenuItem("Payer");
            payerMenuItem.setOnAction(e -> payerPersonne(selectedPersonne));
            contextMenu.getItems().add(payerMenuItem);

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
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une personne");

        // Création des champs de saisie
        TextField tfIm = createTextFieldWithPrompt("IM");
        TextField tfNom = createTextFieldWithPrompt("Nom");
        TextField tfPrenoms = createTextFieldWithPrompt("Prénom");
        TextField tfdatenais = createTextFieldWithPrompt("yyyy-mm-dd");
        TextField tfContact = createTextFieldWithPrompt("Contact");
        ComboBox<String> cbDiplome = createComboBoxWithPrompt("Diplôme");
        ComboBox<String> cbSituation = createComboBoxWithPrompt("Situation");
        cbSituation.getItems().addAll("divorcé(e)", "marié(e)", "veuf(ve)");
        TextField tfNomConjoint = createTextFieldWithPrompt("Nom conjoint");
        TextField tfPrenomConjoint = createTextFieldWithPrompt("Prénom conjoint");

        GridPane gridPane = createGridPane();
        gridPane.addRow(0, new Label("IM:"), tfIm);
        gridPane.addRow(1, new Label("Nom:"), tfNom);
        gridPane.addRow(2, new Label("Prénom:"), tfPrenoms);
        gridPane.addRow(3, new Label("Date de naissance:"), tfdatenais);
        gridPane.addRow(4, new Label("Contact:"), tfContact);
        gridPane.addRow(5, new Label("Diplôme:"), cbDiplome);
        gridPane.addRow(6, new Label("Situation:"), cbSituation);
        gridPane.addRow(7, new Label("Nom conjoint:"), tfNomConjoint);
        gridPane.addRow(8, new Label("Prénom conjoint:"), tfPrenomConjoint);

        dialog.getDialogPane().setContent(gridPane);

        ObservableList<String> diplomeList = getDiplomesFromDatabase();
        cbDiplome.setItems(diplomeList);

        ButtonType validerButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, annulerButtonType);

        Button validerButton = (Button) dialog.getDialogPane().lookupButton(validerButtonType);
        validerButton.setDefaultButton(false);

        validerButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (validateInputs(tfIm.getText(), tfNom.getText(), tfPrenoms.getText(), tfdatenais.getText(),
                    cbDiplome.getValue(), tfContact.getText(), cbSituation.getValue(),
                    tfNomConjoint.getText(), tfPrenomConjoint.getText())) {
                event.consume();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            String im = tfIm.getText();
            String nom = tfNom.getText();
            String prenoms = tfPrenoms.getText();
            LocalDate datenais = dpDate.getValue();
            String contact = tfContact.getText();
            String diplome = cbDiplome.getValue();
            String situation = cbSituation.getValue();
            String nomConjoint = tfNomConjoint.getText();
            String prenomConjoint = tfPrenomConjoint.getText();

            Personneadd personne = new Personneadd(im, nom, prenoms, datenais, situation, contact, diplome, nomConjoint, prenomConjoint);
            addToDatabase(personne);

            loadDataFromDatabase();
        }
    }

    private TextField createTextFieldWithPrompt(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }

    private ComboBox<String> createComboBoxWithPrompt(String promptText) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setPromptText(promptText);
        return comboBox;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        return gridPane;
    }

    private ObservableList<String> getDiplomesFromDatabase() {
        ObservableList<String> diplomeList = FXCollections.observableArrayList();
        try (Connection conn = ConnectionDatabase.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT diplome FROM tarif")) {
            while (rs.next()) {
                String diplome = rs.getString("diplome");
                diplomeList.add(diplome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diplomeList;
    }



    private boolean validateInputs(String im, String nom, String prenom, String datenais, String contact, String diplome, String situation, String nomconjoint, String prenomconjoint) {
        boolean hasError = false;
        if (im.isEmpty() || nom.isEmpty() || prenom.isEmpty() || datenais == null || contact.isEmpty() ||
                situation.isEmpty() || nomconjoint.isEmpty() || prenomconjoint.isEmpty()) {
            showErrorMessage("Veuillez remplir tous les champs.");
            hasError = true;
        } else {
            if (!nom.matches("[A-Za-z\\p{L}]+")) {
                showErrorMessage("Le nom ne doit contenir que des lettres.");
                hasError = true;
            } else {
                nom = nom.toUpperCase();
            }

            if (!nomconjoint.matches("[A-Za-z\\p{L}]+")) {
                showErrorMessage("Le nom ne doit contenir que des lettres.");
                hasError = true;
            } else {
                nomconjoint = nomconjoint.toUpperCase();
            }
        }

        return hasError;
    }

    private void addToDatabase(Personneadd personne) {
        try (Connection conn = ConnectionDatabase.connect()) {
            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");
                String selectPersonneQuery = "SELECT COUNT(*) FROM personne WHERE im = ?";
                PreparedStatement selectPersonneStatement = conn.prepareStatement(selectPersonneQuery);
                selectPersonneStatement.setString(1, personne.getIm());
                ResultSet personneResultSet = selectPersonneStatement.executeQuery();
                personneResultSet.next();
                int personneRowCount = personneResultSet.getInt(1);
                personneResultSet.close();
                selectPersonneStatement.close();

                if (personneRowCount > 0) {
                    showErrorMessage("L'IM existe déjà.");
                } else {
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

    private void payerPersonne (Personne personne) {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Payer une personne");

        // Création des champs de saisie
        TextField tfIm = new TextField(personne.getIm());
        tfIm.setPromptText("IM");
        TextField tfNom = new TextField(personne.getNom());
        tfNom.setPromptText("Nom");

    }

    private void modifierPersonne(Personne personne) {

        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier une personne");

        // Création des champs de saisie
        TextField tfIm = new TextField(personne.getIm());
        tfIm.setPromptText("IM");
        TextField tfNom = new TextField(personne.getNom());
        tfNom.setPromptText("Nom");
        TextField tfPrenoms = new TextField(personne.getPrenoms());
        tfPrenoms.setPromptText("Prénom");
        TextField tfDatenais = new TextField(personne.getDatenais());
        tfDatenais.setPromptText("Date de naissance");
        TextField tfContact = new TextField(personne.getContact());
        tfContact.setPromptText("Contact");
        TextField tfDiplome = new TextField(personne.getDiplome());
        tfDiplome.setPromptText("Diplome");
        ComboBox<String> cbSituation = createComboBoxWithPrompt("Situation");
        cbSituation.getItems().addAll("divorcé(e)", "marié(e)", "veuf(ve)");
        TextField tfNomconjoint = new TextField(personne.getNomconjoint());
        tfNomconjoint.setPromptText("Nom conjoint");
        TextField tfPrenomconjoint = new TextField(personne.getPrenomconjoint());
        tfPrenomconjoint.setPromptText("Prénom conjoint");

        // Création de la grille de disposition pour les champs de saisie
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("IM:"), tfIm);
        gridPane.addRow(1, new Label("Nom:"), tfNom);
        gridPane.addRow(2, new Label("Prénom:"), tfPrenoms);
        gridPane.addRow(3, new Label("Date de naissance:"), tfDatenais);
        gridPane.addRow(4, new Label("Contact:"), tfContact);
        gridPane.addRow(5, new Label("Diplome:"), tfDiplome);
        gridPane.addRow(6, new Label("Situation:"), cbSituation);
        gridPane.addRow(7, new Label("Nom conjoint:"), tfNomconjoint);
        gridPane.addRow(8, new Label("Prénom conjoint:"), tfPrenomconjoint);

        dialog.getDialogPane().setContent(gridPane);

        ButtonType validerButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        ButtonType annulerButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(validerButtonType, annulerButtonType);

        // Obtenir le bouton de validation
        Button validerButton = (Button) dialog.getDialogPane().lookupButton(validerButtonType);

        // Désactiver le bouton de validation par défaut
        validerButton.setDefaultButton(false);

        // Écouter les événements de clic sur le bouton de validation
        validerButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (validateInputs(tfIm.getText(), tfNom.getText(), tfPrenoms.getText(), tfDatenais.getText(),
                    tfContact.getText(), tfDiplome.getText(), cbSituation.getValue(),
                    tfNomconjoint.getText(), tfPrenomconjoint.getText())) {
                event.consume();
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // Effectuer la mise à jour dans la base de données
            String im = tfIm.getText();
            String nom = tfNom.getText();
            String prenoms = tfPrenoms.getText();
            String datenais = tfDatenais.getText(); // Supposons que la date est une chaîne de caractères
            String contact = tfContact.getText();
            String diplome = tfDiplome.getText();
            String situation = cbSituation.getValue();
            String nomconjoint = tfNomconjoint.getText();
            String prenomconjoint = tfPrenomconjoint.getText();

            // Mettre à jour les propriétés de la personne
            personne.setIm(im);
            personne.setNom(nom);
            personne.setPrenoms(prenoms);
            personne.setDatenais(datenais);
            personne.setContact(contact);
            personne.setDiplome(diplome);
            personne.setSituation(situation);
            personne.setNomconjoint(nomconjoint);
            personne.setPrenomconjoint(prenomconjoint);

            // Effectuer la mise à jour dans la base de données
            updateDatabase(personne);

            // Rafraîchir les données de la TableView
            loadDataFromDatabase();
        }
    }



    private void updateDatabase(Personne personne) {
        Connection conn = ConnectionDatabase.connect();

        try {
            // Préparez une instruction SQL pour mettre à jour la personne
            String updateQuery = "UPDATE personne SET im = ?, nom = ?, prenoms = ?, datenais = ?::date , contact = ?, diplome = ?, situation = ?, nomconjoint = ?, prenomconjoint = ? WHERE id = ?";

            // Créez une déclaration préparée en utilisant l'instruction SQL
            PreparedStatement statement = conn.prepareStatement(updateQuery);

            // Définissez les valeurs des paramètres dans la déclaration préparée
            statement.setString(1, personne.getIm());
            statement.setString(2, personne.getNom());
            statement.setString(3, personne.getPrenoms());
            statement.setString(4, personne.getDatenais());
            statement.setString(5, personne.getContact());
            statement.setString(6, personne.getDiplome());
            statement.setString(7, personne.getSituation());
            statement.setString(8, personne.getNomconjoint());
            statement.setString(9, personne.getPrenomconjoint());
            statement.setInt(10, personne.getId()); // Assuming getId() returns the ID of the personne object

            // Exécutez la déclaration préparée pour effectuer la mise à jour
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Personne mise à jour avec succès dans la base de données.");
            } else {
                System.out.println("La mise à jour de la personne a échoué.");
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


    private String getNumTarifForDiplome(String diplome) {
        String numtarif = "";

        // Effectuer une requête à la base de données pour récupérer le numtarif associé au diplôme
        String query = "SELECT num_tarif FROM tarif WHERE diplome = ?";
        try (Connection conn = ConnectionDatabase.connect();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, diplome);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                numtarif = resultSet.getString("numtarif");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numtarif;
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
                        String datenais = resultSet.getString("datenais");
                        String contact = resultSet.getString("contact");
                        boolean statut = resultSet.getBoolean("statut");
                        String statutText = statut ? "vivant" : "décédé"; // Modification ici
                        String diplome = resultSet.getString("diplome");
                        String situation = resultSet.getString("situation");
                        String nomconjoint = resultSet.getString("nomconjoint");
                        String prenomconjoint = resultSet.getString("prenomconjoint");

                        Personne personne = new Personne( id, im, numtarif, nom, prenoms, datenais, situation, statutText, contact, diplome, nomconjoint, prenomconjoint);
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
