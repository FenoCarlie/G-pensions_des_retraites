package com.example.gestion_des_pensions_des_retraites;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TarifaddController {
    @FXML
    private TextField TfNumero;

    @FXML
    private TextField TfDiplome;

    @FXML
    private TextField TfCategorie;

    @FXML
    private TextField TfMontant;

    public String getNumero() {
        return TfNumero.getText();
    }

    public String getDiplome() {
        return TfDiplome.getText();
    }

    public String getCategorie() {
        return TfCategorie.getText();
    }

    public String getMontant() {
        return TfMontant.getText();
    }

}
