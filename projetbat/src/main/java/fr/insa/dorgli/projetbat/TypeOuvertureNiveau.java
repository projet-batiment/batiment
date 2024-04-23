package fr.insa.dorgli.projetbat;

public class TypeOuvertureNiveau implements ToStringShort {
	// j'ai pris le parti d'utiliser "largeur" et "hauteur" pour mieux distinguer
	//   les deux directions (largeur et longueurs peuvent prêter à confusion)
	public double prixOuverture;
	private double largeur;
	private double hauteur;
	private String nom;

	public TypeOuvertureNiveau(double prixOuverture, double largeur, double hauteur, String nom) {
		this.prixOuverture = prixOuverture;
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.nom = nom;
	}

	public double getPrixOuverture() {
		return prixUnitaire;
	}

	public void setPrixOuverture(double prixOuverture) {
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
		return "TypeOuvertureNiveau { prixUnitaire: " + prixOuverture + ", largeur: " + largeur + ", hauteur: " + hauteur + ", nom: " + nom + " }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + ")";
	}
}
