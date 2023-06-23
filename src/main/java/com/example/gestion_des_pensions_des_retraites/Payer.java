package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.*;

public class Payer {
    private final IntegerProperty id;
    private final StringProperty im;
    private final StringProperty nom;
    private final StringProperty prenoms;
    private final StringProperty date;
    private final StringProperty num_tarif;
    private final StringProperty montant;

    public Payer(int id, String im, String nom, String prenoms, String num_tarif, String date, String montant) {
        this.id = new SimpleIntegerProperty(id);
        this.im = new SimpleStringProperty(im);
        this.nom = new SimpleStringProperty(nom);
        this.prenoms = new SimpleStringProperty(prenoms);
        this.date = new SimpleStringProperty(date);
        this.num_tarif = new SimpleStringProperty(num_tarif);
        this.montant = new SimpleStringProperty(montant);
    }

    // Getter for num_tarif property
    public String getNum_tarif() {
        return num_tarif.get();
    }

    // Setter for num_tarif property
    public void setNum_tarif(String num_tarif) {
        this.num_tarif.set(num_tarif);
    }

    public StringProperty num_tarifProperty() {
        return num_tarif;
    }

    // Getters and setters for montant
    public String getMontant() {
        return montant.get();
    }

    public void setMontant(String montant) {
        this.montant.set(montant);
    }

    public StringProperty montantProperty() {
        return montant;
    }

    // Getters and setters for nom
    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    // Getters and setters for prenoms
    public String getPrenoms() {
        return prenoms.get();
    }

    public void setPrenoms(String prenoms) {
        this.prenoms.set(prenoms);
    }

    public StringProperty prenomsProperty() {
        return prenoms;
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

    // Getter for im property
    public String getIm() {
        return im.get();
    }

    // Setter for im property
    public void setIm(String im) {
        this.im.set(im);
    }

    public String getDate() {
        return date.get();
    }

    // Setter for date property
    public void setDate(String date) {
        this.date.set(date);
    }

    // Getter methods for the StringProperties
    public StringProperty imProperty() {
        return im;
    }

    // Getter method for the ObjectProperty
    public StringProperty dateProperty() {
        return date;
    }
}
