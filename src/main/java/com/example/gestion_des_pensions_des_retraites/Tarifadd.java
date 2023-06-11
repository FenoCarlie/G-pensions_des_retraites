package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tarifadd {
    private final StringProperty numero;
    private final StringProperty diplome;
    private final StringProperty categorie;
    private final IntegerProperty montant;

    public Tarifadd(String numero, String diplome, String categorie, int montant) {
        this.numero = new SimpleStringProperty(numero);
        this.diplome = new SimpleStringProperty(diplome);
        this.categorie = new SimpleStringProperty(categorie);
        this.montant = new SimpleIntegerProperty(montant);
    }

    // Getters and setters for numero
    public String getNumero() {
        return numero.get();
    }

    public void setNumero(String numero) {
        this.numero.set(numero);
    }

    public StringProperty numeroProperty() {
        return numero;
    }

    // Getters and setters for diplome
    public String getDiplome() {
        return diplome.get();
    }

    public void setDiplome(String diplome) {
        this.diplome.set(diplome);
    }

    public StringProperty diplomeProperty() {
        return diplome;
    }

    // Getters and setters for categorie
    public String getCategorie() {
        return categorie.get();
    }

    public void setCategorie(String categorie) {
        this.categorie.set(categorie);
    }

    public StringProperty categorieProperty() {
        return categorie;
    }

    // Getters and setters for montant
    public int getMontant() {
        return montant.get();
    }

    public void setMontant(int montant) {
        this.montant.set(montant);
    }

    public IntegerProperty montantProperty() {
        return montant;
    }
}
