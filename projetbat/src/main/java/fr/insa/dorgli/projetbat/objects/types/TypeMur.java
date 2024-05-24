package fr.insa.dorgli.projetbat.objects.types;

import fr.insa.dorgli.projetbat.objects.Deserialize;
import fr.insa.dorgli.projetbat.objects.Objects;
import fr.insa.dorgli.projetbat.utils.EscapeStrings;
import fr.insa.dorgli.projetbat.utils.StructuredToString;

public class TypeMur extends Type  {
	private double epaisseur;
	private double prixUnitaire;

	public TypeMur() {
		this.epaisseur = 0;
		this.prixUnitaire = 0;
	}
	public TypeMur(int id, String nom, String description, double epaisseur, double prixUnitaire) {
		super(id, nom, description);
		this.epaisseur = epaisseur;
		this.prixUnitaire = prixUnitaire;
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
		return new StructuredToString.OfFancyToStrings(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
		    .field("epaisseur", String.valueOf(epaisseur))
		    .field("prixUnitaire", String.valueOf(prixUnitaire))
        	    .getValue();
	}

	@Override
	public String serialize(Objects objects) {
		return String.join(",",
		    String.valueOf(super.getId()),
		    EscapeStrings.escapeString(nom),
		    EscapeStrings.escapeString(description),
		    String.valueOf(epaisseur),
		    String.valueOf(prixUnitaire)
		);
	}
}
