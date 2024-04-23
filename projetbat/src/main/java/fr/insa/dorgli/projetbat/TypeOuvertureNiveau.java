package fr.insa.dorgli.projetbat;

public class TypeOuvertureNiveau implements ToStringShort {
	// j'ai pris le parti d'utiliser "largeur" et "hauteur" pour mieux distinguer
	//   les deux directions (largeur et longueurs peuvent prêter à confusion)
	// Si on ne change plus les TypeOuvertureNiveau ni les TypeOuvertureMur,
	//   alors on pourrait même les rassembler en une seule classe puisqu'ils ont les même propriétés
	private String nom;
	private String description;
	private double hauteur;
	private double largeur;
	private double prixUnitaire;

	public TypeOuvertureNiveau(String nom, String description, double hauteur, double largeur, double prixUnitaire) {
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

	public double getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(double prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	public String toString() {
		return "TypeOuvertureNiveau { nom: '" + nom + "'"
		    + ", description: '" + description + "'"
		    + ", hauteur: " + hauteur
		    + ", largeur: " + largeur
		    + ", prixUnitaire: " + prixUnitaire + " }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + " )";
	}
}
