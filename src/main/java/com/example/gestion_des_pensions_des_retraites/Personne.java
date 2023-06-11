package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Personne {
    private final IntegerProperty id;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final StringProperty date_nes;
    private final StringProperty contact;
    private final StringProperty situation;
    private final StringProperty nom_conjoint;
    private final StringProperty prenom_conjoint;

    public Personne(int id, String nom, String prenom, String date_nes, String situation, String contact, String nom_conjoint, String prenom_conjoint) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.date_nes = new SimpleStringProperty(date_nes);
        this.situation = new SimpleStringProperty(situation);
        this.contact = new SimpleStringProperty(contact);
        this.nom_conjoint = new SimpleStringProperty(nom_conjoint);
        this.prenom_conjoint = new SimpleStringProperty(prenom_conjoint);
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

    // Getters and setters for contact
    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public StringProperty contact() {
        return contact;
    }

    // Getters and setters for nom
    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nom() {
        return nom;
    }

    // Getters and setters for prenom
    public String getPrenom() {
        return prenom.get();
    }

    public void Prenom(String prenom) {
        this.prenom.set(prenom);
    }

    public StringProperty prenom() {
        return prenom;
    }

    // Getters and setters for situation
    public String getSituation() {
        return situation.get();
    }

    public void Situation(String situation) {
        this.situation.set(situation);
    }

    public StringProperty situation() {
        return situation;
    }

    // Getters and setters for date_nes
    public String getDate_nes() {
        return date_nes.get();
    }

    public void Date_nes(String date_nes) {
        this.date_nes.set(date_nes);
    }

    public StringProperty date_nes() {
        return date_nes;
    }

    // Getters and setters for Nom_conjoint
    public String getNom_conjoint() {
        return nom_conjoint.get();
    }

    public void setNom_conjoint(String diplome) {
        this.nom_conjoint.set(diplome);
    }

    public StringProperty nom_conjointProperty() {
        return nom_conjoint;
    }

    // Getters and setters for Prenom_conjoint
    public String getPrenom_conjoint() {
        return prenom_conjoint.get();
    }

    public void setPrenom_conjoint(String prenom_conjoint) {
        this.prenom_conjoint.set(prenom_conjoint);
    }

    public StringProperty prenom_conjointProperty() {
        return prenom_conjoint;
    }
}