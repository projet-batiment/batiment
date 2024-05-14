package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.Deserialize;
import fr.insa.dorgli.projetbat.ToStringShort;

public class TypeOuvertureMur implements ToStringShort {
	private String nom;
	private String description;
	private double hauteur;
	private double largeur;
	private double prixOuverture;

	public TypeOuvertureMur(String nom, String description, double hauteur, double largeur, double prixOuverture) {
		this.nom = nom;
		this.description = description;
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.prixOuverture = prixOuverture;
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

	public double getPrixOuverture() {
		return prixOuverture;
	}

	public void setPrixOuverture(double prixOuverture) {
		this.prixOuverture = prixOuverture;
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

	public String toString() {
		return "TypeOuvertureMur { nom: '" + nom + "'"
		    + ", description: '" + description + "'"
		    + ", hauteur: " + hauteur
		    + ", largeur: " + largeur
		    + ", prixOuverture: " + prixOuverture + " }";
	}

	public String toStringShort() {
		// TODO -> toStringShort -> afficher l'ID
		return "( #" + "TODO" + " )";
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfTypeOuvertureMur(this);
		return String.join(",",
		    String.valueOf(id),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description),
		    String.valueOf(hauteur),
		    String.valueOf(largeur),
		    String.valueOf(prixOuverture)
		);
	}
}
