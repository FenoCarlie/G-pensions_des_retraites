package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Personnepayer {
    private final StringProperty im;
    private final StringProperty nom;
    private final StringProperty prenoms;
    private final StringProperty date;
    private String numtarif;

    private final StringProperty diplome;

    public Personnepayer(String im, String nom, String prenoms, String diplome, String datenais) {
        this.im = new SimpleStringProperty(im);
        this.nom = new SimpleStringProperty(nom);
        this.prenoms = new SimpleStringProperty(prenoms);
        this.diplome = new SimpleStringProperty(diplome);
        this.date = new SimpleStringProperty(datenais);
    }

    public String getNumtarif() {
        return numtarif;
    }

    public void setNumtarif(String numtarif) {
        this.numtarif = numtarif;
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


    // Getters and setters for im
    public String getIm() {
        return im.get();
    }

    public void setIm(String im) {
        this.im.set(im);
    }

    public StringProperty imProperty() {
        return im;
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

    // Getters and setters for situation

    // Getters and setters for date
    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }
}
