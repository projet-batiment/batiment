package fr.insa.dorgli.projetbat;

public class TypeOuvertureMur implements ToStringShort {
	private double prixUnitaire;
	private double hauteur;
	private double largeur;
	private String nom;

	public TypeOuvertureMur(double prixUnitaire, double hauteur, double largeur, String nom) {
		this.prixUnitaire = prixUnitaire;
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.nom = nom;
	}

	public double getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(double prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	public double getHauteur() {
		return hauteur;
	}

	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}

	public double getLargeur() {
		return largeur;
	}

	public void setLargeur(double largeur) {
		this.largeur = largeur;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String toString() {
		return "TypeOuvertureMur { prixUnitaire: " + prixUnitaire + ", hauteur: " + hauteur + ", largeur: " + largeur
				+ ", nom: " + nom + " }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + ")";
	}
}
