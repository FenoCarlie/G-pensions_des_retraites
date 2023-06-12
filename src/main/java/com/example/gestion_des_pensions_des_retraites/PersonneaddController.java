package com.example.gestion_des_pensions_des_retraites;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PersonneaddController {
    @FXML
    private TextField tfNumero;

    @FXML
    private TextField tfDiplome;

    @FXML
    private TextField tfCategorie;

    @FXML
    private TextField tfMontant;

    public Tarifadd getTarif() {
        String numero = tfNumero.getText();
        String diplome = tfDiplome.getText();
        String categorie = tfCategorie.getText();
        int montant = 0; // Valeur par défaut en cas de champ de saisie vide

        if (!tfMontant.getText().isEmpty()) {
            montant = Integer.parseInt(tfMontant.getText());
        }

        // Créer et retourner un objet Tarif avec les valeurs saisies dans les champs de saisie
        return new Tarifadd(numero, diplome, categorie, montant);
    }
}
