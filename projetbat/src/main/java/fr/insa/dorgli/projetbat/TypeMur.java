package fr.insa.dorgli.projetbat;

public class TypeMur implements ToStringShort {
	private double epaisseur;
	private double prixUnitaire;
	private String nom;

	public TypeMur(double epaisseur, double prixUnitaire, String nom) {
		this.epaisseur = epaisseur;
		this.prixUnitaire = prixUnitaire;
		this.nom = nom;
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

	public String toString() {
		return "TypeMur { epaisseur: " + epaisseur + ", prixUnitaire: " + prixUnitaire + ", nom: " + nom + " }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + ")";
	}
}
