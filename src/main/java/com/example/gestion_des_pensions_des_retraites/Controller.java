package com.example.gestion_des_pensions_des_retraites;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class  Controller implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @FXML
    private BorderPane bp;

    @FXML
    private AnchorPane ap;

    @FXML
    private TableColumn<Home, Integer> HcEffectif;

    @FXML
    private TableColumn<Home, String> HcStatut;

    @FXML
    private TableView<Home> tbvHome;

    private ObservableList<Home> homes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homes = FXCollections.observableArrayList();

        HcStatut.setCellValueFactory(data -> data.getValue().statutProperty());
        HcEffectif.setCellValueFactory(data -> data.getValue().effectifProperty().asObject());
        tbvHome.setItems(homes);

        afficherPersonnesParStatut();
    }

    @FXML
    private void home(MouseEvent event){
        bp.setCenter(ap);
    }

    @FXML
    private void tarif(MouseEvent event){
        loadPage("tarif");
    }

    @FXML
    private void personne(MouseEvent event){
        loadPage("personne");
    }

    @FXML
    private void paye(MouseEvent event){
        loadPage("paye");
    }

    @FXML
    private void conjoint(MouseEvent event){
        loadPage("conjoint");
    }

    private void afficherPersonnesParStatut() {
        // Effacer les données existantes
        homes.clear();

        try (Connection conn = ConnectionDatabase.connect()) {
            if (conn != null) {
                String query = "SELECT statut, COUNT(*) AS effectif_total FROM personne GROUP BY statut";
                try (PreparedStatement statement = conn.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int effectif = resultSet.getInt("effectif_total");
                        boolean statut = resultSet.getBoolean("statut");
                        String statutText = statut ? "vivant" : "décédé"; // Modification ici

                        Home home = new Home(effectif, statutText);
                        homes.add(home);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPage(String page){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(page + ".fxml")));
        } catch (IOException ex) {
            logger.error("Exception occurred: " + ex.getMessage(), ex);
        }
        bp.setCenter(root);
    }
}
