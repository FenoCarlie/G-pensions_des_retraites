package com.example.gestion_des_pensions_des_retraites;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class  Controller implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @FXML
    private BorderPane bp;

    @FXML
    private AnchorPane ap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
