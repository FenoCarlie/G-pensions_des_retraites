package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

public class Payeradd {
    private final StringProperty im;
    private final StringProperty num_tarif;
    private final ObjectProperty<LocalDate> date;

    public Payeradd(String im, String num_tarif, LocalDate date) {
        this.im = new SimpleStringProperty(im);
        this.num_tarif = new SimpleStringProperty(num_tarif);
        this.date = new SimpleObjectProperty<>(date);
    }

    // Getter for im property
    public String getIm() {
        return im.get();
    }

    // Setter for im property
    public void setIm(String newIm) {
        im.set(newIm);
    }

    // Getter for num_tarif property
    public String getNumTarif() {
        return num_tarif.get();
    }

    // Setter for num_tarif property
    public void setNumTarif(String newNumTarif) {
        num_tarif.set(newNumTarif);
    }


    // Getter for date property

    public LocalDate getDate() {
        return date.get();
    }

    // Setter for date property
    public void setDate(LocalDate newDate) {
        date.set(newDate);
    }

    // Getter methods for the StringProperties
    public StringProperty imProperty() {
        return im;
    }

    public StringProperty numTarifProperty() {
        return num_tarif;
    }

    // Getter method for the ObjectProperty
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }
}
