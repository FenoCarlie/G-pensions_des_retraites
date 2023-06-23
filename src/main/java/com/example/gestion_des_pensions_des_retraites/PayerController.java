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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Optional;
public class PayerController {

    @FXML
    private TableColumn<Payer, Integer> PcId;

    @FXML
    private TableColumn<Payer, String> PcMontant;

    @FXML
    private TableView<Payer> tbvPayers;

    @FXML
    private TableColumn<Payer, String> PcIm;

    @FXML
    private TableColumn<Payer, String> PcNom;

    @FXML
    private TableColumn<Payer, String> PcPrenoms;

    @FXML
    private TableColumn<Payer, String> PcNum_tarif;

    @FXML
    private TableColumn<Payer, String> PcDate;

    private ObservableList<Payer> payers;


    @FXML
    private DatePicker dpDebut;

    @FXML
    private DatePicker dpFin;

    @FXML
    private Button searchButton;


    @FXML
    private void initialize() {
        payers = FXCollections.observableArrayList();

        PcId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        PcIm.setCellValueFactory(data -> data.getValue().imProperty());
        PcNom.setCellValueFactory(data -> data.getValue().nomProperty());
        PcPrenoms.setCellValueFactory(data -> data.getValue().prenomsProperty());
        PcNum_tarif.setCellValueFactory(data -> data.getValue().num_tarifProperty());
        PcDate.setCellValueFactory(data -> data.getValue().dateProperty());
        PcMontant.setCellValueFactory(data -> data.getValue().montantProperty());
        tbvPayers.setItems(payers);

        // Récupérer les données des tarifs depuis la base de données
        loadDataFromDatabase();

        // Gérer le clic droit sur la table
        tbvPayers.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                showContextMenu(event);
            }
        });
        searchButton.setOnAction(event -> {
            if (dpDebut.getValue() == null || dpFin.getValue() == null) {
                loadDataFromDatabase();
            } else {
                rechercheEntreDeuxDate();
            }
        });
    }

    private void rechercheEntreDeuxDate() {
        LocalDate debut = dpDebut.getValue();
        LocalDate fin = dpFin.getValue();

        if (debut != null && fin != null) {
            // Convertir les dates en format compatible avec la base de données (par exemple, au format "yyyy-MM-dd")
            String dateDebut = debut.toString();
            String dateFin = fin.toString();

            // Exécuter une requête SQL pour récupérer les pensions payées entre les deux dates
            // Utilisez la clause WHERE pour filtrer les résultats en fonction des dates
            String query = "SELECT * FROM payer WHERE date >= ? AND date <= ?";
            try (Connection conn = ConnectionDatabase.connect();
                 PreparedStatement statement = conn.prepareStatement(query)) {

                statement.setString(1, dateDebut);
                statement.setString(2, dateFin);

                ResultSet resultSet = statement.executeQuery();

                // Traiter les résultats de la requête et afficher les pensions payées
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String im = resultSet.getString("im");
                    String num_tarif = resultSet.getString("num_tarif");
                    String date = resultSet.getString("date");
                    String montant = resultSet.getString("montant");
                    String nom = resultSet.getString("nom");
                    String prenoms = resultSet.getString("prenoms");

                    Payer payer = new Payer(id, im, num_tarif, date, montant, nom, prenoms);
                    payers.add(payer);
                }

                // Fermer le ResultSet et la connexion à la base de données
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showErrorAlert("Erreur de saisie", "Veuillez sélectionner une date de début et une date de fin.");
        }
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
        tfNum_tarif.setPromptText("Numero du tarf");
        TextField tfDate = new TextField(payer.getDate());
        tfDate.setPromptText("date");

        // Créer une disposition pour organiser les éléments
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("IM:"), tfIm);
        gridPane.addRow(1, new Label("Numero tarif"), tfNum_tarif);
        gridPane.addRow(2, new Label("Date:"), tfDate);

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
            if (validateInputs(tfIm.getText(),tfNum_tarif.getText(), tfDate.getText())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations du tarif ici
            String im = tfIm.getText();
            String num_tarif = tfNum_tarif.getText();
            String date = tfDate.getText();

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
            String updateQuery = "UPDATE payer SET im = ?, num_tarif = ?, date = ?::date WHERE id = ?";

            // Créez une déclaration préparée en utilisant l'instruction SQL
            PreparedStatement statement = conn.prepareStatement(updateQuery);

            // Définissez les valeurs des paramètres dans la déclaration préparée
            statement.setString(1, payer.getIm());
            statement.setString(2, payer.getNum_tarif());
            statement.setString(3, payer.getDate());
            statement.setInt(4, payer.getId());

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
                String deleteQuery = "DELETE FROM payer WHERE id = ?";
                PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, payer.getId());
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


    private boolean validateInputs(String im, String num_tarif, String date) {
        boolean hasError = false;

        if (im.isEmpty()) {
            showErrorAlert("Erreur de validation", "Le champ 'im' est vide.");
            hasError = true;
        } else if (num_tarif.isEmpty()) {
            showErrorAlert("Erreur de validation", "Le champ 'num_tarif' est vide.");
            hasError = true;
        } else if (date == null) {
            showErrorAlert("Erreur de validation", "Le champ 'date' est vide.");
            hasError = true;
        } else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            showErrorAlert("Erreur de validation", "Le champ 'date' doit être au format 'yyyy-mm-dd'.");
            hasError = true;
        } else {
            try (Connection conn = ConnectionDatabase.connect()) {
                if (conn != null) {
                    // Vérifier si la valeur de "im" existe dans la table "personne"
                    String selectPersonneQuery = "SELECT COUNT(*) FROM personne WHERE im = ?";
                    try (PreparedStatement selectPersonneStatement = conn.prepareStatement(selectPersonneQuery)) {
                        selectPersonneStatement.setString(1, im);
                        try (ResultSet personneResultSet = selectPersonneStatement.executeQuery()) {
                            personneResultSet.next();
                            int personneRowCount = personneResultSet.getInt(1);
                            if (personneRowCount == 0) {
                                showErrorAlert("Erreur de validation", "La valeur de 'im' n'existe pas dans la table 'personne'.");
                                hasError = true;
                            }
                        }
                    }

                    // Vérifier si la valeur de "num_tarif" existe dans la table correspondante
                    String selectNumTarifQuery = "SELECT COUNT(*) FROM tarif WHERE num_tarif = ?";
                    try (PreparedStatement selectNumTarifStatement = conn.prepareStatement(selectNumTarifQuery)) {
                        selectNumTarifStatement.setString(1, num_tarif);
                        try (ResultSet numTarifResultSet = selectNumTarifStatement.executeQuery()) {
                            numTarifResultSet.next();
                            int numTarifRowCount = numTarifResultSet.getInt(1);
                            if (numTarifRowCount == 0) {
                                showErrorAlert("Erreur de validation", "La valeur de 'num_tarif' n'existe pas dans la table correspondante.");
                                hasError = true;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return hasError;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }


    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadDataFromDatabase() {
        // Effacer les données existantes
        payers.clear();

        try (Connection conn = ConnectionDatabase.connect()) {
            if (conn != null) {
                // Récupérer les payeurs depuis la base de données avec le nom et le prénom de la table personne liée
                String query = "SELECT payer.id AS pa_id, payer.date AS pa_date,personne.im AS pe_im, tarif.num_tarif AS t_num_tarif, personne.nom AS pe_nom, personne.prenoms AS pe_prenoms, tarif.montant AS t_montant FROM payer JOIN tarif ON payer.num_tarif = tarif.num_tarif JOIN personne ON payer.im = personne.im;";
                try (PreparedStatement statement = conn.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {

                    // Ajouter les payeurs à la liste observable
                    while (resultSet.next()) {
                        int id = resultSet.getInt("pa_id");
                        int montant = resultSet.getInt("t_montant");
                        String date = resultSet.getString("pa_date");
                        String nom = resultSet.getString("pe_nom");
                        String prenoms = resultSet.getString("pe_prenoms");
                        String num_tarif = resultSet.getString("t_num_tarif");
                        String im = resultSet.getString("pe_im");

                        // Formater le montant avec des séparateurs de milliers et " Ar" à la fin
                        NumberFormat numberFormat = new DecimalFormat("#,###");
                        String formattedMontant = numberFormat.format(montant) + " Ar";

                        Payer payer = new Payer(id, im, nom, prenoms, num_tarif, date, formattedMontant);
                        payers.add(payer);
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
}
