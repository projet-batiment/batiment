package fr.insa.dorgli.projetbat;

public class TypeMur {
    private int id;
    private double epaisseur;
    private double prixUnitaire;
    private String nom;

    public TypeMur(int id, double epaisseur, double prixUnitaire, String nom) {
        this.id = id;
        this.epaisseur = epaisseur;
        this.prixUnitaire = prixUnitaire;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getEpaisseur() {
        return epaisseur;
    }

    public void setEpaisseur(double epaisseur) {
        this.epaisseur = epaisseur;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
