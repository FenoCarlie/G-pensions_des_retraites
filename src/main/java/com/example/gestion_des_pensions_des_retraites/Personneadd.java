package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Personneadd {
    private final StringProperty im;
    private final StringProperty nom;
    private final StringProperty prenom;
    private final StringProperty date_nais;
    private final StringProperty contact;
    private final StringProperty statut;
    private final StringProperty diplome;
    private final StringProperty situation;
    private final StringProperty nom_conjoint;
    private final StringProperty prenom_conjoint;

    public Personneadd(String im, String nom, String prenom, String situation, String date_nais, String statut, String contact, String diplome, String nom_conjoint, String prenom_conjoint) {
        this.im = new SimpleStringProperty(im);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.date_nais = new SimpleStringProperty(date_nais);
        this.statut = new SimpleStringProperty(statut);
        this.situation = new SimpleStringProperty(situation);
        this.contact = new SimpleStringProperty(contact);
        this.diplome = new SimpleStringProperty(diplome);
        this.nom_conjoint = new SimpleStringProperty(nom_conjoint);
        this.prenom_conjoint = new SimpleStringProperty(prenom_conjoint);
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

    // Getters and setters for statut
    public String getStatut() {
        return statut.get();
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    public StringProperty statutProperty() {
        return statut;
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

    // Getters and setters for contact
    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public StringProperty contactProperty() {
        return contact;
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

    // Getters and setters for prenom
    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    // Getters and setters for situation
    public String getSituation() {
        return situation.get();
    }

    public void setSituation(String situation) {
        this.situation.set(situation);
    }

    public StringProperty situationProperty() {
        return situation;
    }

    // Getters and setters for date_nais
    public String getDate_nais() {
        return date_nais.get();
    }

    public void setDate_nais(String date_nais) {
        this.date_nais.set(date_nais);
    }

    public StringProperty date_naisProperty() {
        return date_nais;
    }

    // Getters and setters for nom_conjoint
    public String getNom_conjoint() {
        return nom_conjoint.get();
    }

    public void setNom_conjoint(String nom_conjoint) {
        this.nom_conjoint.set(nom_conjoint);
    }

    public StringProperty nom_conjointProperty() {
        return nom_conjoint;
    }

    // Getters and setters for prenom_conjoint
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
