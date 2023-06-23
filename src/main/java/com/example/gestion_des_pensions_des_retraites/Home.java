package com.example.gestion_des_pensions_des_retraites;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Home extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    private final StringProperty statut;
    private final IntegerProperty effectif;

    public Home(int effectif, String statut){
        this.statut = new SimpleStringProperty(statut);
        this.effectif = new SimpleIntegerProperty(effectif);
    }

    public String getStatut() {
        return statut.get();
    }

    public StringProperty statutProperty() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    public int getEffectif() {
        return effectif.get();
    }

    public IntegerProperty effectifProperty() {
        return effectif;
    }

    public void setEffectif(int effectif) {
        this.effectif.set(effectif);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);

        // Permet de déplacer la fenêtre en maintenant un bouton de souris enfoncé et en faisant glisser la souris
        root.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}