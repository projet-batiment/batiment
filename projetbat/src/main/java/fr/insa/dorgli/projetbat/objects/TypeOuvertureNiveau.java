package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.StructuredToString;

public class TypeOuvertureNiveau extends BObject {
	// j'ai pris le parti d'utiliser "largeur" et "hauteur" pour mieux distinguer
	//   les deux directions (largeur et longueurs peuvent prêter à confusion)
	// Si on ne change plus les TypeOuvertureNiveau ni les TypeOuvertureMur,
	//   alors on pourrait même les rassembler en une seule classe puisqu'ils ont les même propriétés
	/// TODO!!! (utile ici ?) implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
	private String nom;
	private String description;
	private double hauteur;
	private double largeur;
	public double prixOuverture;

	public TypeOuvertureNiveau(int id, String nom, String description, double hauteur, double largeur, double prixOuverture) {
		super(id);
		this.nom = nom;
		this.description = description;
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.prixOuverture = prixOuverture;
	}

	public double getPrixOuverture() {
		return prixOuverture;
	}

	public void setPrixOuverture(double prixOuverture) {
		this.prixOuverture = prixOuverture;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
