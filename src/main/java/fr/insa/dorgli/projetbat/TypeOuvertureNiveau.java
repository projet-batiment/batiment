package main.java.fr.insa.dorgli.projetbat;

public class TypeOuvertureNiveau implements ToStringShort {
    private double prixUnitaire;
    private double x;
    private double y;
    private String nom;

    public TypeOuvertureNiveau(double prixUnitaire, double x, double y, String nom) {
        this.prixUnitaire = prixUnitaire;
        this.x = x;
        this.y = y;
        this.nom = nom;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String toString() {
        return "TypeOuvertureNiveau { prixUnitaire: " + prixUnitaire + ", x: " + x + ", y: " + y + ", nom: " + nom + " }";
    }

    public String toStringShort() {
        // TODO -> toStringShort -> afficher l'ID
        return "( #" + "TODO" + ")";
    }
}