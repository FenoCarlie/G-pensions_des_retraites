package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Paye {
    private final IntegerProperty id;
    private final StringProperty im;
    private final StringProperty numero_tarif;
    private final StringProperty date;

    public Paye(int id, String im, String numero_tarif, String date) {
        this.id = new SimpleIntegerProperty(id);
        this.im = new SimpleStringProperty(im);
        this.numero_tarif = new SimpleStringProperty(numero_tarif);
        this.date = new SimpleStringProperty(date);
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

    // Getters and setters for numero_tarif
    public String getNumero_tarif() {
        return numero_tarif.get();
    }

    public void setNumero_tarif(String numero_tarif) {
        this.numero_tarif.set(numero_tarif);
    }

    public StringProperty numero_tarifProperty() {
        return numero_tarif;
    }

    // Getters and setters for date
    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }

}