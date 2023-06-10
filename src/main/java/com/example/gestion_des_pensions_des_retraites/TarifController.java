package com.example.gestion_des_pensions_des_retraites;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class TarifController {
    @FXML
    private TableView<Tarif> tbvTarifs;

    @FXML
    private TableColumn<Tarif, String> TcNumero;

    @FXML
    private TableColumn<Tarif, String> TcDiplome;

    @FXML
    private TableColumn<Tarif, String> TcCategorie;

    @FXML
    private TableColumn<Tarif, Integer> TcMontant;

    @FXML
    private Button btnAjouter;

    private ObservableList<Tarif> tarifs;

    @FXML
    private void initialize() {
        tarifs = FXCollections.observableArrayList();

        TcNumero.setCellValueFactory(data -> data.getValue().numeroProperty());
        TcDiplome.setCellValueFactory(data -> data.getValue().diplomeProperty());
        TcCategorie.setCellValueFactory(data -> data.getValue().categorieProperty());
        TcMontant.setCellValueFactory(data -> data.getValue().montantProperty().asObject());

        tbvTarifs.setItems(tarifs);

        // Récupérer les données des tarifs depuis la base de données
        loadDataFromDatabase();
    }

    @FXML
    private void ajouterTarif() {
        // Créer une nouvelle fenêtre de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un tarif");

        // Créer des champs de saisie pour les informations du tarif
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
            if (!validateInputs(tfNumero.getText(), tfDiplome.getText(), tfCategorie.getText(), tfMontant.getText())) {
                event.consume(); // Empêcher la fermeture du dialogue
            }
        });

        // Attendre que l'utilisateur appuie sur l'un des boutons
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == validerButtonType) {
            // L'utilisateur a appuyé sur le bouton "Valider", vous pouvez traiter les informations du tarif ici
            String numero = tfNumero.getText();
            String diplome = tfDiplome.getText();
            String categorie = tfCategorie.getText();
            int montant = Integer.parseInt(tfMontant.getText());

            Tarif tarif = new Tarif(numero, diplome, categorie, montant);
            addToDatabase(tarif);

            // Recharger les données depuis la base de données
            loadDataFromDatabase();
        }
    }

    private boolean validateInputs(String numero, String diplome, String categorie, String montant) {
        // Vérifier si les champs sont vides
        if (numero.isEmpty() || diplome.isEmpty() || categorie.isEmpty() || montant.isEmpty()) {
            showErrorMessage("Veuillez remplir tous les champs.");
            return false;
        }

        // Vérifier si le montant est un entier valide
        try {
            int montantValue = Integer.parseInt(montant);
            if (montantValue <= 0) {
                showErrorMessage("Le montant doit être un entier positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Le montant doit être un entier valide.");
            return false;
        }

        return true;
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur de saisie");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addToDatabase(Tarif tarif) {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            Connection conn = connectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");

                // Vérifier si le numéro du tarif existe déjà
                String existingQuery = "SELECT COUNT(*) FROM TARIF WHERE num_tarif = ?";
                PreparedStatement existingStatement = conn.prepareStatement(existingQuery);
                existingStatement.setString(1, tarif.getNumero());
                ResultSet existingResultSet = existingStatement.executeQuery();
                existingResultSet.next();
                int existingCount = existingResultSet.getInt(1);
                existingResultSet.close();
                existingStatement.close();

                if (existingCount > 0) {
                    // Le numéro du tarif existe déjà, afficher un message d'erreur
                    showErrorMessage("Le numéro du tarif existe déjà.");
                } else {
                    // Insérer le nouveau tarif
                    String insertQuery = "INSERT INTO TARIF (num_tarif, diplome, categorie, montant) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                    insertStatement.setString(1, tarif.getNumero());
                    insertStatement.setString(2, tarif.getDiplome());
                    insertStatement.setString(3, tarif.getCategorie());
                    insertStatement.setInt(4, tarif.getMontant());
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
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            Connection conn = connectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");

                // Récupérer les tarifs depuis la base de données
                String query = "SELECT * FROM tarif";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Effacer les données existantes
                tarifs.clear();

                // Parcourir les résultats et ajouter les tarifs à la liste
                while (resultSet.next()) {
                    String numero = resultSet.getString("num_tarif");
                    String diplome = resultSet.getString("diplome");
                    String categorie = resultSet.getString("categorie");
                    int montant = resultSet.getInt("montant");

                    Tarif tarif = new Tarif(numero, diplome, categorie, montant);
                    tarifs.add(tarif);
                }

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