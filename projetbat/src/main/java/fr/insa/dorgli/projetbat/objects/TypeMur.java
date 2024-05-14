package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class TypeMur extends BObject  {
	private String nom;
	private String description;
	private double epaisseur;
	private double prixUnitaire;

	public TypeMur(int id, String nom, String description, double epaisseur, double prixUnitaire) {
		super(id);
		this.nom = nom;
		this.description = description;
		this.epaisseur = epaisseur;
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

	public double getEpaisseur() {
		return epaisseur;
	}

	public void setEpaisseur(double epaisseur) {
		this.epaisseur = epaisseur;
	}

	public double getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(double prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .field("epaisseur", String.valueOf(epaisseur))
		    .field("prixUnitaire", String.valueOf(prixUnitaire))
        	    .getValue();
	}

	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    Deserialize.escapeString(nom),
		    Deserialize.escapeString(description),
		    String.valueOf(epaisseur),
		    String.valueOf(prixUnitaire)
		);
	}
}
