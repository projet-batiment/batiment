package main.java.fr.insa.dorgli.projetbat;

public class TypeOuvertureNiveau implements ToStringShort {
    // j'ai pris le parti d'utiliser "largeur" et "hauteur" pour mieux distinguer
    //   les deux directions (largeur et longueurs peuvent prêter à confusion)
    private double prixUnitaire;
    private double largeur;
    private double hauteur;
    private String nom;

    public TypeOuvertureNiveau(double prixUnitaire, double largeur, double hauteur, String nom) {
        this.prixUnitaire = prixUnitaire;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.nom = nom;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String toString() {
        return "TypeOuvertureNiveau { prixUnitaire: " + prixUnitaire + ", largeur: " + largeur + ", hauteur: " + hauteur + ", nom: " + nom + " }";
    }

    public String toStringShort() {
        // TODO -> toStringShort -> afficher l'ID
        return "( #" + "TODO" + ")";
    }
}
