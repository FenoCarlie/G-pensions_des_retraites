package com.example.gestion_des_pensions_des_retraites;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @FXML
    private BorderPane bp;

    @FXML
    private AnchorPane ap;

    @FXML
    private TableView<Acceulle> tbvHome;

    @FXML
    private TableColumn<Acceulle, Integer> HcEffectif;

    @FXML
    private TableColumn<Acceulle, String> HcStatut;

    @FXML
    private BarChart<String, Number> histogram;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label labelEffectifTotal;

    private ObservableList<Acceulle> homes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homes = FXCollections.observableArrayList();

        HcStatut.setCellValueFactory(data -> data.getValue().statutProperty());
        HcEffectif.setCellValueFactory(data -> data.getValue().effectifProperty().asObject());
        tbvHome.setItems(homes);

        afficherPersonnesParStatut();

        // Create and display the histogram
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Effectif par statut");
        for (Acceulle home : homes) {
            series.getData().add(new XYChart.Data<>(home.getStatut(), home.getEffectif()));
        }

        histogram.getData().add(series);

        updateEffectifTotalLabel();
    }

    @FXML
    private void home(MouseEvent event) {
        bp.setCenter(ap);
        afficherPersonnesParStatut();
        updateEffectifTotalLabel();
    }

    @FXML
    private void tarif(MouseEvent event) {
        loadPage("tarif");
    }

    @FXML
    private void personne(MouseEvent event) {
        loadPage("personne");
    }

    @FXML
    private void paye(MouseEvent event) {
        loadPage("paye");
    }

    @FXML
    private void conjoint(MouseEvent event) {
        loadPage("conjoint");
    }

    // Other variables and methods

    private int getEffectifTotal() {
        int effectifTotal = 0;
        try (Connection conn = ConnectionDatabase.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM personne")) {
            if (rs.next()) {
                effectifTotal = rs.getInt("total");
                //System.out.println(rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return effectifTotal;
    }

    private void updateEffectifTotalLabel() {
        int effectifTotal = getEffectifTotal();
        labelEffectifTotal.setText(String.valueOf(effectifTotal));
    }

    private void afficherPersonnesParStatut() {
        homes.clear();
        try (Connection conn = ConnectionDatabase.connect()) {
            if (conn != null) {
                String query = "SELECT statut, COUNT(*) AS effectif_total FROM personne GROUP BY statut";
                try (PreparedStatement statement = conn.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int effectif = resultSet.getInt("effectif_total");
                        boolean statut = resultSet.getBoolean("statut");
                        String statutText = statut ? "Vivant" : "Décédé";

                        Acceulle home = new Acceulle(effectif, statutText);
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

    private void loadPage(String page) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(page + ".fxml"));
        } catch (IOException ex) {
            logger.error("Exception occurred: " + ex.getMessage(), ex);
        }
        if (root != null) {
            bp.setCenter(root);
        }
    }
}
