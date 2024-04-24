package fr.insa.dorgli.projetbat;

public class TypeOuvertureMur implements ToStringShort {
	private String nom;
	private String description;
	private double hauteur;
	private double largeur;
	private double prixUnitaire;

	public TypeOuvertureMur(String nom, String description, double hauteur, double largeur, double prixUnitaire) {
		this.nom = nom;
		this.description = description;
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.prixUnitaire = prixUnitaire;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void set surfaceOuv(){
		surfaceOuverture = ( this.largeur * this.hauteur );
		return surfaceOuverture;
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
