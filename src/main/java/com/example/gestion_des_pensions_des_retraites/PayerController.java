package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
public class PayerController {

    @FXML
    private TableColumn<Payer, Integer> PcId;

    @FXML
    private TableView<Payer> tbvPayers;

    @FXML
    private TableColumn<Payer, String> PcIm;

    @FXML
    private TableColumn<Payer, String> PcNum_tarif;

    @FXML
    private TableColumn<Payer, Date> PcDate;


    @FXML
    private DatePicker dpDate;

    private ObservableList<Payer> payers;

    @FXML
    private void initialize() {
        payers = FXCollections.observableArrayList();

        PcId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        PcIm.setCellValueFactory(data -> data.getValue().imProperty());
        PcNum_tarif.setCellValueFactory(data -> data.getValue().num_tarifProperty());
        PcDate.setCellValueFactory(data -> {
            ObjectProperty<LocalDate> dateProperty = data.getValue().dateProperty();
            LocalDate localDate = dateProperty.get();
            Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);
            return new SimpleObjectProperty<>(date);
        });
        tbvPayers.setItems(payers);

        // Récupérer les données des tarifs depuis la base de données
        loadDataFromDatabase();

        // Gérer le clic droit sur la table
        tbvPayers.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showContextMenu(event);
            }
        });
    }

    private ContextMenu contextMenu; // Déclarez une variable de classe pour stocker le menu contextuel

    private void showContextMenu(MouseEvent event) {
        Payer selectedPayer = tbvPayers.getSelectionModel().getSelectedItem();

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

            contextMenu.show(tbvPayers, event.getScreenX(), event.getScreenY());

            // Gérer l'événement de clic droit pour masquer le menu contextuel
            tbvPayers.setOnMousePressed(mouseEvent -> {
                if (mouseEvent.isSecondaryButtonDown()) {
                    if (contextMenu != null) {
                        contextMenu.hide();
                        contextMenu = null;
                    }
                }
            });
        }
    }

    private void modifierPayer(Payer payer) {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier un paye");

        // Créer des champs de saisie pour les informations du tarif
        TextField tfIm = new TextField(payer.getIm());
        tfIm.setPromptText("IM");
        TextField tfNum_tarif = new TextField(payer.getNum_tarif());
        tfNum_tarif.setPromptText("Numero tarif");
        TextField tfDate = new TextField(payer.getDate().toString());
        tfDate.setPromptText("Date");

        // Créer une disposition pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("IM:"), tfIm);
        gridPane.addRow(1, new Label("Numéro du tarif:"), tfNum_tarif);
        gridPane.addRow(2, new Label("Date:"), dpDate);

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
            if (validateInputs(tfIm.getText(), tfNum_tarif.getText(), dpDate.getValue())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations du tarif ici
            String im = tfIm.getText();
            String num_tarif = tfNum_tarif.getText();
            LocalDate date = dpDate.getValue();

            // Mettre à jour les propriétés du tarif
            payer.setIm(im);
            payer.setNum_tarif(num_tarif);
            payer.setDate(date);

            // Mettre à jour le tarif dans la base de données
            updateDatabase(payer);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }

    private void updateDatabase(Payer payer) {
        Connection conn = ConnectionDatabase.connect();

        try {
            // Préparez une instruction SQL pour mettre à jour la paye
            String updateQuery = "UPDATE payer SET im = ?, num_tarif = ?, date = ? WHERE id = ?";

            // Créez une déclaration préparée en utilisant l'instruction SQL
            PreparedStatement statement = conn.prepareStatement(updateQuery);

            // Définissez les valeurs des paramètres dans la déclaration préparée
            statement.setString(1, payer.getIm());
            statement.setString(2, payer.getNum_tarif());
            statement.setDate(3, java.sql.Date.valueOf(payer.getDate()));
            // Convert LocalDate to java.sql.Date
            statement.setInt(4, payer.getId()); // Assuming getId() returns the ID of the payer object

            // Exécutez la déclaration préparée pour effectuer la mise à jour
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Tarif mis à jour avec succès dans la base de données.");
            } else {
                System.out.println("La mise à jour du tarif a échoué.");
            }

            // Fermez la déclaration et la connexion à la base de données
            statement.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez les exceptions liées à la base de données ici
        }
    }


    private void supprimerPayer (Payer payer) {
        Alert confirmationDialog = new Alert(AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Supprimer la paye");
        confirmationDialog.setHeaderText("Êtes-vous sûr de vouloir supprimer ce paye ?");
        confirmationDialog.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la suppression, vous pouvez supprimer le tarif de la base de données ici
            deleteFromDatabase(payer);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }

    }

    private void deleteFromDatabase(Payer payer) {
        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");

                // Supprimer le tarif
                String deleteQuery = "DELETE FROM payer WHERE im = ?";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                deleteStatement.setString(1, payer.getIm());
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

    @FXML
    private void ajouterPayer() {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter de paye");

        // Créer des champs de saisie pour les informations du paye
        TextField tfIm = new TextField();
        tfIm.setPromptText("IM");
        TextField tfNum_tarif = new TextField();
        tfNum_tarif.setPromptText("Numero tarif");
        DatePicker dpDate = new DatePicker();
        dpDate.setPromptText("Date");

        // Créer une disposition pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("IM:"), tfIm);
        gridPane.addRow(1, new Label("Numéro du tarif:"), tfNum_tarif);
        gridPane.addRow(2, new Label("Date:"), dpDate);

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
            if (validateInputs(tfIm.getText(), tfNum_tarif.getText(), dpDate.getValue())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations du tarif ici
            String im = tfIm.getText();
            String num_tarif = tfNum_tarif.getText();
            LocalDate date = dpDate.getValue();

            Payeradd payer = new Payeradd(im, num_tarif, date);
            addToDatabase(payer);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }

    private boolean validateInputs(String im, String num_tarif, LocalDate date) {
        // Perform validation logic using the provided input parameters (im, num_tarif, date)

        // Example validation logic:
        if (im.isEmpty() || num_tarif.isEmpty() || date == null) {
            // Display an error message or perform some action if the inputs are invalid
            return false;
        }

        // If all validation checks pass, return true
        return true;
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addToDatabase(Payeradd payer) {
        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");

                // Insérer le nouveau tarif
                String insertQuery = "INSERT INTO TARIF (im, num_tarif, date) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                insertStatement.setString(1, payer.getIm());
                insertStatement.setString(2, payer.getNumTarif());
                insertStatement.setDate(3, java.sql.Date.valueOf(payer.getDate()));
                insertStatement.executeUpdate();
                insertStatement.close();


                System.out.println("Le tarif a été ajouté avec succès.");

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
        payers.clear();

        try {
            Connection conn = ConnectionDatabase.connect();

            if (conn != null) {
                // Retrieve payers from the database
                String query = "SELECT * FROM payer";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Add payers to the observable list
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String im = resultSet.getString("im");
                    String num_tarif = resultSet.getString("num_tarif");
                    java.sql.Date date = resultSet.getDate("date");

                    // Convert java.sql.Date to java.time.LocalDate
                    LocalDate localDate = date.toLocalDate();

                    Payer payer = new Payer(id, im, num_tarif, localDate);
                    payers.add(payer);
                }

                // Close resources
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