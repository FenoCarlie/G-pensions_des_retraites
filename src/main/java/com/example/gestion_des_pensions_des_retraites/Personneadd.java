package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;

public class Personneadd {
    private final StringProperty im;
    private final StringProperty nom;
    private final StringProperty prenoms;
    private final StringProperty datenais;
    private final StringProperty contact;
    private final StringProperty statut;
    private final StringProperty diplome;
    private final StringProperty situation;
    private final StringProperty nomconjoint;
    private final StringProperty prenomconjoint;

    public Personneadd(String im, String nom, String prenoms, String situation, String datenais, String statut, String contact, String diplome, String nomconjoint, String prenomconjoint) {
        this.im = new SimpleStringProperty(im);
        this.nom = new SimpleStringProperty(nom);
        this.prenoms = new SimpleStringProperty(prenoms);
        this.datenais = new SimpleStringProperty(datenais);
        this.statut = new SimpleStringProperty(statut);
        this.situation = new SimpleStringProperty(situation);
        this.contact = new SimpleStringProperty(contact);
        this.diplome = new SimpleStringProperty(diplome);
        this.nomconjoint = new SimpleStringProperty(nomconjoint);
        this.prenomconjoint = new SimpleStringProperty(prenomconjoint);
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
    public String getSituation() {
        return situation.get();
    }

    public void setSituation(String situation) {
        this.situation.set(situation);
    }

    public StringProperty situationProperty() {
        return situation;
    }

    // Getters and setters for datenais
    public String getDatenais() {
        return datenais.get();
    }

    public void setDatenais(String datenais) {
        this.datenais.set(datenais);
    }

    public StringProperty datenaisProperty() {
        return datenais;
    }

    // Getters and setters for nomconjoint
    public String getNomconjoint() {
        return nomconjoint.get();
    }

    public void setNomconjoint(String nomconjoint) {
        this.nomconjoint.set(nomconjoint);
    }

    public StringProperty nomconjointProperty() {
        return nomconjoint;
    }

    // Getters and setters for prenomconjoint
    public String getPrenomconjoint() {
        return prenomconjoint.get();
    }

    public void setPrenomconjoint(String prenomconjoint) {
        this.prenomconjoint.set(prenomconjoint);
    }

    public StringProperty prenomconjointProperty() {
        return prenomconjoint;
    }
}
