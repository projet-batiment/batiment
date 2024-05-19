package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class TypeOuvertureNiveau extends Type {
	// j'ai pris le parti d'utiliser "largeur" et "hauteur" pour mieux distinguer
	//   les deux directions (largeur et longueurs peuvent prêter à confusion)
	// Si on ne change plus les TypeOuvertureNiveau ni les TypeOuvertureMur,
	//   alors on pourrait même les rassembler en une seule classe puisqu'ils ont les même propriétés
	/// TODO!!! (utile ici ?) implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
	private double hauteur;
	private double largeur;
	public double prixOuverture;

	public TypeOuvertureNiveau() {
		this.hauteur = 0;
		this.largeur = 0;
		this.prixOuverture = 0;
	}
	public TypeOuvertureNiveau(int id, String nom, String description, double hauteur, double largeur, double prixOuverture) {
		super(id, nom, description);
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

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .field("largeur", String.valueOf(largeur))
		    .field("hauteur", String.valueOf(hauteur))
		    .field("prixOuverture", String.valueOf(prixOuverture))
        	    .getValue();
	}

	@Override
	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description),
		    String.valueOf(hauteur),
		    String.valueOf(largeur),
		    String.valueOf(prixOuverture)
		);
	}
}
