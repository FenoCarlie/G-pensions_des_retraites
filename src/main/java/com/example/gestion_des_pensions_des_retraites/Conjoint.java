package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Conjoint {
    private final IntegerProperty id;
    private final StringProperty numero_pension;
    private final StringProperty nom_conjoint;
    private final StringProperty prenom_conjoint;
    private final IntegerProperty montant;

    public Conjoint(int id, String numero_pension, String nom_conjoint, String prenom_conjoint, int montant) {
        this.id = new SimpleIntegerProperty(id);
        this.numero_pension = new SimpleStringProperty(numero_pension);
        this.nom_conjoint = new SimpleStringProperty(nom_conjoint);
        this.prenom_conjoint = new SimpleStringProperty(prenom_conjoint);
        this.montant = new SimpleIntegerProperty(montant);
    }

    // Getters and setters for id
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Getters and setters for numero_pension
    public String getNumero_pension() {
        return numero_pension.get();
    }

    public void setNumero_pension(String numero) {
        this.numero_pension.set(numero);
    }

    public StringProperty numero_pensionProperty() {
        return numero_pension;
    }

    // Getters and setters for Nom_conjoint
    public String getNom_conjoint() {
        return nom_conjoint.get();
    }

    public void setNom_conjoint(String diplome) { this.nom_conjoint.set(diplome); }

    public StringProperty nom_conjointProperty() {
        return nom_conjoint;
    }

    // Getters and setters for categorie
    public String getPrenom_conjoint() {
        return prenom_conjoint.get();
    }

    public void setPrenom_conjoint(String categorie) {
        this.prenom_conjoint.set(categorie);
    }

    public StringProperty prenom_conjointProperty() {
        return prenom_conjoint;
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