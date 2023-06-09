package com.example.gestion_des_pensions_des_retraites;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
    private TableColumn<Tarif, String> TcMontant;

    @FXML
    private Button btnAjouter;

    @FXML
    private void initialize() {
        TcNumero.setCellValueFactory(data -> data.getValue().numeroProperty());
        TcDiplome.setCellValueFactory(data -> data.getValue().diplomeProperty());
        TcCategorie.setCellValueFactory(data -> data.getValue().categorieProperty());
        TcMontant.setCellValueFactory(data -> data.getValue().montantProperty());

        // Récupérer les données des tarifs depuis la base de données
        loadDataFromDatabase();
    }

    @FXML
    private void ajouterTarif() {

    }

    private void loadDataFromDatabase() {
        try {
            ConnectionDatabase connectionDatabase = new ConnectionDatabase();
            Connection conn = connectionDatabase.connect();

            if (conn != null) {
                System.out.println("La connexion à la base de données a été établie avec succès.");

                String query = "SELECT num_tarif, diplome, categorie, montant FROM tarif";
                PreparedStatement statement = conn.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Vider la liste des éléments de la TableView
                tbvTarifs.getItems().clear();

                // Parcourir le ResultSet et créer des objets Tarif à partir des données
                while (resultSet.next()) {
                    String numero = resultSet.getString("num_tarif");
                    String diplome = resultSet.getString("diplome");
                    String categorie = resultSet.getString("categorie");
                    String montant = resultSet.getString("montant");

                    Tarif tarif = new Tarif(numero, diplome, categorie, montant);
                    tbvTarifs.getItems().add(tarif);
                }

                resultSet.close();
                statement.close();
                conn.close();
            } else {
                System.out.println("Échec de la connexion à la base de données.");

                // Afficher un pop-up d'erreur
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur de connexion");
                alert.setHeaderText("Échec de la connexion à la base de données");
                alert.setContentText("Veuillez vérifier vos paramètres de connexion.");

                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();

            // Afficher un pop-up d'erreur avec le message d'exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText("Une erreur s'est produite lors de la connexion à la base de données");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }
}