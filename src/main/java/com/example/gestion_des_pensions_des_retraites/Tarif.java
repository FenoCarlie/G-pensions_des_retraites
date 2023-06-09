
package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class Tarif implements Initializable {
    private SimpleStringProperty numero;
    private SimpleStringProperty diplome;
    private SimpleStringProperty categorie;
    private SimpleStringProperty montant;
    private SimpleStringProperty action;

    public Tarif(String numero, String diplome, String categorie, String montant) {
        this.numero = new SimpleStringProperty(numero);
        this.diplome = new SimpleStringProperty(diplome);
        this.categorie = new SimpleStringProperty(categorie);
        this.montant = new SimpleStringProperty(montant);
    }

    public String getNumero() {
        return numero.get();
    }

    public SimpleStringProperty numeroProperty() {
        return numero;
    }

    public void setNumero(String num_tarif) {
        this.numero.set(num_tarif);
    }

    public String getDiplome() {
        return diplome.get();
    }

    public SimpleStringProperty diplomeProperty() {
        return diplome;
    }

    public void setDiplome(String diplome) {
        this.diplome.set(diplome);
    }

    public String getCategorie() {
        return categorie.get();
    }

    public SimpleStringProperty categorieProperty() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie.set(categorie);
    }

    public String getMontant() {
        return montant.get();
    }

    public SimpleStringProperty montantProperty() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant.set(montant);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialisez les éléments et ajoutez la logique spécifique à la page Tarif
    }
}