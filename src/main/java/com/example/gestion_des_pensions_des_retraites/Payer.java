package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class Payer {
    private final IntegerProperty id;
    private final StringProperty im;
    private final StringProperty nom;
    private final StringProperty prenoms;
    private final StringProperty date;
    private final StringProperty num_tarif;
    private final StringProperty montant;
    private final StringProperty mois;
    private final StringProperty annee;

    public Payer(int id, String im, String nom, String prenoms, String num_tarif, String date, String montant) {
        this.id = new SimpleIntegerProperty(id);
        this.im = new SimpleStringProperty(im);
        this.nom = new SimpleStringProperty(nom);
        this.prenoms = new SimpleStringProperty(prenoms);
        this.date = new SimpleStringProperty(date);
        this.num_tarif = new SimpleStringProperty(num_tarif);
        this.montant = new SimpleStringProperty(montant);

        // Convertir la date en LocalDate
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

        // Extraire le mois et l'année de la date
        String mois = String.valueOf(localDate.getMonthValue());
        String annee = String.valueOf(localDate.getYear());

        // Définir le mois et l'année
        this.mois = new SimpleStringProperty(mois);
        this.annee = new SimpleStringProperty(annee);
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

    public StringProperty dateProperty() {
        return date;
    }

    // Getter methods for mois and annee
    public String getMois() {
        int moisValue = Integer.parseInt(mois.get());
        return Month.of(moisValue).getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    public String getAnnee() {
        return annee.get();
    }

    public StringProperty moisProperty() {
        return mois;
    }

    public StringProperty anneeProperty() {
        return annee;
    }
}
