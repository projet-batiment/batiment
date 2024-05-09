package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.StructuredToString;

public class TypeOuvertureMur extends BObject {
	private String nom;
	private String description;
	private double hauteur;
	private double largeur;
	private double prixOuverture;

	public TypeOuvertureMur(int id, String nom, String description, double hauteur, double largeur, double prixOuverture) {
		super(id);
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

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("nom", nom)
		    .field("description", description)
		    .field("largeur", ""+largeur)
		    .field("hauteur", ""+hauteur)
		    .field("prixOuverture", ""+prixOuverture)
            .getValue();
	}
}
