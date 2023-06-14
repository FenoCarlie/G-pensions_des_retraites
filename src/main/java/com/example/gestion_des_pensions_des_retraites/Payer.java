package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Payer {
    private final IntegerProperty id;
    private final StringProperty im;
    private final StringProperty num_tarif;
    private final StringProperty date;

    public Payer(int id, String im, String num_tarif, String date) {
        this.id = new SimpleIntegerProperty(id);
        this.im = new SimpleStringProperty(im);
        this.num_tarif = new SimpleStringProperty(num_tarif);
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

    // Getter for im property
    public String getIm() {
        return im.get();
    }

    // Setter for im property
    public void setIm(String im) {
        this.im.set(im);
    }

    // Getter for num_tarif property
    public String getNum_tarif() {
        return num_tarif.get();
    }

    // Setter for num_tarif property
    public void setNum_tarif(String num_tarif) {
        this.num_tarif.set(num_tarif);
    }

    // Getter for date property
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

    public StringProperty num_tarifProperty() {
        return num_tarif;
    }

    // Getter method for the ObjectProperty
    public StringProperty dateProperty() {
        return date;
    }
}
