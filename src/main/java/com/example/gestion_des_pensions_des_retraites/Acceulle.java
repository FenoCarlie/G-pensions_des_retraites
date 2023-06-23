package com.example.gestion_des_pensions_des_retraites;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Acceulle {
    private final StringProperty statut;
    private final IntegerProperty effectif;

    public Acceulle(int effectif, String statut){
        this.statut = new SimpleStringProperty(statut);
        this.effectif = new SimpleIntegerProperty(effectif);
    }

    public String getStatut() {
        return statut.get();
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    public int getEffectif() {
        return effectif.get();
    }

    public void setEffectif(int effectif) {
        this.effectif.set(effectif);
    }

    public StringProperty statutProperty() {
        return statut;
    }

    public IntegerProperty effectifProperty() {
        return effectif;
    }
}
