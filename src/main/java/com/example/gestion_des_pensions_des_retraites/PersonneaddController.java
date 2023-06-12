package com.example.gestion_des_pensions_des_retraites;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PersonneaddController {
    @FXML
    private TextField tfIm;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfPrenom;

    @FXML
    private TextField tfDateNais;

    @FXML
    private TextField tfContact;
    @FXML
    private TextField tfStatut;
    @FXML
    private TextField tfDiplome;
    @FXML
    private TextField tfSituation;
    @FXML
    private TextField tfNomConj;
    @FXML
    private TextField tfPrenomConj;

    public Personneadd getPersonne() {
        String im = tfIm.getText();
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String dateNais = tfDateNais.getText();
        String contact = tfContact.getText();
        String statut = tfStatut.getText();
        String diplome = tfDiplome.getText();
        String situation = tfSituation.getText();
        String nomConj = tfNomConj.getText();
        String prenomConj = tfPrenomConj.getText();


        // Créer et retourner un objet Tarif avec les valeurs saisies dans les champs de saisie
        return new Personneadd(im, nom, prenom, dateNais, contact, statut, diplome, situation, nomConj, prenomConj);
    }
}
