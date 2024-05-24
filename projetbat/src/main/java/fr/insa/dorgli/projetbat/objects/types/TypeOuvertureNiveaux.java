package fr.insa.dorgli.projetbat.objects.types;

import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.objects.Serialize;
import fr.insa.dorgli.projetbat.utils.EscapeStrings;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class TypeOuvertureNiveaux extends Type {
	// j'ai pris le parti d'utiliser "largeur" et "hauteur" pour mieux distinguer
	//   les deux directions (largeur et longueurs peuvent prêter à confusion)
	// Si on ne change plus les TypeOuvertureNiveau ni les TypeOuvertureMur,
	//   alors on pourrait même les rassembler en une seule classe puisqu'ils ont les même propriétés
	/// TODO!!! (utile ici ?) implement java.awt.Area -> interset the revetements' surfaces with the ouvertures' surfaces
	private double hauteur;
	private double largeur;
	public double prixOuverture;

	public TypeOuvertureNiveaux() {
		this.hauteur = 0;
		this.largeur = 0;
		this.prixOuverture = 0;
	}
	public TypeOuvertureNiveaux(int id, String nom, String description, double hauteur, double largeur, double prixOuverture) {
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
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .field("largeur", String.valueOf(largeur))
		    .field("hauteur", String.valueOf(hauteur))
		    .field("prixOuverture", String.valueOf(prixOuverture))
        	    .getValue();
	}

	@Override
	public void serialize(Serialize serializer) {
		serializer.csv(
		    super.getId(),
		    nom,
		    description,
		    hauteur,
		    largeur,
		    prixOuverture
		);
	}

	@Override
	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    EscapeStrings.escapeString(nom),
		    EscapeStrings.escapeString(description),
		    String.valueOf(hauteur),
		    String.valueOf(largeur),
		    String.valueOf(prixOuverture)
		);
	}
}
