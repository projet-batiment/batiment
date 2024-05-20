package fr.insa.dorgli.projetbat.objects.types;

import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class TypeOuvertureMur extends Type {
	private double hauteur;
	private double largeur;
	private double prixOuverture;

	public TypeOuvertureMur() {
		this.hauteur = 0;
		this.largeur = 0;
		this.prixOuverture = 0;
	}
	public TypeOuvertureMur(int id, String nom, String description, double hauteur, double largeur, double prixOuverture) {
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
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
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
